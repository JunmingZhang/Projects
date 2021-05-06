import React from "react"

import "./style.css"
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import { Link } from "react-router-dom";
import Grid from "@material-ui/core/Grid";

class BlockSheet extends React.Component {
    render() {
        return(
            <Grid  container spacing={5} direction='column' alignItems='center' className="blocksheet">
                <Grid item sm={6}><Card>
                <CardActionArea>
                <Link to="../answers/cbt">
                    <CardContent >
                    <Typography gutterBottom variant="h5" component="h2"> CBT </Typography>
                    <Typography variant="body2" color="textSecondary" component="p">
                    These questions are based on Cognitive Behavioral Therapy (CBT), to help you identify the relationships
                     between your thoughts (cognitions) and distressing emotions.
                    These questions help you articulate a stressful or troubling situation for reflection, 
                    elaborate on the thoughts and emotions elicited by the situation,
                    and consider alternative ways to frame the situation.
                    </Typography>
                    </CardContent>
                    </Link>
                </CardActionArea>
                </Card></Grid>

            <Grid item  sm={6}> <Card>
            <CardActionArea>
            <Link to="../answers/act">
                <CardContent >
                <Typography gutterBottom variant="h5" component="h2">
                    ACT
                </Typography>
                <Typography variant="body2" color="textSecondary" component="p">
                These questions are designed based the "Diffusion" technique in Acceptance and Commitment Therapy (ACT)
                Cognitive defusion exercises are designed to address the (sometimes overwhelming) perceived credibility
                 of painful cognitions and feelings. Taking thoughts like “I’m terrible” or “I’m useless” too literally 
                 makes it much more difficult for us to see them as what they are—to see thoughts as thoughts.
                </Typography>
                </CardContent>
                </Link>
            </CardActionArea>
            </Card></Grid>

            <Grid item  sm={6}><Card >
            <CardActionArea>
            <Link to="../answers/blank">
                <CardContent >
                
                <Typography gutterBottom variant="h5" component="h2"> Free-Writing </Typography>
                <Typography variant="body2" color="textSecondary" component="p">
                If you want to simply journal about something without answering any questions, here is a blank canvas for you.
                </Typography>
                </CardContent>
                </Link>
            </CardActionArea>
            </Card>
            </Grid>
              
            </Grid>
        );
    }
}

export default BlockSheet