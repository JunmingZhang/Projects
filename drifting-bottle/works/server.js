"use strict";
const log = console.log;

const env = process.env.NODE_ENV;
const USE_TEST_USER = env !== "production" && process.env.TEST_USER_ON; // option to turn on the test user.
const TEST_USER_ID = "5fb8b011b864666580b4efe3"; // the id of our test user (you will have to replace it with a test user that you made). can also put this into a separate configutation file
const TEST_USER_EMAIL = "test@user.com";

const express = require("express");
const app = express();
const session = require("express-session");
const MongoStore = require("connect-mongo");

const { mongoose } = require("./db/mongoose");
mongoose.set("useFindAndModify", false); // for some deprecation issues

const path = require("path");

const { Journal } = require("./models/journal");
const { User } = require("./models/user");
const cors = require("cors");
if (env !== "production") {
  app.use(cors());
}
const { ObjectID } = require("mongodb");

const bodyParser = require("body-parser");
app.use(bodyParser.json()); // parsing JSON body
app.use(bodyParser.urlencoded({ extended: true })); // parsing URL-encoded form data (from form POST requests)

function isMongoError(error) {
  // checks for first error returned by promise rejection if Mongo database suddently disconnects
  return (
    typeof error === "object" &&
    error !== null &&
    error.name === "MongoNetworkError"
  );
}
/* Middleware ******************/
const mongoChecker = (req, res, next) => {
  // check mongoose connection established.
  if (mongoose.connection.readyState != 1) {
    log("Issue with mongoose connection");
    res.status(500).send("Internal server error");
    return;
  } else {
    next();
  }
};

const authenticate = (req, res, next) => {
  console.log("req.session", req.session)
  if (req.session.user) {
    User.findById(req.session.user)
      .then((user) => {
        if (!user) {
          return Promise.reject();
        } else {
          req.user = user;
          next();
        }
      })
      .catch((error) => {
        res.status(401).send("Unauthorized");
      });
  } else {
    res.status(401).send("Unauthorized");
  }
};
/*** Session handling **************************************/
// Create a session and session cookie
app.use(
  session({
    secret: process.env.SESSION_SECRET || "our hardcoded secret", // make a SESSION_SECRET environment variable when deploying (for example, on heroku)
    resave: false,
    saveUninitialized: false,
    cookie: {
      expires: 600000,
      httpOnly: true,
    },
    // store the sessions on the database in production
    store:
      env === "production"
        ? MongoStore.create({
            mongoUrl:
              process.env.MONGODB_URI || "mongodb://localhost:27017/StudentAPI",
          })
        : null,
  })
);

app.post("/users/register", async (req, res) => {
  const email = req.body.email;
  const password = req.body.password;
  const isAdmin = req.body.isAdmin;

  try {
    const existingUser = await User.findOne({ email: email });
    if (existingUser == null) {
      // no user with the same email
      const newuser = User({
        email: email,
        password: password,
        isAdmin: isAdmin,
      });

      try {
        const newUser = await newuser.save();
        res.send(newUser);
      } catch (error) {
        if (isMongoError(error)) {
          // check for if mongo server suddenly dissconnected before this request.
          res.status(500).send("Internal server error");
        } else {
          res.status(400).send("Bad Request" + error); // 400 for bad request gets sent to client.
        }
      }
    } else {
      res.status(409).send("Email exists. Try logging in instead");
    }
  } catch (error) {
    log(error);
    res.status(500).send("Internal Server Error");
  }
});

// A route to login and create a session
app.post("/users/login", (req, res) => {
  const email = req.body.email;
  const password = req.body.password;

  // log(email, password);
  // Use the static method on the User model to find a user
  // by their email and password
  User.findByEmailPassword(email, password)
    .then((user) => {
      // Add the user's id to the session.
      // We can check later if this exists to ensure we are logged in.
      req.session.user = user._id;
      req.session.email = user.email; // we will later send the email to the browser when checking if someone is logged in through GET /check-session (we will display it on the frontend dashboard. You could however also just send a boolean flag).
      res.send({ currentUser: user.email });
    })
    .catch((error) => {
      if (isMongoError(error)) {
        // check for if mongo server suddenly dissconnected before this request.
        res.status(500).send("Internal server error");
      } else {
        res.status(400).send("Bad Request " + error); // 400 for bad request gets sent to client.
      }
    });
});

// A route to logout a user
app.get("/users/logout", (req, res) => {
  // Remove the session
  req.session.destroy((error) => {
    if (error) {
      res.status(500).send(error);
    } else {
      res.send();
    }
  });
});

// A route to check if a user is logged in on the session
app.get("/users/check-session", (req, res) => {
  if (env !== "production" && USE_TEST_USER) {
    // test user on development environment.
    req.session.user = TEST_USER_ID;
    req.session.email = TEST_USER_EMAIL;
    res.send({ currentUser: TEST_USER_EMAIL });
    return;
  }

  if (req.session.user) {
    res.send({ currentUser: req.session.email });
  } else {
    res.status(401).send(req.session.user);
  }
});

/******************** API routes *******************/

// USER ROUTE
app.post("/api/users", mongoChecker, async (req, res) => {
  log(req.body);

  // Create a new user
  const user = new User({
    email: req.body.email,
    password: req.body.password,
    isAdmin: req.body.isAdmin,
  });

  try {
    // Save the user
    const newUser = await user.save();
    res.send(newUser);
  } catch (error) {
    if (isMongoError(error)) {
      // check for if mongo server suddenly disconnected before this request.
      res.status(500).send("Internal server error");
    } else {
      log(error);
      res.status(400).send("Bad Request"); // bad request for changing the student.
    }
  }
});

// a GET route to get all admins
app.get("/api/users", mongoChecker, authenticate, async (req, res) => {
  // Get the students
  try {
    const users = await User.find({ email: req.user.email });
    const user = await User.findOne({ email: req.user.email });
    // res.send(students) // just the array
    res.send({ users: users, user: user }); // can wrap students in object if want to add more properties
  } catch (error) {
    log(error);
    res.status(500).send("Internal Server Error");
  }
});



app.get("/api/users/:id", mongoChecker, authenticate, async (req, res) => {
  /// req.params has the wildcard parameters in the url, in this case, id.
  // log(req.params.id)
  const id = req.params.id;

  // Good practise: Validate id immediately.
  if (!ObjectID.isValid(id)) {
    res.status(404).send(); // if invalid id, definitely can't find resource, 404.
    return; // so that we don't run the rest of the handler.
  }

  // If id valid, findById
  try {
    const user = await User.findOne({ _id: id, email: req.user.email });
    if (!user) {
      res.status(404).send("Resource not found"); // could not find this student
    } else {
      /// sometimes we might wrap returned object in another object:
      res.send(user);
    }
  } catch (error) {
    log(error);
    res.status(500).send("Internal Server Error"); // server error
  }
});

/// a DELETE route to remove a user
app.delete("/api/users", mongoChecker, authenticate, async (req, res) => {
  // Delete a student by their id
  try {
    const user = await User.findOneAndRemove({ email: req.user.email });
    if (!user) {
      res.status(404).send();
    } else {
      res.send(user);
    }
  } catch (error) {
    log(error);
    res.status(500).send(); // server error, could not delete.
  }
});

/// a DELETE route to remove a guest by their id.
app.delete("/api/users/:id", mongoChecker, authenticate, async (req, res) => {
  const id = req.params.id;

  // Validate id
  if (!ObjectID.isValid(id)) {
    res.status(404).send("Resource not found");
    return;
  }

  // Delete a student by their id
  try {
    const user = await User.findOneAndRemove({
      _id: id,
      email: req.user.email,
    });
    if (!user) {
      res.status(404).send();
    } else {
      res.send(user);
    }
  } catch (error) {
    log(error);
    res.status(500).send(); // server error, could not delete.
  }
});

app.patch(
  "/api/users/:id",
  mongoChecker,
  authenticate,
  async function (req, res) {
    const id = req.params.id;

    if (!ObjectID.isValid(id)) {
      res.status(404).send();
      return; // so that we don't run the rest of the handler.
    }

    // var updateFields = req.body;
    const fieldsToUpdate = {};
    req.body.map((change) => {
      const propertyToChange = change.path.substr(1); // getting rid of the '/' character
      fieldsToUpdate[propertyToChange] = change.value;
    });

    try {
      const user = await User.findOneAndUpdate(
        { _id: id, email: req.user.email },
        { $set: fieldsToUpdate },
        { new: true }
      );
      if (!user) {
        res.status(404).send("Resource not found");
      } else {
        res.send(user);
      }
    } catch (error) {
      log(error);
      if (isMongoError(error)) {
        // check for if mongo server suddenly dissconnected before this request.
        res.status(500).send("Internal server error");
      } else {
        res.status(400).send("Bad Request"); // bad request for changing the student.
      }
    }
  }
);

app.patch("/api/users", mongoChecker, authenticate, async function (req, res) {
  // var updateFields = req.body;
  const fieldsToUpdate = {};
  req.body.map((change) => {
    const propertyToChange = change.path.substr(1); // getting rid of the '/' character
    fieldsToUpdate[propertyToChange] = change.value;
  });

  try {
    const user = await User.findOneAndUpdate(
      { email: req.user.email },
      { $set: fieldsToUpdate },
      { new: true }
    );
    if (!user) {
      res.status(404).send("Resource not found");
    } else {
      res.send(user);
    }
  } catch (error) {
    log(error);
    if (isMongoError(error)) {
      // check for if mongo server suddenly dissconnected before this request.
      res.status(500).send("Internal server error");
    } else {
      res.status(400).send("Bad Request"); // bad request for changing the student.
    }
  }
});

//  ********************JOURNAL ROUTERS *****************************//
//get all journals of this user
app.get("/api/journals", mongoChecker, authenticate, async (req, res) => {
  Journal.find()
    .then((journals) => res.send(journals))
    .catch((error) => {
      if (isMongoError(error)) {
        // check for if mongo server suddenly dissconnected before this request.
        res.status(500).send("Internal server error");
      } else {
        res.status(400).send("Bad Request"); // 400 for bad request gets sent to client.
      }
    });
});

//get one journal
app.get("/api/journals/:jid", mongoChecker, authenticate, async (req, res) => {
  Journal.findOne({ _id: req.params.jid })
    .then((journal) => res.send(journal))
    .catch((error) => {
      if (isMongoError(error)) {
        // check for if mongo server suddenly dissconnected before this request.
        res.status(500).send("Internal server error");
      } else {
        res.status(400).send("Bad Request"); // 400 for bad request gets sent to client.
      }
    });
});

//create a journal
app.post("/api/journals", authenticate, (req, res) => {
  const journal = new Journal({
    title: req.body.title,
    content: req.body.content,
    creator: req.user._id,
    createdAt: new Date(),
  });
  journal
    .save()
    .then((journal) => res.send(journal))
    .catch((error) => {
      if (isMongoError(error)) {
        // check for if mongo server suddenly dissconnected before this request.
        res.status(500).send("Internal server error");
      } else {
        res.status(400).send("Bad Request"); // 400 for bad request gets sent to client.
      }
    });
});

app.patch("/api/journals/:jid", (req, res) => {
  Journal.findOneAndUpdate(
    { _id: req.params.jid },
    {
      $set: {
        content: req.body.content,
      },
    },
    { new: true, useFindAndModify: false }
  )
    .then((journal) => {
      journal.save();
      res.send(journal);
    })
    .catch((err) => {
      res.status(500).send(err);
    });
});
app.delete("/api/journals/:jid", (req, res) => {
  Journal.deleteOne({ _id: req.params.jid })
    .then((journal) => {
      res.send(journal);
    })
    .catch((err) => {
      res.status(500).send(err);
    });
});
//***************Admin Routers********** */
// app.delete("/admin", async (req, res) => {
//   const email = req.body.email;
app.delete("/api/admin", mongoChecker, async (req, res) => {
  const email = req.body.email;

  // Validate email
  if (!email) {
    res.status(404).send("Resource not found");
    return;
  }

  // check mongoose connection established.
  if (mongoose.connection.readyState != 1) {
    log("Issue with mongoose connection");
    res.status(500).send("Internal server error");
    return;
  }

  // normal promise version:
  User.deleteOne({ email: email })
    .then((user) => {
      User.find().then((users) => {
        // res.send(students) // just the array
        res.send({ users }); // can wrap students in object if want to add more properties
      });
    })
    .catch((error) => {
      log(error);
      res.status(500).send(); // server error, could not delete.
    });
});

// a GET route to get all users
// app.get("/admin", async (req, res) => {
//   // check mongoose connection established.
//   if (mongoose.connection.readyState != 1) {
//     log("Issue with mongoose connection");
//     res.status(500).send("Internal server error");
//     return;
//   }

//   // normal promise version
//   User.find()
//     .then((users) => {
//       // res.send(students) // just the array
//       res.send({ users }); // can wrap students in object if want to add more properties
//     })
//     .catch((error) => {
//       log(error);
//       res.status(500).send("Internal Server Error");
//     });
// });
app.get("/api/admin", mongoChecker, async (req, res) => {
  
  try {
    const users = await User.find();
    // const users = await User.find({ email: req.user.email });
    // const user = await User.findOne({ email: req.user.email });
    // res.send(students) // just the array
    res.send({ users: users}); // can wrap students in object if want to add more properties
  } catch (error) {
    log(error);
    res.status(500).send("Internal Server Error");
  }
});


app.use(express.static(path.join(__dirname, "/client/build")));
app.get("*", (req, res) => {
  const goodPageRoutes = ["/", "/register"];
  if (!goodPageRoutes.includes(req.url)) {
    // if url not in expected page routes, set status to 404.
    res.status(404);
  }

  res.sendFile(path.join(__dirname, "client/build/index.html"));
});
// Add a new journal into the user's journals
// set up the port
const port = process.env.PORT || 3000;
app.listen(port, () => {
  log(`Listening on port ${port}...`);
});
