import React from "react";
// import Title from "./../Title";
import { Link } from "react-router-dom";
import Button from "@material-ui/core/Button";

import "./styles.css";
// import bg from './static/漂流瓶.gif';

/* Component for the Home page */
class Home extends React.Component {

  // constructor (props) {
  //   super(props);

  //   this.state = {
  //     videoURL = './static/海灯节1.mp4'
  //   }
  // }

  render() {
    return (
      <div className="home__bg-image center">
        <Link className="home__button-link center" to={"./../Register"}>
               { /* Using the global state variable from App.js */}
          <Button className="home__button"> Start </Button>
        </Link>

         <Link className="home__button-link" to={"./../AdminSpace"}>
               { /* Using the global state variable from App.js */}
          <Button > I am administrator </Button>
        </Link>
      </div>

      // <div class="videocontainer">
      // <video className="fullscreenvideo" src={bg} muted autoplay="autoplay" controls="false">
      // </video>
        // <Link className="home__button-link" to={"./../Register"} style="background:url(./static/漂流瓶.gif)">
               
        //   <Button className="home__button">
          
          

        //   </Button>

        // </Link> 
        
// </div>
  

      
    );
  }
}

export default Home;
