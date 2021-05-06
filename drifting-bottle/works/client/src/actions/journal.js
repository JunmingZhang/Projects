import ENV from "./../config.js";
const API_HOST = ENV.API_HOST;

export const loadAllJournals = (mybottles) => {
  const url = `${API_HOST}/api/journals`;
  fetch(url)
    .then((res) => {
      if (res.status === 200) {
        return res.text();
      } else {
        console.log("Could not get journals" + res.statusText);
      }
    })
    .then((json) => {
      // json.sort(function (a, b) {
      //   return a.createdAt > b.createdAt;
      // });
      mybottles.setState({ bottles: json });
    })
    .catch((error) => {
      console.log(error);
    });
};
