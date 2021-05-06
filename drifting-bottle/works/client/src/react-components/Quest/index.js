import React from "react";
// import {Checkbox} from '@material-ui/core';
// import FormControlLabel from '@material-ui/core/FormControlLabel';

import "./quest.css";
import Sheet from "./Sheet"
import Header from "./../Header";
import Title from "../Title"

// 1. A user can click the profile picture of another user to see their profile.
// 2. A user can click Block so that the blocked user cannot send bottles to this user any more.
// 3. A user can click Connect and then they will be navigated to page My Bottles.
// The message or bottle from the sender will be placed in the My Bottles page of the receiver. 

class Quest extends React.Component {
    

    state = {
        bgcolor: this.props.appState.bgcolor
    }

    // toggle = () => {
    //     this.setState({ifBlock: !this.state.ifBlock})
    //     console.log(this.state.ifBlock)
    // }

    render() {
        return(
            <div className="page" style={{background: this.state.bgcolor}}>
                <Header/>

                <Title title="Please choose one to talk!" />
                    

                {/* <FormControlLabel
                    className="checkblock"
                    control={<Checkbox checked={this.state.ifBlock}
                    onChange = {this.toggle}
                    name="checked" />}
                    label="block"
                /> */}

                <Sheet/>
            </div>
        );
    }
}

export default Quest