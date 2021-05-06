import React from "react";
import { Stage, Layer, Rect } from 'react-konva';

import "./template.css"

class Shape extends React.Component {
    state = {
        color: this.props.color,
        blur: 5
    }

    onHover = (e) => {
        this.props.hover(e)
        this.setState({blur: 20})
    }

    onLeave = (e) => {
        this.props.leave(e)
        this.setState({blur: 5})
    }

    render() {
        console.log(this.props.page)
        return (
            <Stage width={window.innerWidth / 1.3} height={window.innerHeight / 5}>
                <Layer>
                    <Rect
                        className="theme"
                        width={100}
                        height={100}
                        fill={this.state.color}
                        shadowBlur={this.state.blur}
                        onClick={this.props.click}
                        onMouseEnter={this.onHover}
                        onMouseLeave={this.onLeave}
                    />
                </Layer>
            </Stage>
        );
    }
}

export default Shape