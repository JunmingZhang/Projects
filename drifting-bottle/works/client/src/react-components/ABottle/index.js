import React from "react";

import "./style.css";
import Header from "./../Header";
import profilepic from './../MyBottles/Bottle/FacePic/2.jpeg';
import Button from "@material-ui/core/Button";

class ABottle extends React.Component {
    // get informations in state from database
    // here I will hard code it
    state = {
        bgcolor: this.props.appState.bgcolor,
        userName: "bei",
        userEmail: "bei@mail.utoronto.ca",
        date: "2021/04/04",
        rp_user: "ShuaiTian",
        rp: "",
        replies: [{user: "bei", reply: "Getting some sunlight really wakes you up, I've been taking walks after breakfast and it really brings me energy."}, 
            {user: "ShuaiTian", reply: "Have you tried doing some exercise and get sunlights?Does it help?"},
            {user: "bei", reply: "Had a rough couple of days."}, 
            {user: "ShuaiTian", reply: "I am enough with the Get and Patch stuff."}],
        content: "Had a rough couple of days." 
    }

    handleInputChange = (event) => {
        const target = event.target
        const value = target.value
        const name = target.name
        this.setState({[name]: value})
    }
    
    // we will record this reply in dataset
    addReply = () => {
        const rpList = this.state.replies
        rpList.push({user: this.state.rp_user, reply: this.state.rp})
        this.setState({
            replies: rpList
        })
    }    

    render() {
        return (
            <div className="page" style={{background: this.state.bgcolor}}>
                <Header/>
                <div className="userInformation"> 
                    <img src={ profilepic } id="profilepic" alt=" profile pic of user"/>
                    <h2 id="username" >{ this.state.userName }</h2>
                    <span id="email">{ this.state.userEmail }</span>
                    <span id="date">Sent at { this.state.date }</span>
                </div>

                <div className="content">
                    <h2 class="black">Message from {this.state.userName}:</h2>
                    <div class = "contentText">
                        {this.state.content}
                    </div>
                    <h2 class="black">Replies:
                    <input 
                        value={this.state.rp}
                        onChange = { this.handleInputChange } 
                        type="text" name="rp" placeholder="Reply content">
                
                    </input>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={this.addReply}
                        className="user-form__submit-button"
                    >
                        Reply
                     </Button>
                    </h2>
                    { this.state.replies.map((re) => {
                        return (
                            <div class = "contentText">
                                {re.user} replies: {re.reply}
                            </div>
                        )
                    }) }   
                </div>
            </div>

        );
    }
}
export default ABottle;