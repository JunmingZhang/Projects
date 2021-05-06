import React from "react";
import Grid from "@material-ui/core/Grid";
import Input from "./input"
import { Link } from "react-router-dom";
import Button from "@material-ui/core/Button";
import "./style.css";

/* Component for the Student Form */
class tenq extends React.Component {

  render() {

    const questions = [
        "What's the situation? Feel free to explain it in as much detail as you'd like.",
        "What part of the situation is most troubling?",
        "What are you thinking to yourself?",
        "What thought is the most troubling?", 
        "What do you feel when you think this?",
        "When you have these feelings, what actions do you take? What actions do you avoid?",
        "Retype the summary of the situation in the following format: Trigger - Thought - Feeling - Behaviour:",
        "Consider whether the trigger truly justifies this type of thinking. Explain below.",
        "If you were to explore an alternative line of thinking, how would you do it?"
    ]
            
    return (
       [ questions.map((question)=>{
            return (
            <div>
            <div className="tenq-question">{ question }</div> 
                <Grid className="tenq-answer" container spacing={4}>
                    <Input/></Grid>
            </div>
            ) 
         }),
         <div className="tenqbuttons">
         <Link to={"../../mybottles"}>
          <Button className="tenqbutton" variant="contained"> Save </Button>
          <Button className="tenqbutton" variant="contained"> Send </Button>
        </Link>
        </div>
        ]
    )
  }
}

export default tenq;
