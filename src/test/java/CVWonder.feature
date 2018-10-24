Feature: Website testing

Scenario Outline: Creating a user
Given a user is on the add user page
When name "<name>", email "<email>", password "<password>" are entered
And role "<role>" is selected
Then the user is created

Examples:
| name | email | password | role |
| Jordan | jordan@email.com | pass | Trainee|
| Manish | manish@email.com | pass | Trainer|
| Lucy | lucy@email.com | pass | Sales|
| Divine | divine@email.com | pass | Training Manager|

Scenario Outline: User logging in
Given a user is on the login page
When email "<email>" and password "<password>" are entered
Then the user is taken to the page "<page>"

Examples:
| email | password | page |
| jordan@email.com | pass | Trainee Dashboard |
| manish@email.com | pass | Trainer Dashboard |
| lucy@email.com | pass | Sales Dashboard |
| divine@email.com | pass | Training Manager Dashboard |

Scenario: User logging out
Given a user is logged in
When logout is clicked
Then the user is taken to the login page

Scenario: User navigating to contact page
Given a user is logged in
When contact is clicked
Then the user is taken to the contact page

Scenario: Trainee uploading a new CV
Given a trainee is logged in
When a post request with a new CV is sent
Then the new CV should be visible on the trainee dashboard

Scenario: Trainee deleting a CV
Given a trainee is logged in
When a delete request is sent
Then the CV should no longer be visible on the trainee dashboard

Scenario Outline: Non-trainee user searching for a user
Given a training manager is logged in
When search term "<search>" is searched for
Then all users with the search term "<search>" in their name or email should appear

Examples:
| search |
| Jordan |
| Jor |

Scenario: Non-trainee user viewing a trainee
Given a training manager is logged in
When a trainee is searched for
And a trainee is clicked on
Then the trainee profile should appear on screen

Scenario: Training manager updating the status of a CV
Given a training manager is logged in
When a trainee is searched for
And a trainee is clicked on
And flag is clicked
Then the updated status should appear next to the CV