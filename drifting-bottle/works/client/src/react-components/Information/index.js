import React from "react";
import "./styles.css";
import profilepic from './profilepicpurple.png';
import Divider from "@material-ui/core/Divider";
import Typography from '@material-ui/core/Typography';
import Grid from "@material-ui/core/Grid";

/* The Information Component in Profile*/
class Information extends React.Component {
  render() {
    const { userName, userEmail,  jwritten, jsent, jcollected, friends } = this.props;

    return (
      <div className="userInformation"> 
       <Grid container direction='column' alignItems='center'>
          <Grid item ><img src={ profilepic } className="profilepic" alt=" profile pic of user"/></Grid>
          <Grid item><Typography align='center' variant="h5">{ userName }</Typography></Grid>
          <Grid item><Typography align='center' variant="subtitle1">{ userEmail }</Typography></Grid>
       </Grid>
       
        <Grid container alignItems="center" className="summary" spacing={6}>
              <Grid item lg>
                <Typography align='center' variant="subtitle1">Written</Typography>
                <Typography  align='center' variant="h4">{ jwritten }</Typography>
              </Grid>
              
              <Divider orientation="vertical" flexItem />

              <Grid item lg>
                <Typography align='center' variant="subtitle1">Friends</Typography>
                <Typography  align='center' variant="h4">{ friends }</Typography>
              </Grid>
              
              <Divider orientation="vertical" flexItem />

              <Grid item lg>
                <Typography align='center' variant="subtitle1">Sent</Typography>
                <Typography  align='center' variant="h4">{ jsent }</Typography>
              </Grid>
              
              <Divider orientation="vertical" flexItem />

              <Grid item lg>
                <Typography align='center' variant="subtitle1"> Collected</Typography>
                <Typography  align='center' variant="h4">{ jcollected }</Typography>
              </Grid>
        </Grid>
      </div>
        
    
     
    );
  }
}

export default Information;