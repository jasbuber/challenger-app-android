# Description

This is an android application that allows people to create video challenges that others can join and try to complete. A person, creating a challenge,
needs to upload a video description so that other people understand what is it all about. After completing a challenge, a video response can be uploaded
by a participant, showing that a challenge was completed. Challenge creator can accept or reject such response, granting points or deleting response if it failed.

# Prerequisites

This app uses server side that needs to be available in order for it to work. Server side is available in @challenge-app repository.
Server main url address should be provided in Router.java.

```
private static final String server = "server_url_address";
```

Since application is tightly coupled with facebook, a facebook account is necessary to run it.


# Getting Started

Build and generate apk in a standard android way.
