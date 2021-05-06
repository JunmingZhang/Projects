import React, { Fragment } from "react";
// Importing components
import  Header from "./../Header";
import Preference from "../Preference";
import Information from "./../Information";
import { getUser } from "../../actions/user";
import Button from "@material-ui/core/Button";
import "./styles.css"

class Profile extends React.Component {
 
  state = {
    userName: "",
    userEmail: this.props.appState.currentUser,
    jwritten:5, 
    jsent:2, 
    jcollected: 3, 
    friends:0,
    userInfo: "" 
    
  };

  // Generic handler for whenever we type in an input box.
  // We change the state for the particular property bound to the textbox from the event.
  handleInputChange = event => {
    const target = event.target;
    const value = target.value;
    const name = target.name;

    // log(name)

    // 'this' is bound to the Queue component in this arrow function.
     //  In arrow functinos, 'this' is bound to the enclosing lexical function/global scope
     //  where it is *defined*.  This is different than 'this' in normal functions,
     //  which are bound at the call-site.
    this.setState({
      [name]: value // [name] sets the object property name to the value of the `name` variable.
    });
  };

  // Each section of the Queue has its own componenet, cleaning up the
  // JSX a lot.
  render() {
    console.log("s4", this.state.s)
    return (
      <Fragment>
        <Header/>
        {/* User information with text props. To be replaced in phase 2. */}
        < Information 
          userEmail = {this.state.userEmail} 
          userName = {this.state.userName}
          jwritten ={ this.state.jwritten }
          jsent ={ this.state.jsent }
          jcollected ={ this.state.jcollected }
          friends ={ this.state.friends }
        />
        < Preference/>
       

       
      </Fragment>
    );
  }
}

export default Profile;