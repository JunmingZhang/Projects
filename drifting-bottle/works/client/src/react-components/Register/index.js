// Everything here was previously in the App component.
import React from "react";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import { Link } from "react-router-dom";
import Input from "./../Input";
import "./styles.css";

import Title from "./../Title";
import { updateLoginForm, login, signup } from "../../actions/user";
class Register extends React.Component {
  ///  React 'state'.
  // Allows us to keep track of changing data in this component.

  state = {
    bgcolor: this.props.appState.bgcolor,
    email: "", // User name is register email.
    password: "",
    l: "./../Space",
    admins: this.props.appState.admins
  };
  
  render() {
    const { app } = this.props;
    console.log("appState", this.props.appState.admins)
    console.log("this.state", this.state)
    
    return (
      <div className="register" style={{ background: this.state.bgcolor }}>
        {/* Header component with text props. */}
        <Title
          title={`Welcome to Drifting Bottle`}
          // subtitle={`Please input you email and password below`}
        />

        {/* User Form component with text and function props. */}
        <Grid className="user-form" container spacing={4}>
          {/* Inputs to add student */}
          <Input
            name="email"
            value={this.state.email}
            onChange={(e) => updateLoginForm(this, e.target)}
            label="Email"
          />

          <Input
            name="password"
            value={this.state.password}
            onChange={(e) => updateLoginForm(this, e.target)}
            label="Password"
          />

          <Grid
            className="user-form__button-grid"
            item
            xl={2}
            lg={2}
            md={12}
            s={12}
            xs={12}
          >
            
            <Link to={{pathname: this.state.l, state: this.state.email}} >
              <Button
                variant="contained"
                color="primary"
                className="user-form__submit-button"
                onClick={() => login(this, app)}
              >
                Sign in
              </Button>
            </Link>
            <Link to={{pathname: this.state.l, state: this.state.email}}>
              <Button
                variant="contained"
                color="primary"
                className="user-form__submit-button"
                onClick={() => signup(this, app)}
              >
                Sign up
              </Button>
            </Link>
          </Grid>
        </Grid>

        <Grid className="user-form" container spacing={8}></Grid>
      </div>
    );
  }
}

export default Register;
