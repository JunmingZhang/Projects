import React from "react";
import Grid from "@material-ui/core/Grid";
import Input from "../CBT/input";
import { Link } from "react-router-dom";
import Button from "@material-ui/core/Button";
import { questions } from "./actquestions";
import "./style.css";

class ACT extends React.Component {
  render() {
    return [
      questions.map((question) => {
        return (
          <div>
            <div className="act-question">{question}</div>
            <Grid className="act-answer" container spacing={4}>
              <Input />
            </Grid>
          </div>
        );
      }),
      <Link to={"../../mybottles"}>
        <Button className="actbutton" variant="contained">
          Save
        </Button>
        <Button className="actbutton" variant="contained">
          Send
        </Button>
      </Link>,
    ];
  }
}

export default ACT;
