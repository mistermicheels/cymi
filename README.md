# Can You Make It

This is the code for Can You Make It, a web application allowing people to indicate whether they will join certain events.

The application is mostly a playground that lets me try out all kinds of stuff in the context of an actual application. There will likely be some over-engineering, especially given the size of the development team (only me) and userbase (no one).

## Conventions

### Commit messages

As different parts of the application sit together in one single repo, it can make sense to indicate which part of the application a commit belongs to.

Convention to follow:
- If the change is specific to the code for a certain part and does not change the behavior of the application as observed by the user, prefix the commit message with that part (example: "backend: make tests use correct assertEquals semantics")
- Otherwise, don't include a prefix, even if the change only touches a certain part of the codebase (example: "don't ask user to confirm email if they have not signed up yet")