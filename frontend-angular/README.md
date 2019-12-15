# CYMI Angular frontend

## Authentication

Authentication and authorization errors are dealt with by AuthInterceptor.

-   401:
    -   If user was currently logged in, show a modal to log in again. As the underlying page is not touched, any data the user already entered is preserved. If the user successfully logs in again, the request(s) that triggered the relogin are retried.
    -   If the user was not logged in yet, navigate to the login page.
-   403: Navigate to the home page.
