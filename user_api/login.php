<?php
// Include the database connection code from your db_connection.php file
require 'db_connection.php';

// Get the POST data sent from the Android app
$data = json_decode(file_get_contents("php://input"));

$response = array();

if (isset($data->username) && isset($data->password)) {
    $username = $data->username;
    $password = $data->password;

    // Check if the username and password match in the database
    $login_query = "SELECT id, username, token FROM users WHERE username = ? AND password = ?";
    $login_stmt = $conn->prepare($login_query);
    $login_stmt->bind_param("ss", $username, $password);

    if ($login_stmt->execute()) {
        $login_result = $login_stmt->get_result();

        if ($login_result->num_rows > 0) {
            // Login successful
            $user_data = $login_result->fetch_assoc();

            // Include user data in the response
            $response["status"] = "success";
            $response["message"] = "Login successful";
            $response["user_data"] = $user_data;
        } else {
            // Login failed
            $response["status"] = "error";
            $response["message"] = "Login failed. Invalid username or password.";
            $response["user_data"] = null; // Set user data to null
        }
    } else {
        // Database query execution error
        $response["status"] = "error";
        $response["message"] = "Database error";
        $response["user_data"] = null; // Set user data to null
    }

    // Close the login statement when it's no longer needed
    $login_stmt->close();
} else {
    // Invalid request
    $response["status"] = "error";
    $response["message"] = "Invalid request";
    $response["user_data"] = null; // Set user data to null
}

// Close the database connection at the end
$conn->close();

// Output the JSON response
echo json_encode($response);
?>
