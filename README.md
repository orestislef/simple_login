# Simple Login
This project consists of two main components: a user API developed for XAMPP located in the `user_api` folder and an Android application written in native Java.

## User API Installation Instructions

### Prerequisites
- XAMPP installed on your machine.

### Installation Steps
1. Clone or download this repository to your local machine.
2. Navigate to the `user_api` folder.
3. Copy the contents of this folder into the `htdocs` directory of your XAMPP installation.
4. Start the Apache and MySQL services in XAMPP.
5. Import the provided database schema into your MySQL database. You can find the database schema in the `database` folder.
6. Update the database connection details in the API files (`config.php` or similar) to match your MySQL configuration.

## Android Application

### Setup
1. Open the Android application project in your preferred IDE (Android Studio recommended).
2. Modify the `BASE_URL` constant to match the URL where your user API is hosted.

### Functionality
- The Android application provides login and register functionality.
- Upon successful login, users are redirected to the main home page.
- Users can change their passwords from the main home page if logged in successfully.

## Contributors
- [OrestisLef(https://github.com/orestislef)

## License
This project is licensed under the [MIT License](LICENSE).
