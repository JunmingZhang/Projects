import React from "react";

import "./title.css";

/* The Header Component */
function MakeSubtitle(props) {
  var subtitle = props.sub
  if (typeof subtitle !== 'undefined') {
    return (
        <h2>{subtitle}</h2>
    );
  } else {
    return(null);
  }
}

class Title extends React.Component {
  render() {
    const { title, subtitle } = this.props;

    console.log(typeof subtitle !== 'undefined')
    return (
      <div>
        <div id="title">
          <h1>{title}</h1>
        </div>

        <MakeSubtitle
          sub={subtitle}
        />
      </div>
    );
  }
}

export default Title;