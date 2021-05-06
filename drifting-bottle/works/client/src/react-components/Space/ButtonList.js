import React from "react";

import MyButton from "./MyButton"

import "./space.css";

class ButtonList extends React.Component {
    render() {
        console.log("s3", this.props.appState)
        return (
            <div>
                <div id="button_list">
                    <ul>
                        <MyButton
                            link="../Profile"
                            buttonText="Visit my Profile!"
                            appState={this.props.appState}
                        />

                        <MyButton
                            link="../Quest"
                            buttonText="Take a Quest!"
                            appState={this.props.appState}
                        />

                        <MyButton
                            link="../Abottle"
                            buttonText="check my mailbox!"
                            appState={this.props.appState}
                        />

                        <MyButton
                            link="../Theme"
                            buttonText="Choose my theme!"
                            appState={this.props.appState}
                        />
                    </ul>
                </div>
            </div>
        );
    }
}

export default ButtonList