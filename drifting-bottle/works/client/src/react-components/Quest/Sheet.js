import React from "react";
// import {Checkbox} from '@material-ui/core';
// import FormControlLabel from '@material-ui/core/FormControlLabel';
import Grid from "@material-ui/core/Grid";

import "./quest.css";
import User from "./User"

// import logo1 from "./static/profile_image1.jpg"
import logo2 from "./static/profile_image2.png"

class Sheet extends React.Component {
    render() {
        return(
            <Grid  container spacing={10} justify="center" alignItems='center'>
                <Grid container item xs={10} spacing={8} alignItems="center" justify="center">

                <User
                name="Lowen Yang"
                ifBlock={false}
                logo={logo2}
                />

                <User
                name="PangBei"
                ifBlock={true}
                logo={logo2}
                />

                <User
                name="ShuaiTian"
                ifBlock={true}
                logo={logo2}
                />

                </Grid>

                <Grid container item xs={10} spacing={8} alignItems="center" justify="center">

                <User
                name="ChenChen"
                ifBlock={true}
                logo={logo2}
                />

                <User
                name="JunMing"
                ifBlock={true}
                logo={logo2}
                />

                <User
                name="HunMing"
                ifBlock={true}
                logo={logo2}
                />

                </Grid>
            </Grid>
        );
    }
}

export default Sheet