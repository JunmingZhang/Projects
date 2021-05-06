import ENV from './../config.js'

// Importing the Answers and our simple Profile Page
import MyBottles from "../react-components/MyBottles";
import Template from "../react-components/Template";

const API_HOST = ENV.api_host

export const changeTemplate = (newTempColor) => {
    const url = `${API_HOST}/api/users/email`;

    const color = {
        tempcolor: newTempColor
    }
    const request = new Request(url, {
        method: "patch",
        body: JSON.stringify(color),
        headers: {
            Accept: "application/json, text/plain, */*",
            "Content-Type": "application/json"
        }
    });

    // Since this is a GET request, simply call fetch on the URL
    fetch(request)
        .then(res => {
            if (res.status === 200) {
                // return a promise that resolves with the JSON body
                MyBottles.setState({ tempcolor: newTempColor })
                Template.setState({ bgcolor: newTempColor })
                return res.json();
            } else {
                alert("Could not set bgcolor!");
            }
        })
        .catch(error => {
            console.log(error);
        });
};

export const readAppTemplate = (app) => {
    const url = `${API_HOST}/api/users/email`;

    fetch(url)
        .then(res => {
            if (res.status === 200) {
                // return a promise that resolves with the JSON body
                return res.json();
            } else {
                alert("Could not get the admin");
            }
        })
        .then(json => {
            // the resolved promise with the JSON body
            app.setState({ bgcolor: json.user.tempcolor });
        })
        .catch(error => {
            console.log(error);
        });
};
