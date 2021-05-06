import React from "react";
// npm install react-konva konva --save

import { Stage, Layer, Rect } from 'react-konva';
import Grid from '@material-ui/core/Grid';

import './template.css'

// function changeBoardColor(self) {
//     self.setState({tempcolor: self.props.tempcolor})
// }

class Board extends React.Component {
    state = {
        tempcolor: this.props.tempcolor
    }

    componentWillReceiveProps(nextProps) {
        this.setState({tempcolor: nextProps.tempcolor})
    }

    render() {
        return (
            <div id="board">
                <Grid container spacing={1} alignItems="center" justify="center" >
                    <Stage width={window.innerWidth / 1.3} height={window.innerHeight / 5}>
                        <Layer>
                            <Rect
                                className="theme"
                                width={800}
                                height={300}
                                fill={this.state.tempcolor}
                                strokeWidth={5} // border width
                                stroke="deeppink" // border color
                                
                            />
                        </Layer>
                    </Stage>
                </Grid>
            </div>
        );
    }
}

export default Board