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

## Activities
All endpoints requires user authorization.

### `POST /api/classes/{id}/activities`
Adds an activity in the provided class.
<br><br>
Request body (JSON):
<table>
    <tr>
        <td>name (String)</td>    
        <td>Name of the activity. For example, quiz, test, assignment etc.</td>    
    </tr>
    <tr>
        <td>weight (Double)</td>    
        <td>Weight of the activity in the final grade in percentage.</td>    
    </tr>
    <tr>
        <td>total (Double) (Optional)</td>    
        <td>
            Total score on which the activity is graded on. For example,
            weight may be 5% but the total score can be 100.
            <br>
            Can be excluded if unknown.
        </td>    
    </tr>
    <tr>
        <td>score (Double) (Optional)</td>    
        <td>
            Score achieved by the user.
            <br>
            Can be excluded to indicate that the activity has not been graded yet.
        </td>    
    </tr>
</table>

Response:
<br> 201 (success; returns the newly created activity)
<br> 401 (unauthorized)
<br>

### `POST /api/classes/{id}/activities/ordering`
Changes the order of activities. 

Request body: Ordered JSON array of activity ids. If one or more activity 
ids are not provided but they are in the class, then they are put at the end
of newly ordered activities.

Response:
<br> 200 (success; returns activity list)

### `PATCH /api/activities/{id}`
Updates the provided activity. Usually used for updating the 'score' attribute.
<br><br>
Request body (JSON): Same as the post endpoint, but all keys are optional.
<br><br>
Response:
200 (succeed; returns the updated activity)

### `DELETE /api/activities/{id}`
Response:
<br> 204 (success)