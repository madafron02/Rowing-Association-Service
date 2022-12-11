## Feedback for backlog

- Hashing the password is a non-functional requirement. Move it from functional requirements.
- We should upload the first draft on GitLab.
- Add some 'won't haves' to the backlog.
- Keep notifications as 'could haves'.
- We can add activity deletion as a 'could'.
- We should start putting the backlog on GitLab.


## The bounding context and context map feedback

- The bounding context structure is fine.
- The component diagram should be in UML style and use lollipop notation.
- Component diagram should only have continuous lines.
- No need for ports.


## General questions about the functionality

- We can use the certificate as a boat type.
- We only need a single boat type.
- Modify organization types upon entering them.
- Competetiveness should be included in the activity.
- Training is a subtype of competetiveness.
- We can probably use the email as an id for the User.
- The owner should be able to see his activities (We can add this to the backlog).
- To approve the joining Users for the activities we have two possible approaches:
	- owner logs in and then approves via Postman.
	- we are allowed to put a hyperlink in the email of the notification.
- Add status for matches and store incomplete matches in the database!
- The first interaction should be with the authentication microservice.
- Any User can publish competitions (no need for a Publisher).


## Code of conduct feedback

- Seems okay.
- We should push it to the project repository.


## Mitchell's Covid situation

- He has to talk with the academic counsellor to see if he can avoid the grade penalty.


## Points of action

- Add the template project on GitLab.
- Start coding if we have a chance.


## Notes for future meetings

- Week 5 meeting is at 16:20.
- We will have a final presentation
	- We will get questions from another TA and the lecturers.


## Things that the TA will do

- The TA will send the link explaining what to do with the authentication token ( we shouldn't send it in the header).
- Check if we can use an email as a User's id.