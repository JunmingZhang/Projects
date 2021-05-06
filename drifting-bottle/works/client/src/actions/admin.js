import ENV from "./../config.js";
const API_HOST = ENV.api_host;

export const getUsers = (adminComp) => {
    // the URL for the request
    const url = `${API_HOST}/api/admin`;
    console.log("adminComp", adminComp)
    // Since this is a GET request, simply call fetch on the URL
    fetch(url)
        .then(res => {
            if (res.status === 200) {
                // return a promise that resolves with the JSON body
                // console.log("res", res.json)
                return res.json();
            } else {
                alert("Could not get users");
            }
        })
        .then(json => {
            // the resolved promise with the JSON body
            console.log("json", json.users);
            let lst = []
            var i;
            for (i = 0; i < json.users.length; i++){
                lst.push({ email: json.users[i].email})
            }
            console.log("users", lst);
            adminComp.setState({ users: lst });

        })
        .catch(error => {
            console.log(error);
        });
};

export const delUser = (toMove) => {
    // the URL for the request
    const url = `${API_HOST}/api/admin`;

    // The data we are going to send in our request
    const user = {email: toMove}

    // Create our request constructor with all the parameters we need
    const request = new Request(url, {
        method: "delete",
        body: JSON.stringify(user),
        headers: {
            Accept: "application/json, text/plain, */*",
            "Content-Type": "application/json"
        }
    });

    // Send the request with fetch()
    fetch(request)
        .then(function (res) {
            // Handle response we get from the API.
            // Usually check the error codes to see what happened.
            if (res.status === 200) {
                // If student was added successfully, tell the user.
                console.log("successful delete");
            } else {
                console.log("cannot delete");
            }
        })
        .catch(error => {
            console.log(error);
        });
};