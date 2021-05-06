import React from "react";
// import {Checkbox} from '@material-ui/core';
// import FormControlLabel from '@material-ui/core/FormControlLabel';
import { Link } from "react-router-dom";
import Button from '@material-ui/core/Button';

import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Grid from "@material-ui/core/Grid";


import "./quest.css";
// import logo1 from "./static/profile_image1.jpg"
// import logo2 from "./static/profile_image2.png"

class User extends React.Component {
    state = {
        name: this.props.name,
        logo: this.props.logo,
        ifBlock: this.props.ifBlock
    }

    onRedirectable = (e) => {
        if (this.state.ifBlock) {
            e.preventDefault()
        }
    }

    render(){

        return(
            <Grid item xs={2}>
                    <Card style={{overflow: "scroll"}}>
                    <CardContent
                    style={{
                        display: 'block',
                        width: '10vw',
                        height: '10vw'}}
                    >
                    <Typography gutterBottom variant="h5" component="h2"> {this.state.name} </Typography>
                    <Typography variant="body2" color="textSecondary" component="p">

                    <CardActionArea onClick={this.onRedirectable}>
                    <Link to="../Profile" onClick={this.onRedirectable}>
                        <img className="profile_image" src={this.state.logo} alt="profile_image"/>
                    </Link>
                    </CardActionArea>

                    <div className="linkpos">
                    <Button
                        className="myButton"
                        variant="contained"
                        disabled={this.state.ifBlock}
                        onClick={e =>  window.location.href='../MyBottles'}> Connect!
                    </Button>
                    </div>
                    </Typography>
                    </CardContent>
                </Card></Grid>
        );
    }
}

export default User