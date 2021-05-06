import React from "react";
// npm install react-konva konva --save

import { Link } from "react-router-dom";
import Button from "@material-ui/core/Button";

import Header from "./../Header";
import Title from "./../Title";
import Sheet from "./Sheet";
import "./theme.css";
// import Img from "./Img"

import { changeTheme } from "../../actions/theme";

class Theme extends React.Component {
  state = {
    user: this.props.appState.currentUser,
    bgcolor: this.props.appState.bgcolor,
    orgcolor: this.props.appState.bgcolor,
  };

  changeBackgroundClick = (e) => {
    const color = e.target.attrs.fill;

    this.setState({
      orgcolor: color,
      bgcolor: color,
    });

    this.props.globalBackground(color);
    if (this.state.user) {
      changeTheme(this.state.user, color);
    }
  };

  changeBackgroundHover = (e) => {
    const color = e.target.attrs.fill;
    this.setState({
      bgcolor: color,
    });
  };

  changeBackgroundLeave = (e) => {
    this.setState({
      bgcolor: this.state.orgcolor,
    });
  };

  render() {
    return (
      <div className="theme_sheet" style={{ background: this.state.bgcolor }}>
        <Header />
        <Title title="Please choose your favorite theme!" />

        <div id="theme_grid">
          <Sheet
            changeBackgroundClick={this.changeBackgroundClick}
            changeBackgroundHover={this.changeBackgroundHover}
            changeBackgroundLeave={this.changeBackgroundLeave}
          />
        </div>

        <Link to={"../Space"} style={{ textDecoration: "none" }} id="linkpos">
          <Button variant="contained" className="myButton">
            Back to My Space
          </Button>
        </Link>
      </div>
    );
  }
}

export default Theme;
