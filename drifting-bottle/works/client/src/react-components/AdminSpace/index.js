import React from "react";

import "./adminSpace.css"
import ButtonList from "./ButtonList"
import Header from "./../Header";

class AdminSpace extends React.Component {
    state = {
        bgcolor: this.props.appState.bgcolor
    }

    render () {
        return (
            <div className="space" style={{background: this.state.bgcolor}}>
                <Header/>
                <div id="top">
                    <h1> Hello administrator! </h1>
                    
                </div>

                <ButtonList/>
            </div>
        );
    }
}

export default AdminSpace