# Grade Tracker API

## Authorization

### `POST /api/auth/google-signin`
Request: 
<br>Body: ID token string provided by Google Identity service.
<br> Content type: text/plain.

Response: 
<br> 401 (invalid ID token string)
<br> 200 (success; returns `token` as cookie)

## Classes
All endpoints requires user authorization.

### `POST /api/classes`
Adds an enrolled class for the authorized user.
<br><br>
Request body (JSON):
<table>
    <tr>
        <td>course_name (String)</td>    
        <td>Name of the course.</td>    
    </tr>
    <tr>
        <td>section (String)</td>    
        <td>Signle character.</td>    
    </tr>
    <tr>
        <td>term (String)</td>    
        <td>Valid values are: FALL, WINTER, SUMMER.</td>    
    </tr>
    <tr>
        <td>year (Integer)</td>    
        <td>Four digit year.</td>    
    </tr>
</table>

Response:
<br> 201 (success; returns the newly created object)
<br> 401 (unauthorized)
<br>

### `GET /api/classes`
Response:
<br> 200 (success; returns an array of class objects)
<br>

### `DELETE /api/classes/{id}`
Response:
<br> 204 (success)
<br> 403 (if the class doesn't belong to the user)