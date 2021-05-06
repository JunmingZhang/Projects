// Functions to help with user actions.

// environment configutations
import ENV from "./../config.js";
const API_HOST = ENV.api_host;
// console.log('Current environment:', ENV.env)

// Send a request to check if a user is logged in through the session cookie
export const checkSession = (app) => {
  const url = `${API_HOST}/users/check-session`;

  if (!ENV.use_frontend_test_user) {
    fetch(url)
      .then((res) => {
        if (res.status === 200) {
          return res.json();
        }
      })
      .then((json) => {
        if (json && json.currentUser) {
          app.setState({ currentUser: json.currentUser });
        }
      })
      .catch((error) => {
        console.log(error);
      });
  } else {
    app.setState({ user: ENV.user });
  }
};

// A functon to update the login form state
export const updateLoginForm = (loginComp, field) => {
  const value = field.value;
  const name = field.name;

  loginComp.setState({
    [name]: value,
  });
  console.log("loginComp", loginComp.state.admins);
  if ((name === "email") & loginComp.state.admins.includes(value)) {
    loginComp.setState({
      l: "./../Admin",
    });
  }
};

export const signup = (loginComp, app) => {
  // const request = new Request(`${API_HOST}/users/register`, {
  const request = new Request(`${API_HOST}/api/users`, {
    method: "post",
    body: JSON.stringify(loginComp.state),
    headers: {
      Accept: "application/json, text/plain, */*",
      "Content-Type": "application/json",
    },
  });

  // Send the request with fetch()
  fetch(request)
    .then((res) => {
      if (res.status === 200) {
        return res.json();
      }
      if (res.status === 309) {
        return res.statusText();
      }
      console.log(res);
    })
    .then((json) => {
      if (json.currentUser !== undefined) {
        app.setState({ currentUser: json.currentUser });
      }
    })
    .catch((error) => {
      console.log(error);
    });
};
// A function to send a POST request with the user to be logged in
export const login = (loginComp, app) => {
  // Create our request constructor with all the parameters we need
  const request = new Request(`${API_HOST}/users/login`, {
    method: "post",
    body: JSON.stringify(loginComp.state),
    headers: {
      Accept: "application/json, text/plain, */*",
      "Content-Type": "application/json",
    },
  });

  // Send the request with fetch()
  fetch(request)
    .then((res) => {
      if (res.status === 200) {
        return res.json();
      }
    })
    .then((json) => {
      if (json.currentUser !== undefined) {
        app.setState({ currentUser: json.currentUser });
      }
    })
    .catch((error) => {
      console.log(error);
    });
};

// A function to send a GET request to logout the current user
export const logout = (app) => {
  const url = `${API_HOST}/users/logout`;

  fetch(url)
    .then((res) => {
      app.setState({
        user: null,
        message: { type: "", body: "" },
      });
    })
    .catch((error) => {
      console.log(error);
    });
};

// export const getUser = (infoState) => {
//   // the URL for the request
//   const url = `${API_HOST}/api/getInfo`;

//   // Since this is a GET request, simply call fetch on the URL
//   fetch(url)
//       .then(res => {
//           if (res.status === 200) {
//               // return a promise that resolves with the JSON body
//               return res.json();
//           } else {
//               alert("Could not get students");
//           }
//       })
//       .then(json => {
//           // the resolved promise with the JSON body
//           infoState.setState({ userInfo: json });
//       })
//       .catch(error => {
//           console.log(error);
//       });
// };