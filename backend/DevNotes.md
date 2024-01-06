# DevNotes
## POST /search
### Request
```json
{
  "type": "content_type",
  "query": "search_query",
  "args": {
    "arg1": "arg1",
    "arg2": "arg2"
  }
}
```
### Response
JSON (API dependent)

To test this endpoint, use one of the following curl commands:
## Kitsu (Anime)
With args:
```bash
curl -H "Accept: text/plain" -H "Content-type: text/plain" -X POST -d '{"type":"anime","query":"bleach","args":{"categories":"Action"}}' http://localhost:8080/search
```
Without args:
```bash
curl -H "Accept: text/plain" -H "Content-type: text/plain" -X POST -d '{"type":"anime","query":"bleach"}' http://localhost:8080/search
```
## TheMovieDB (Movies)
With args:
```bash
curl -H "Accept: text/plain" -H "Content-type: text/plain" -X POST -d '{"type":"movie","query":"spider man","args":{"primary_release_year":"2021"}}' http://localhost:8080/search
```
Without args:
```bash
curl -H "Accept: text/plain" -H "Content-type: text/plain" -X POST -d '{"type":"movie","query":"spider man"}' http://localhost:8080/search
```
## TheTVDB (TV Shows)
With args:
```bash
curl -H "Accept: text/plain" -H "Content-type: text/plain" -X POST -d '{"type":"tv","query":"the office","args":{"first_air_date_year":"2005"}}' http://localhost:8080/search
```
Without args:
```bash
curl -H "Accept: text/plain" -H "Content-type: text/plain" -X POST -d '{"type":"tv","query":"the office"}' http://localhost:8080/search
```