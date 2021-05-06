import React from "react";

import "./space.css"
import ButtonList from "./ButtonList"

class Space extends React.Component {
    state = {
        s: this.props.appState,
        bgcolor: this.props.appState.bgcolor
    }

    render () {
        console.log("s2", this.props.appState.currentUser)
        return (
            <div className="space" style={{background: this.state.bgcolor}}>

                <div id="top">
                    <h1> Good to see you, {this.props.appState.currentUser} </h1>
                    <h1> What do you want to do for next? </h1>
                </div>

                <ButtonList appState={this.props.appState}/>
            </div>
        );
    }
}

export default Space