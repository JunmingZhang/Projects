import React from "react"
import { Link } from "react-router-dom";
import Button from "@material-ui/core/Button";
import "./style.css"


class Intro extends React.Component {
    state = {
        bgcolor: this.props.appState.bgcolor,
    }

    render() {
        return(
            <div className="page" style={{background: this.state.bgcolor}}>
            <div className="intro">
            <p>Think of a current, past or future situation where you feel stressed or have a negative emotion, 
            which you can try to reflect on as you go through this activity. </p>
        
            <p>Troubling thoughts are often due to common patterns in thinking that reinforce negative emotions 
            and thoughts beyond what is actually true.  It can be helpful to identify whether your thoughts fit into 
            any of these patterns.  Let's take a look at the type of thinking you're doing right now. </p>

            <h3>Thoughts about yourself</h3>
            <h4> Personalizing the situation</h4> 
            "If something bad happens, it means
             there’s something bad about me." Personalizing is thinking a bad outcome is the result of something bad in you. 
             You take a judgment of something and internalize it as a judgment of yourself.

             <h3>Thoughts about the past/future</h3>
             <h4>Predicting a worst case scenario</h4>
            “This is going to turn out badly.” Worst case scenarios 
             are demoralizing. You’re tempted to believe that the worst is going to happen, which causes you to behave as 
             if it has *already* happened. Predictions can lead to despair and anxiety.

             <h4>Thinking in black and white</h4> 
             “If it’s not perfect, it’s a failure.” Black and white thinking 
             is focusing on two outcomes of a situation. You’re tempted to think there are basically two possibilities:
            all/nothing, all good/all bad, all your fault/all their fault, total success/total failure.

             <h4>Making a broad conclusion</h4> 
             “It happened this time, so it’s true all the time.” Making broad conclusions 
             is using a specific event to show that your thought is true for all events. You take the fact that something 
             happened once, as evidence that it will happen all the time. 

             <h4>Emotional reasoning</h4>
            "I feel like this is bad." Emotional reasoning is using a gut feeling as evidence
            that something is wrong. You’re tempted to believe that if you feel that way, it must be true. Emotional 
            reasoning can lead to feeling discouraged and demoralized. 

            <h4>Magnification/Minimization</h4> 
            "The flaws are so big that the positives don’t matter." Magnifying 
            negatives while minimizing positives can lead to feeling overwhelmed, anxious, and sad. You focus on flaws
            and fears while denying positives, strengths, and accomplishments.   

            <h3>Thoughts about others</h3>
            <h4>Blaming</h4>
             "If something bad happens, there is someone or something to blame." 
            Blaming is faulting someone or something for an upsetting event. You’re tempted to think that control is in the hands 
            of another person, or that something took away your control. 
                
            <h4>Mindreading</h4>
            “This person thinks I’m awkward. They don’t appreciate me.”
             Mindreading is thinking that you know what someone else is thinking. You’re tempted 
             to believe that you know the private thoughts that explain people’s behaviors. Mindreading can lead to anxiety and anger.
             <Link to={"./cbt/tenq"}>
            <Button className="tenqbutton" variant="contained"> Next </Button>
           
            </Link>
           
               
        </div>
        </div>
            
        );
    }
}

export default Intro
