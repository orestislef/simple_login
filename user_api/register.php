<?php
// Include the database connection code from your db_connection.php file
require 'db_connection.php';

// Function to generate a random token (you should use a more secure method)
function generateToken() {
    return bin2hex(random_bytes(32)); // Generates a 64-character token
}

// Get the POST data sent from the Android app
$data = json_decode(file_get_contents("php://input"));

$response = array();

if (isset($data->username) && isset($data->password)) {
    $username = $data->username;
    $password = $data->password;

    // Check if the username already exists in the database
    $check_query = "SELECT id FROM users WHERE username = ?";
    $check_stmt = $conn->prepare($check_query);
    $check_stmt->bind_param("s", $username);

    if ($check_stmt->execute()) {
        $check_result = $check_stmt->get_result();

        if ($check_result->num_rows > 0) {
            // Username already exists, registration failed
            $response["status"] = "error";
            $response["message"] = "Username already exists";
            $response["user_data"] = null; // Set user data to null
        } else {
            // Generate a token (you should use a secure method to generate tokens)
            $token = generateToken();

            // Insert the new user into the database along with the token
            $insert_query = "INSERT INTO users (username, password, token) VALUES (?, ?, ?)";
            $insert_stmt = $conn->prepare($insert_query);
            $insert_stmt->bind_param("sss", $username, $password, $token);

            if ($insert_stmt->execute()) {
                // Registration successful
                $response["status"] = "success";
                $response["message"] = "Registration successful";
                
                // Retrieve the user data and include it in the response
                $user_query = "SELECT id, username, token FROM users WHERE username = ?";
                $user_stmt = $conn->prepare($user_query);
                $user_stmt->bind_param("s", $username);
                $user_stmt->execute();
                $user_result = $user_stmt->get_result();
                $user_data = $user_result->fetch_assoc();
                $user_stmt->close();
                
                $response["user_data"] = $user_data;
            } else {
                // Database insertion failed
                $response["status"] = "error";
                $response["message"] = "Registration failed: " . $conn->error; // Include the error message
                $response["user_data"] = null; // Set user data to null
            }

            // Close the insert statement when it's no longer needed
            $insert_stmt->close();
        }
    } else {
        // Database query execution error
        $response["status"] = "error";
        $response["message"] = "Database error";
        $response["user_data"] = null; // Set user data to null
    }

    // Close the check statement when it's no longer needed
    $check_stmt->close();
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
