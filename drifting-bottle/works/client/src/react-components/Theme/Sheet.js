import React from "react";
import Grid from '@material-ui/core/Grid';

import Shape from "./Shape"
import "./theme.css"

function FormRowColor(props) {
    return (
      <React.Fragment>
        <Grid item xs={2}>
            <Shape color={props.color1} click={props.click} hover={props.hover} leave={props.leave}/>
        </Grid>
        <Grid item xs={2}>
            <Shape color={props.color2} click={props.click} hover={props.hover} leave={props.leave}/>
        </Grid>
        <Grid item xs={2}>
            <Shape color={props.color3} click={props.click} hover={props.hover} leave={props.leave}/>
        </Grid>
        <Grid item xs={2}>
            <Shape color={props.color4} click={props.click} hover={props.hover} leave={props.leave}/>
        </Grid>
      </React.Fragment>
    );
}

// function FormRowImg(props) {
//     return (
//       <React.Fragment>
//         <Grid item xs={2}>
//             <Img click={props.click} hover={props.hover} leave={props.leave}/>
//         </Grid>
//         <Grid item xs={2}>
//             <Img click={props.click} hover={props.hover} leave={props.leave}/>
//         </Grid>
//         <Grid item xs={2}>
//             <Img click={props.click} hover={props.hover} leave={props.leave}/>
//         </Grid>
//         <Grid item xs={2}>
//             <Img click={props.click} hover={props.hover} leave={props.leave}/>
//         </Grid>
//       </React.Fragment>
//     );
//   }

class Sheet extends React.Component {

    render() {
        const { changeBackgroundClick, changeBackgroundHover, changeBackgroundLeave } = this.props;

        return(
            <Grid container spacing={1}  alignItems="center" justify="center" >
                <Grid container item xs={10} spacing={2} alignItems="center" justify="center" >
                    <FormRowColor
                        color1="#ffccff"
                        color2="cornsilk"
                        color3="#ccff66"
                        color4="lightcyan"
                        click={changeBackgroundClick}
                        hover={changeBackgroundHover}
                        leave={changeBackgroundLeave}
                    />
                </Grid>
                <Grid container item xs={10} spacing={2} alignItems="center" justify="center">
                    <FormRowColor
                        color1="beige"
                        color2="bisque"
                        color3="aliceblue"
                        color4="#ffff99"
                        click={changeBackgroundClick}
                        hover={changeBackgroundHover}
                        leave={changeBackgroundLeave}
                    />
                </Grid>
                {/* <Grid container item xs={12} spacing={2} alignItems="center" justify="center">
                    <FormRowImg
                        click={changeBackgroundClick}
                        hover={changeBackgroundHover}
                        leave={changeBackgroundLeave}
                    />
                </Grid> */}
            </Grid>
        );
    }
}

export default Sheet