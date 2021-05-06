import React from "react";
// npm install react-konva konva --save

import { Link } from "react-router-dom";
import Button from '@material-ui/core/Button';

import Header from "./../Header";
import Title from "./../Title";
import Sheet from "./Sheet"
import Board from "./Board"
import './template.css'

import { changeTemplate } from "../../actions/template"

function MakeButton(props) {
    return (
        <div id="buttonListPos">
            <ul className="button_array">
                <li>
                    <Button variant="contained" onClick={props.back} className="myButton">
                        back to default
                    </Button>
                </li>
                
                <li>
                    <Link to="../Answers" style={{ textDecoration: 'none' }}>
                        <Button variant="contained" className="myButton">
                            write a journal
                        </Button>
                    </Link>
                </li>
            </ul>
        </div>
    );
}

class Template extends React.Component {

    state = {
        user: this.props.appState.user,
        bgcolor: this.props.appState.bgcolor,
        orgcolor: "white",
        tempcolor: "white",
    }

    changeBackgroundClick = (e) => {
        const color = e.target.attrs.fill
        
        this.setState({
          tempcolor: color,  
          orgcolor: color
        })

        this.props.pickTemplate(color)
        if (this.state.user) {
            changeTemplate(color)
        }
    }

    changeBackgroundHover = (e) => {
        const color = e.target.attrs.fill
        this.setState({
          tempcolor: color
        })
    }

    changeBackgroundLeave = (e) => {
        this.setState({  
          tempcolor: this.state.orgcolor
        })
    }

    backToDefault = (e) => {
        this.setState({
            tempcolor: "white"
        })
    }

    render() {
        return(
            <div className="theme_sheet"  style={{background: this.state.bgcolor}}>
                <Header/>
                <Title title="Please choose a template"/>

                <div id="center">
                    <Sheet
                        changeBackgroundClick={this.changeBackgroundClick}
                        changeBackgroundHover={this.changeBackgroundHover}
                        changeBackgroundLeave={this.changeBackgroundLeave}
                    />
                </div>

                <MakeButton back={this.backToDefault}/>

                <Board tempcolor={this.state.tempcolor}/>
            </div>
        );
    }
}

export default Template