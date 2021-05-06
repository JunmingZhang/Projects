import React from "react";
import "./style.css";
import { Link } from "react-router-dom";

// we will be taking profile picture from the dataset
import profilepic from "./FacePic/1.jpeg";
import profilepic1 from "./FacePic/2.jpeg";
import profilepic2 from "./../../Information/profilepicpurple.png";

class Bottle extends React.Component {
  render() {
    const { sender, date, content, userEmail, tempcolor } = this.props;
    console.log(userEmail);
    console.log(sender);
    if (userEmail === sender) {
      return (
        <div className="bottle">
          <div className="bottleIconContainer">
            <Link to={"./../ABottle"}>
              <img
                src={profilepic2}
                className="bottleIcon"
                alt=" profile pic of user"
              />
            </Link>
          </div>

          <h3>
            {sender}
            <span className="grey">@{date}</span>
          </h3>
          <div className="txt">{content}</div>
        </div>
      );
    }
    return (
      <div className="bottle" style={{background: {tempcolor}}}>
        <div className="bottleIconContainer">
          <Link to={"./../ABottle"}>
            <img
              src={profilepic, profilepic1}
              className="bottleIcon"
              alt=" profile pic of user"
            />
          </Link>
        </div>

        <h3>
          {sender}
          <span className="grey">@{date}</span>
        </h3>
        {content}
      </div>
    );
  }
}
export default Bottle;
