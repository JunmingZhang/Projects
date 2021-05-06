/* Restaurant and Reservation Models */
// DO NOT CHANGE THIS FILE

const mongoose = require("mongoose");

const JournalSchema = new mongoose.Schema({
  title: {
    type: String,
    default: "<Untitled>",
  },
  content: {
    type: String,
    required: [true, "A journal must have a body"],
  },
  template: String,

  creator: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "User",
  },
  //points to a creator, one journal has one creator,
  // but one creator may have many journals
  createdAt: Date,
});

const Journal = mongoose.model("Journal", JournalSchema);

module.exports = { Journal };
