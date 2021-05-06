import React from "react"

import "./style.css"
import BlockSheet from "./BlockSheet"
import Header from "./../Header";



class Answers extends React.Component {
    state = {
        bgcolor: this.props.appState.bgcolor,
    }

    render() {
        return (
        <div className="page" style={{background: this.state.bgcolor}}>
        <Header/>
        <BlockSheet/>
  
        
        </div>
        );
    }
}

export default Answers;