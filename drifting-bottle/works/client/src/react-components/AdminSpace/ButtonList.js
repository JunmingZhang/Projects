import React from "react";

import MyButton from "./MyButton"

import "./adminSpace.css";

class ButtonList extends React.Component {
    render() {
        return (
            <div>
                <div id="button_list">
                    <ul>
                        <MyButton
                            link="../Profile"
                            buttonText="Visit my Profile!"
                        />

                        <MyButton
                            link="../Quest"
                            buttonText="Take a Quest!"
                        />

                        <MyButton
                            link="../Abottle"
                            buttonText="check my mailbox!"
                        />

                        <MyButton
                            link="../Theme"
                            buttonText="Choose my theme!"
                        />

                        <MyButton
                            link="../Admin"
                            buttonText="Manage users!"
                        />
                    </ul>
                </div>
            </div>
        );
    }
}

export default ButtonList