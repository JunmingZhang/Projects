import React from "react";
import "./style.css";
import Header from "./../Header";
import Bottle from "./Bottle";
import { loadAllJournals } from "../../actions/journal";

class MyBottles extends React.Component {
  // componentDidMount() {
  //   loadAllJournals(this);
  // }
  state = {
    bgcolor: this.props.appState.bgcolor,
    tempcolor: this.props.appState.tempcolor,
    userName: "lowyang@fmail.com",


    bottles: [
      {
        sender: "bei@gmail.com",
        date: "2021-4-04",
        content: "Had a rough couple of days. ",
      },
      {
        sender: "bei@gmail.com",
        date: "2021-4-02",
        content:
          "Getting some sunlight really wakes you up. I've been taking walks after breakfast and it really brings me energy. ",
      },
    ],
  };

  render() {
    return (
      // <div className="page" style={{ background: this.state.bgcolor }}>
      <div className="home__bg-image center">
        <Header />
        <div id="timeline">
          <div className="timelineHeader">
            <ul>
              <li>My Bottles</li>
            </ul>
          </div>
          {this.state.bottles.map((bottle) => {
            return (
              <Bottle
                sender={bottle.sender}
                date={bottle.date}
                content={bottle.content}
                userName={this.state.userName}
                tempcolor={this.state.tempcolor}
              />
            );
          })}
        </div>
      </div>
    );
  }
}

export default MyBottles;
