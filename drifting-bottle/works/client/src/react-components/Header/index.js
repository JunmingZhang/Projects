import React from "react";

import "./styles.css";

class HeaderItem extends React.Component {
  render() {
    const { title, link } = this.props;
    return (
      <a href={link}>
        <h3 className="header-item">{title} </h3>
      </a>
    );
  }
}

/* The Header Component */
class Header extends React.Component {
  render() {
    return (
      <nav className="header">
        <ul className="header-bar">
          <HeaderItem title="Answers" link="../answers" />
          <HeaderItem title="Quests" link="../quest" />
          <HeaderItem title="Profile" link="../profile" />
          <HeaderItem title="My Bottles" link="../MyBottles" />
        </ul>
      </nav>
    );
  }
}

export default Header;
