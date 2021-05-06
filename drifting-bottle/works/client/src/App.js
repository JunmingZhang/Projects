import React from "react";
import { Route, Switch, BrowserRouter } from "react-router-dom";
import "./App.css";

import "./config";

// Importing the Answers and our simple Profile Page
import Answers from "./react-components/Answers";
import CBT from "./react-components/Answers/CBT";
import ACT from "./react-components/Answers/ACT/ACT";
import Blank from "./react-components/Answers/Blank";
import tenq from "./react-components/Answers/CBT/tenq.js";
import Profile from "./react-components/Profile";
import Quest from "./react-components/Quest";
import Register from "./react-components/Register";
import Space from "./react-components/Space";
import Theme from "./react-components/Theme";
import Template from "./react-components/Template";
import Admin from "./react-components/Admin";
import MyBottles from "./react-components/MyBottles";
import ABottle from "./react-components/ABottle";
import AdminSpace from "./react-components/AdminSpace";

//import { checkSession } from "./actions/user";
import { readAppTheme } from "./actions/theme";
import { readAppTemplate } from "./actions/template";
import { checkSession } from "./actions/user";

class App extends React.Component {
  componentDidMount() {
    checkSession(this); // sees if a user is logged in
    if (this.state.currentUser) {
      readAppTheme(this);
      readAppTemplate(this);
    }
  }

  state = {
    // user:null,
    term: "Winter 2021",
    bgcolor: "aliceblue",
    tempcolor: null,
    currentUser: null,
    admins: ["admin@gmail.com"],
    currentUserInfo: null,
  };

  onChangeBackground = (color) => {
    this.setState({ bgcolor: color });
  };

  onChangeTemplate = (color) => {
    this.setState({ tempcolor: color });
  };

  render() {
    const { currentUser } = this.state;
    return (
      <div>
        <BrowserRouter>
          <Switch>
            {" "}
            {/* Home Page */}
            <Route
              exact
              path={["/", "/space", "/register"]}
              // if the user wants to go to user profile/register/admin page, check current user first
              render={() => (
                <div className="app">
                  {/* Different componenets rendered depending on if someone is logged in. */}
                  {!currentUser ? (
                    <Register appState={this.state} app={this} />
                  ) : (
                    <Space appState={this.state} />
                  )}
                </div>
              )}
            />
            {/* Admin */}
            <Route
              exact
              path="/admin"
              render={() => <Admin appState={this.state} />}
            />
            <Route
              exact
              path="/adminspace"
              render={() => <AdminSpace appState={this.state} />}
            />
            {/* MyBottles */}
            <Route
              exact
              path="/MyBottles"
              render={() => <MyBottles appState={this.state} />}
            />
            {/* ABottle */}
            <Route
              exact
              path="/ABottle"
              render={() => <ABottle appState={this.state} />}
            />
            {/* Answers */}
            <Route
              exact
              path="/answers"
              render={() => <Answers appState={this.state} />}
            />
            <Route
              exact
              path="/answers/cbt"
              render={() => <CBT appState={this.state} />}
            />
            <Route exact path="/answers/cbt/tenq" component={tenq} />
            <Route
              exact
              path="/answers/act"
              render={() => <ACT appState={this.state} />}
            />
            <Route
              exact
              path="/answers/blank"
              render={() => <Blank appState={this.state} />}
            />
            {/* personal space */}
            <Route
              exact
              path="/quest"
              render={() => <Quest appState={this.state} />}
            />
            <Route
              exact
              path="/theme"
              render={() => (
                <Theme
                  appState={this.state}
                  globalBackground={this.onChangeBackground}
                />
              )}
            />
            <Route
              exact
              path="/template"
              render={() => (
                <Template
                  appState={this.state}
                  pickTemplate={this.onChangeTemplate}
                />
              )}
            />
            <Route
              exact
              path="/Profile"
              render={() => (
                <Profile
                  appState={this.state}
                  pickTemplate={this.onChangeTemplate}
                />
              )}
            />
          </Switch>
        </BrowserRouter>
      </div>
    );
  }
}

export default App;
