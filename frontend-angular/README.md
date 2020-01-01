# CYMI Angular frontend

## Authentication

Authentication and authorization errors are dealt with by AuthInterceptor.

-   401:
    -   If user was currently logged in, show a modal to log in again. As the underlying page is not touched, any data the user already entered is preserved. If the user successfully logs in again, the request(s) that triggered the relogin are retried.
    -   If the user was not logged in yet, navigate to the login page.
-   403: Navigate to the home page.

Routes that assume the user to be authenticated are also protected by a route guard called AuthGuard. One important effect of this is that component loading waits for the authentication to be initialized when accessing those routes directly.

## Development process

### Formatting and linting

Visual Studio Code is configured to automatically format files on save, using Prettier. Saving a file also fixes all auto-fixable TSLint errors. This configuration is committed to version control at the top level of the repository under `.vscode`. The build script also checks for incorrect formatting or linting errors (see below).

### Build script

There is a build script that does the following:

1. Check dependencies for vulnerabilities
2. Check that all relevant files have Prettier formatting
3. Check for TSLint errors
4. Run unit tests
5. Run end-to-end tests
6. Build the frontend with ahead-of-time compilation enabled (detects things like a template referring to a non-existent property)
