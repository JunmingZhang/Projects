# Drifting Bottles

## Introduction
This is a Drifting bottle web app created by Team41. This web app is aiming to helps people with their mental well-being. We provide users with sequences of reflective questions inspired by psychotherapy such as Action and Commitment Therapy(ACT) and Cognitive Behavioral Therapy(CBT). Users answers to these guiding questions are saved on their timeline for future review, in the form of journals. Users are able to view other’s answers anonymously and left comments with permission. We hope the Drifting Bottle app could give people a pure land to become people's hearts.

## Simple instructions on how to start
1. run `npm install` in the root directory to install _node_modules_ and initialize _package-lock.json_.
2. run `mongod --dbpath mongo-data` in the root directory, and then connect to the mongoDB.
3. create a database in mongoDB called __DriftBottleAPI__, and a collection called __users__.
4. run `node server.js` in the root directory to start the server (port #: 3000).
5. run `npm install` in the _client_ directory to install _node_modules_ and initialize _package-lock.json_.
6. run `npm start` in the _client_ directory to start the web app.

## URL (not recommended)
https://peaceful-savannah-72134.herokuapp.com

## Features of Drifting Bottles:
1. Sign-in, Sign-up, Create User Profile
2. Change web page color style.
3. Communicate with other user through showing profiles and send message.
4. Create user's own journal based on user profile.

## How to use (User side)
Please read the __Features of Drifting Bottles__ section.

## How to use (Admin side)
The admin can manage the user, except for all functionalities of an ordinary user, and admin can delete an ordinary user or his journal.

## Web pages

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

## Dependencies
1. react-konva: a library to draw shape (installed by running `npm install react-konva konva --save`)
2. mongoose: a library for connecting the library (installed by running `npm install mongoose --save`)
3. body-parser: a middleware used to parse the JavaScript dictionary object to JSON object for sending imformation (installed by running `npm install body-parser --save`)

## Testing account
1. essential for creating an ordinary user
<pre>
<code>
{
    "email": "user@gmail.com",
    "password": "12345678",
    "isAdmin": false
}
</code>
</pre>
2. essential for creating an admin:
<pre>
<code>
{
    "email": "admin@gmail.com",
    "password": "adminadmin",
    "isAdmin": true
}
</code>
</pre>

## Model
1. User (normal guest/admin)
<pre>
<code>
{
    email:
    {
        type: String
        required: true
        minlength: 1
    },
    password:
    {
        type: String
        required: true
        minlength: 6
    },
    isAdmin:
    {
        type: Boolean
        required: true
    },
    name: String,
    age: Number,
    bgcolor:
    {
        type: String.
        default: "aliceblue"
    },
    tempcolor:
    {
        type: String.
        default: "aliceblue"
    },
    anonymous: { type: Boolean, default: false },
    commentable: { type: Boolean, default: true },
    visible: { type: Boolean, default: true },
    journals: [{ document to the model journal },],
}
</code>
</pre>

2. Journal
<pre>
<code>
{
    title: String,
    content:
    {
        type: String,
        required: true
    },
    creator:
    {
        type: mongoose.Schema.Types.ObjectId,
        ref: "user"
    },
    createdAt: Date(),
}
</code>
</pre>

## Router
1. Session handling
- `POST http://localhost:3000/users/register`
Register a new user account
    - body: Look for the __Testing account__ part
    - header: "Content-Type": "application/json"
- `POST http://localhost:3000/user/login`
Log in your account (for example, an admin)
    - body:
    <pre>
    <code>
    {
    "email": "admin@gmail.com",
    "password": "adminadmin",
    }
    </code>
    </pre>
    - header: "Content-Type": "application/json"
- `GET http://localhost:3000/user/logout`
Logout your current account
- `GET http://localhost:3000/users/check-session`
Check if a user is logged in on the session

2. API routes
- `POST http://localhost:3000/api/users`
    - another way to register an account
- `GET http://localhost:3000/api/users/email`
Get a user account by email (email passed from middleware _authenticate_)
- `DELETE http://localhost:3000/api/users/email`
Delete a user by email (email passed from middleware _authenticate_)
- `PATCH http://localhost:3000/api/users/email`
Update a user account (for example, background color) by email (email passed from middleware _authenticate_) with fields in request body
    - update body demo
    <pre>
    <code>
    {
        "bgcolor": "xxx",
        "tempcolor": "xxx"
    }
    </code>
    </pre>
    - header: "Content-Type": "application/json"
- `GET http://localhost:3000/api/journals`
Return all journals
- `GET /api/journals/:jid`
Get one journal by ID
- `POST http://localhost:3000/api/journals`
Write one journal
    - body:
    <pre>
    <code>
    {
    "title": "string title",
    "password": "string content",
    "creator": ID of the creator,
    "CrearedAt": Date()
    }
    </code>
    </pre>
    - header: "Content-Type": "application/json"
- `PATCH http://localhost:3000/api/journals/api/journals/:jid`
 Update a journal by its jid with given fields in request body
    - body:
    <pre>
    <code>
    {
    "title": "string title",
    "password": "string content",
    "CrearedAt": Date()
    }
    </code>
    </pre>
    - header: "Content-Type": "application/json"
- `DELETE http://localhost:3000/api/journals/api/journals/:jid`
Delete a user by given jid
