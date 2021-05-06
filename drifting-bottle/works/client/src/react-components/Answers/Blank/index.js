import React from "react";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";

import "./styles.css";

class Blank extends React.Component {
    state = {
      bgcolor: this.props.appState.bgcolor,
    }

    render() {
        return(
          <div className="page" style={{background: this.state.bgcolor}}>
            <Grid item className="blank"
            >
            <TextField             
              margin="normal"
              variant="outlined"
              multiline
              fullWidth='true'
              label="Write here"
            />
          </Grid>
          </div>
        );
    }
}

export default Blank