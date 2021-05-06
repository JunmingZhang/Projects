const prod = {
  env: "production",
  api_host: "",
};
const dev = {
  env: "development",
  api_host: "http://localhost:3000",
  use_frontend_test_user: false,
  user: "test@user.com",
};

export default process.env.NODE_ENV === "production" ? prod : dev;
