# VolunteerApp

VolunteerApp is an Android application designed to help users find and register for volunteer opportunities. The app supports different user roles including Admin, Organizer, and Volunteer, each with specific functionalities.

## Features

- **Admin Dashboard**: Manage users and events.
- **Organizer Dashboard**: Create and manage events, view registered volunteers.
- **Volunteer Dashboard**: Browse and register for events.
- **User Authentication**: Login and registration for users.
- **Dark Mode Support**: The app supports both light and dark modes.

## Screenshots

![Home Page](screenshots/home_page.png)
![Login Page](screenshots/login_page.png)
![Admin Dashboard](screenshots/admin_dashboard.png)
![Organizer Dashboard](screenshots/organizer_dashboard.png)
![Volunteer Dashboard](screenshots/volunteer_dashboard.png)

## Installation

1. **Clone the repository**:
    ```sh
    git clone https://github.com/yourusername/VolunteerApp.git
    cd VolunteerApp
    ```

2. **Open the project in Android Studio**:
    - Open Android Studio.
    - Click on `File > Open` and select the `VolunteerApp` directory.

3. **Build the project**:
    - Click on `Build > Rebuild Project` to build the project.

4. **Run the app**:
    - Connect an Android device or start an emulator.
    - Click on `Run > Run 'app'`.

## Usage

1. **Home Page**: The home page provides an overview of the app and options to login or register.
2. **Login**: Users can login using their credentials. Based on their role, they will be redirected to the appropriate dashboard.
3. **Admin Dashboard**: Admins can manage users and events.
4. **Organizer Dashboard**: Organizers can create and manage events, and view registered volunteers.
5. **Volunteer Dashboard**: Volunteers can browse and register for events.

## Project Structure

VolunteerApp/ ├── app/ │ ├── src/ │ │ ├── main/ │ │ │ ├── java/com/example/volunteerapp/ │ │ │ │ ├── activities/ │ │ │ │ │ ├── AdminDashboardActivity.java │ │ │ │ │ ├── OrganizerDashboardActivity.java │ │ │ │ │ ├── VolunteerDashboardActivity.java │ │ │ │ │ ├── LoginActivity.java │ │ │ │ │ ├── RegistrationActivity.java │ │ │ │ │ ├── HomePageActivity.java │ │ │ │ ├── helpers/ │ │ │ │ │ ├── DatabaseHelper.java │ │ │ ├── res/ │ │ │ │ ├── layout/ │ │ │ │ │ ├── activity_admin_dashboard.xml │ │ │ │ │ ├── activity_organizer_dashboard.xml │ │ │ │ │ ├── activity_volunteer_dashboard.xml │ │ │ │ │ ├── activity_login.xml │ │ │ │ │ ├── activity_registration.xml │ │ │ │ │ ├── activity_home_page.xml │ │ │ │ ├── values/ │ │ │ │ │ ├── colors.xml │ │ │ │ │ ├── styles.xml │ │ │ │ ├── values-night/ │ │ │ │ │ ├── colors.xml │ │ │ ├── drawable/ │ │ │ │ ├── rounded_button.xml ├── build.gradle ├── settings.gradle └── README.md



## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

