### Introduction
This is a Drifting bottle web app created by Team41.
This web app is aiming to helps people with their mental well-being. We provide users with sequences of reflective questions inspired by psychotherapy such as Action and Commitment Therapy(ACT) and Cognitive Behavioral Therapy(CBT). Users answers to these guiding questions are saved on their timeline for future review, in the form of journals.  Users are able to view other’s answers anonymously and left comments with permission. We hope the Drifting Bottle app could give people a pure land to become people's hearts.

### index.html
Our web app start with page "Home". Just run npm install and npm start, it will automatically direct you to home page.

### Library to install
- react-konva: a library to draw shape (installed by running "npm install react-konva konva --save")

### For phase 2
- save theme data in JSON so each page will have a unified theme
- Quest can navigate to profiles for different users with user data in JSON

### "Home" page
- In the home page, there is a button in top left conor saying I'm administrator. Click the Button to go to Admin page
- In center of home page, there is a link "start", click to continuing Sign in or Sign up.

### "Register" page
- In the register page, you can input user email and password.
- Click Sign In button, sign in and redirect to user's "Space" page.
- Click Sign Up button, redirect to the questionnaire(Answers) page, user needs to finish the questionnaire when first sign up, the questionnaire helps them built personal profile.

### "Admin" page
- In the home page, there is a button in top left conor saying I'm administrator. Click the Button to go to Admin page
- In Admin page, click <Delete> to delete a user. In phase2, this user will be deleted from JSON
- You can also input the email of the user and click <Delete User>.

### "MyBottles" page
- In this page, all the bottles a user sent, wrote or recieved is shown in the right. In phase two, the senders' information and content will be read from JSON.
- A user can click the profile picture of a bottle to see it.
- On the left is the user's profile picture and stats about the use's bottles.

### "ABottle" page
- In this page, the content of bottle is shown under "Message from"
- All the replies are shown under "Replies"
- Beside "Replies", a user can write a reply and click reply. Then the reply will be added at the end. In phase two, this reply will be recorded.
- On the left is the sender's profile picture and information. In phase two, these information will be read from JSON

### "Space" / "Admin Space" page
- This is the directory page to everything else.Admin has a manage user button that leads to the list of all users. 
- Four functions in this page.
- In this page, user could choose to 1."see my profile", 2."meet new friends" by click take a quest, 3."check mailbox" and 4."change page view color scheme"

### Answers
- there are three different templates, two gives reflective questions for the user to consider, the other one is an unstructured template. These journals can be sent to other people or saved for the user only. 
- Each user has their own journal, you can set these journals visible or not by others.

### Profile
- contains user summary and preference, in phase 2 the summary will update as user add journals, and the user can adjust their profile avatar as well. 

=======

