# Grade Tracker API

## Authorization

### `POST /api/auth/google-signin`
Request: 
<br>Body: ID token string provided by Google Identity service.
<br> Content type: text/plain.

Response: 
<br> 401 (invalid ID token string)
<br> 200 (success; returns `token` as cookie)