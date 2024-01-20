# <img src="https://github.com/vtrk/VideoTracker/blob/main/frontend/src/favicon.ico" height="25" alt="logo"> VideoTracker
A self-hostable media tracker

## Development Setup
Requirements
- Git
- Intellij IDEA
- Postgresql
- Angular-cli

## Running the [backend](https://github.com/vtrk/VideoTracker/tree/main/backend)
- Import the [database](https://github.com/vtrk/VideoTracker/blob/main/database.sql)
- Open the backend folder in intellij
- For changing settings, check [application.properties](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties) (the default settings work for a locally confiigured dmbs)
- Build and run the server

### Properties file
The [application.properties](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties) file has the following properties
* [VERSION](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L1): version of the server
* [API](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L2): API to use
  * Values
    * ```Kitsu``` for anime
    * ```TMDB``` for movies and tv shows (requires API key to use)
* [TMDB_API_KEY](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L3): API key for TMDB (optional, only mandatory when using TMDB as the API)
* [DB_HOST](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L4): dbms host location
    * Format: ```hostname:port/postgres```
* [DB_USER](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L5): username of dbms user
* [DB_PASSWORD](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L6): password of dbms user

### Backend Dashboard
The dashboard is accessible at ```hostname:port/```

The default port is 8080

Only users with the [admin](https://github.com/vtrk/VideoTracker/blob/main/database.sql#L6) flag set in the database table [user_vt](https://github.com/vtrk/VideoTracker/blob/main/database.sql#L1-L9) can access the dashboard

## Running the [frontend](https://github.com/vtrk/VideoTracker/tree/main/frontend)
- Open frontend in a terminal
- Run ```ng-serve```

  By default, the frontend will be served at port 4200
