<?php
// Include the database connection code from your db_connection.php file
require 'db_connection.php';

// Read the JSON input from the request body and decode it into a PHP object
$json_input = file_get_contents("php://input");
$request = json_decode($json_input);

$response = array();

if ($request !== null) {
    $token = $request->token;
    $new_password = $request->new_password;

    if (!empty($token) && !empty($new_password)) {
        // Check if the token exists in the users table
        $check_query = "SELECT token FROM users WHERE token = ?";
        $check_stmt = $conn->prepare($check_query);
        $check_stmt->bind_param("s", $token);

        if ($check_stmt->execute()) {
            $check_result = $check_stmt->get_result();

            if ($check_result->num_rows > 0) {
                // Update the password in the users table
                $update_query = "UPDATE users SET password = ? WHERE token = ?";
                $update_stmt = $conn->prepare($update_query);
                $update_stmt->bind_param("ss", $new_password, $token);

                if ($update_stmt->execute()) {
                    // Password change successful
                    $response["status"] = "success";
                    $response["message"] = "Password change successful";

                    // Retrieve the updated user data
                    $user_query = "SELECT * FROM users WHERE token = ?";
                    $user_stmt = $conn->prepare($user_query);
                    $user_stmt->bind_param("s", $token);
                    $user_stmt->execute();
                    $user_result = $user_stmt->get_result();
                    $user_data = $user_result->fetch_assoc();
                    $response["user_data"] = $user_data;

                    // Close the user data statement
                    $user_stmt->close();
                } else {
                    // Database update failed
                    $response["status"] = "error";
                    $response["message"] = "Password change failed: " . $conn->error;
                }

                // Close the update statement when it's no longer needed
                $update_stmt->close();
            } else {
                // Token not found in the users table
                $response["status"] = "error";
                $response["message"] = "Token not found";
            }
        } else {
            // Database query execution error
            $response["status"] = "error";
            $response["message"] = "Database error";
        }

        // Close the check statement when it's no longer needed
        $check_stmt->close();
    } else {
        // Invalid request
        $response["status"] = "error";
        $response["message"] = "Invalid request";
    }
} else {
    // Invalid JSON input
    $response["status"] = "error";
    $response["message"] = "Invalid JSON input";
}

// Close the database connection at the end
$conn->close();

// Output the JSON response
echo json_encode($response);
?>
