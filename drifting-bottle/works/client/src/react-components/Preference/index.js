import React from "react";
import "./styles.css";

import Checkbox from "@material-ui/core/Checkbox";
import TableRow from "@material-ui/core/TableRow";

/* Component for the List of Students */
class Preference extends React.Component {
  state = {
    privateJournals: false,
    anonymous: true,
    commentable: true,
    visible: true,
  };

  // a function to toggle the .
  toggle = (oldState) => {
    this.setState({
      oldState: !oldState,
    });
  };

  render() {
    return (
      <div className="preferenceTable">
        <TableRow id="preferenceHeader">
          <h2>Preference</h2>
        </TableRow>
        <TableRow id="preferenceBox">
          <Checkbox
            checked={this.state.privateJournals}
            onChange={this.toggle}
          />
          <span>Set all my journals to private</span>
        </TableRow>

        <TableRow id="preferenceBox">
          <Checkbox checked={this.state.anonymous} onChange={this.toggle} />
          <span>Keep myself anonymous for all my bottles</span>
        </TableRow>

        <TableRow id="preferenceBox">
          <Checkbox checked={this.state.commentable} onChange={this.toggle} />
          <span>Enable comments on all my sent bottles</span>
        </TableRow>

        <TableRow id="preferenceBox">
          <Checkbox checked={this.state.visible} onChange={this.toggle} />
          <span>My profile is not visible to anyone else</span>
        </TableRow>
      </div>
    );
  }
}

export default Preference;
