# Tutor Flow Frontend

This is the frontend for the Tutor Flow project, built with React and Vite. 
It provides an intuitive and responsive interface for managing lessons, students, and user profiles.

## Technologies Used
- **React** - Component-based UI library
- **Vite** - Fast build tool for frontend development
- **React Router DOM** - Handles navigation between different pages
- **Mantine** - UI component library for styling and interactivity

## Features
- **Authentication & Security**
    - Users must be logged in to access any pages except login.
    - Redirection to login page if unauthorized.

- **User Profile**
    - Displays user information.
    - Quick access to the student list and recent lessons.
    - Allows editing username and managing students.

- **Lessons Management**
    - Dashboard with all lessons, including filtering, sorting, and search functionality.
    - Ability to view, edit, or delete specific lessons.
    - Page to add new lessons.

- **Responsive Design**
    - Optimized for both desktop and mobile.
    - Different navigation bars for each version.

## Project Setup
To set up and run the frontend locally:

1. Clone the repository:
   ```sh
   git clone https://github.com/Hortensjaa/TutorFlow
   cd frontend
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the development server:
   ```sh
   npm run dev
   ```
4. Open [http://localhost:5173](http://localhost:5173) in your browser.

## Folder Structure
```
tutor-flow-frontend/
│── src/
│   ├── api/               # Requests to backend api
│   ├── components/        # UI components
│   ├── providers/         # Global state management
│   ├── models/            # Request/Response Models
│   ├── App.jsx            # Main App component
│   ├── main.jsx           # Entry point
│── package.json           # Project dependencies and scripts
│── vite.config.js         # Vite configuration
```

## Build and Deployment
To build the project for production:
```sh
npm run build
```
This will create an optimized `dist/` folder with production-ready files.

To preview the build locally:
```sh
npm run preview
```
