# <img src="https://github.com/vtrk/VideoTracker/blob/main/frontend/src/favicon.ico" height="25" alt="logo"> VideoTracker
A self-hostable media tracker

## Development Setup
Requirements
- Git
- Intellij IDEA
- Postgresql
- Angular-cli
- Angular 17
- Node.js (v20.11.0 lts/iron recommended)

[Angular version compatibility](https://angular.io/guide/versions)

## Running the [backend](https://github.com/vtrk/VideoTracker/tree/main/backend)
- Import the [database](https://github.com/vtrk/VideoTracker/blob/main/database.sql)
- Open the backend folder in intellij
- For changing settings, check [application.properties](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties) (the default settings work for a locally configured dmbs)
- Build and run the server

### Properties file
The [application.properties](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties) file has the following properties
* [VERSION](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L1): version of the server
* [API](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L2): API to use
  * Values
    * ```Kitsu``` for anime - (Default)
    * ```TMDB``` for movies and tv shows (requires API key to use)
* [TMDB_API_KEY](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L3): API key for TMDB (optional, only mandatory when using TMDB as the API)
* [DB_HOST](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L4): dbms host location
    * Format: ```hostname:port/postgres```
    * Default ```localhost:5432/postgres```
* [DB_USER](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L5): username of dbms user
   * Default: ```postgres```
* [DB_PASSWORD](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L6): password of dbms user
   * Default: ```postgres```
* [LOG_FILE_PATH](https://github.com/vtrk/VideoTracker/blob/main/backend/src/main/resources/application.properties#L7): path to the log file, if empty the server does not log to file
   * Default: ```VideoTracker.log``` 

### Backend Dashboard
The dashboard is accessible at ```hostname:port/```

The default port is 8080

Only users with the [admin](https://github.com/vtrk/VideoTracker/blob/main/database.sql#L6) flag set in the database table [user_vt](https://github.com/vtrk/VideoTracker/blob/main/database.sql#L1-L9) can access the dashboard

## Running the [frontend](https://github.com/vtrk/VideoTracker/tree/main/frontend)
- Open the frontend folder in a terminal
- Run ```npm install``` to download project dependencies
- Run ```ng serve``` to run the frontend

  By default, the frontend will be served at port 4200
