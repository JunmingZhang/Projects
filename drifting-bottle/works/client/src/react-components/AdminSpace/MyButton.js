import React from "react";
import Button from '@material-ui/core/Button';
import { Link } from "react-router-dom";

import "./adminSpace.css";

class MyButton extends React.Component {
    render() {
        const  { link, buttonTitle, buttonText } = this.props
        return(
            <li>
            <Link to={ link } style={{ textDecoration: 'none' }}>
                <Button variant="contained" className="myButton">
                    <h3>{buttonTitle}</h3>
                    {buttonText}
                </Button>
            </Link>
        </li>
        );
    }
}

export default MyButton