import ENV from "./../config.js";

// Importing the Answers and our simple Profile Page
import Answers from "../react-components/Answers";
import CBT from "../react-components/Answers/CBT";
import ACT from "../react-components/Answers/ACT/ACT";
import Blank from "../react-components/Answers/Blank";
import Profile from "../react-components/Profile";
import Quest from "../react-components/Quest";
import Space from "../react-components/Space";
import Theme from "../react-components/Theme";
import Template from "../react-components/Template";
import Admin from "../react-components/Admin";
import MyBottles from "../react-components/MyBottles";
import ABottle from "../react-components/ABottle";
import AdminSpace from "../react-components/AdminSpace";

const API_HOST = ENV.api_host;

export const changeTheme = (user, newBgcolor) => {
  const url = `${API_HOST}/api/users/email`;

  const color = {
    bgcolor: newBgcolor,
  };
  const request = new Request(url, {
    method: "patch",
    body: JSON.stringify(color),
    headers: {
      Accept: "application/json, text/plain, */*",
      "Content-Type": "application/json",
    },
  });
  console.log(request);

  // Since this is a GET request, simply call fetch on the URL
  fetch(request)
    .then((res) => {
      if (res.status === 200) {
        // return a promise that resolves with the JSON body
        Answers.setState({ bgcolor: newBgcolor });
        CBT.setState({ bgcolor: newBgcolor });
        ACT.setState({ bgcolor: newBgcolor });
        Blank.setState({ bgcolor: newBgcolor });
        Profile.setState({ bgcolor: newBgcolor });
        Quest.setState({ bgcolor: newBgcolor });
        Theme.setState({ bgcolor: newBgcolor });
        Template.setState({ bgcolor: newBgcolor });
        if (user.isAdmin) {
          Admin.setState({ bgcolor: newBgcolor });
          AdminSpace.setState({ bgcolor: newBgcolor });
        } else {
          Space.setState({ bgcolor: newBgcolor });
        }
        MyBottles.setState({ bgcolor: newBgcolor });
        ABottle.setState({ bgcolor: newBgcolor });
        return res.json();
      } else {
        console.log(Promise.reject(res.json()));
        alert("Could not set bgcolor!");
      }
    })
    .catch((error) => {
      console.log(error);
    });
};

export const readAppTheme = (app) => {
  const url = `${API_HOST}/api/users/email`;

  fetch(url)
    .then((res) => {
      if (res.status === 200) {
        // return a promise that resolves with the JSON body
        return res.json();
      } else {
        alert("Could not get the admin");
      }
    })
    .then((json) => {
      // the resolved promise with the JSON body
      console.log(json.user);
      app.setState({ bgcolor: json.user.bgcolor });
    })
    .catch((error) => {
      console.log(error);
    });
};
