"use strict";

const mongoose = require("mongoose");
const validator = require("validator");
const bcrypt = require("bcryptjs");

// Making a Mongoose model a little differently: a Mongoose Schema
// Allows us to add additional functionality.
const UserSchema = new mongoose.Schema({
  email: {
    type: String,
    required: true,
    minlength: 1,
    trim: true,
    unique: true,
    validate: {
      validator: validator.isEmail, // custom validator
      message: "Not valid email",
    },
  },
  password: {
    type: String,
    required: true,
    minlength: 6,
  },
  isAdmin: {
    type: Boolean,
    required: true,
    default: false },
  
  name: {
    type: String,
    required: false,
    minlegth: 1,
    trim: true
  },
  
  age: {
    type: Number,
    required: false,
    default: 18
  },
  // creator: {
  //   type: mongoose.Schema.Types.ObjectId,
  //   required: true
  // },

    // theme and template style settings
  bgcolor: {
    type: String,
    required: false,
    default: "aliceblue"
  },
  tempcolor: {
    type: String,
    required: false,
    default: "white"
  },

  // All the journals that this user have written
  journals: [
    {
      type: mongoose.Schema.Types.ObjectId,
      ref: "Journal",
    },
  ],

  anonymous: { type: Boolean, default: false },
  commentable: { type: Boolean, default: true },
  visible: { type: Boolean, default: true },
});

// An example of Mongoose middleware.
// This function will run immediately prior to saving the document
// in the database.
UserSchema.pre("save", function (next) {
  const user = this; // binds this to User document instance

  // checks to ensure we don't hash password more than once
  if (user.isModified("password")) {
    // generate salt and hash the password
    bcrypt.genSalt(10, (err, salt) => {
      bcrypt.hash(user.password, salt, (err, hash) => {
        user.password = hash;
        next();
      });
    });
  } else {
    next();
  }
});

// A static method on the document model.
// Allows us to find a User document by comparing the hashed password
//  to a given one, for example when logging in.
UserSchema.statics.findByEmailPassword = function (email, password) {
  const User = this; // binds this to the User model

  // First find the user by their email
  return User.findOne({ email: email }).then((user) => {
    if (!user) {
      return Promise.reject(); // a rejected promise
    }
    // if the user exists, make sure their password is correct
    return new Promise((resolve, reject) => {
      bcrypt.compare(password, user.password, (err, result) => {
        if (result) {
          resolve(user);
        } else {
          reject();
        }
      });
    });
  });
};

// make a model using the User schema
const User = mongoose.model("User", UserSchema);
module.exports = { User };
