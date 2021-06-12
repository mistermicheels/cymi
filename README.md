Project status:

-   🛑 Not actively maintained
-   🔒 Not looking for code contributions from other developers

# Can You Make It

This is the code for Can You Make It, a web application allowing people to indicate whether they will join certain events.

The application is mostly a playground that lets me try out all kinds of stuff in the context of an actual application. There will likely be some over-engineering, especially given the size of the development team (only me) and userbase (no one).

## Functionality

Users can create groups and invite others to those groups. Within groups, admins can create events. All group members can then RSVP to those events.

## Signing up

Users can sign up based on their email address. They will receive an email to confirm this email address.

It's also possible to invite a not-yet-registered user to a group based on their email address. The user will receive an email invite with a token that can be used to immediately confirm their email address.

## Authentication and authorization

Users are required to confirm their email before being able to log in. Because the user's email address is used as identification for group invitations, logging in without a confirmed email is not allowed.

A successful login produces a session token (required for every request) and CSRF token (required for every state-changing request). Making a request with an invalid session token (or CSRF token if it's required) triggers a relogin by returning a 401 response.

If users try to access anything they shouldn't have access to (example: a group they are not a member of), they are redirected to the home page by returning a 403 response.

## Conventions

### Commit messages

As different parts of the application sit together in one single repo, it can make sense to indicate which part of the application a commit belongs to.

Convention to follow:

-   If the change is specific to the code for a certain part and does not change the behavior of the application as observed by the user, prefix the commit message with that part
    -   Example: [backend: make tests use correct assertEquals semantics](https://github.com/mistermicheels/cymi/commit/a030614aa1d5ed9eeed6fb9b32b298f6d117d58f)
-   Otherwise, don't include a prefix, even if the change only touches a certain part of the codebase
    -   Example: [don't ask user to confirm email if they have not signed up yet](https://github.com/mistermicheels/cymi/commit/2ec2962cb84a6190ae2bb2af2fafd5ed64e57021)
