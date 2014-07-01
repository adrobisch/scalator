var ApiService = function Api(http) {
  this.authenticate = function (login, password) {
    return http.post("/api/auth/login", {"login" :  login, "password": password});
  };

  this.userInfo = function () {
    return http.get("/api/auth/user");
  };
  
  this.logout = function () {
    return http.post("/api/auth/logout", {});
  };

};
ApiService.$inject = ["$http"];

module.exports = ApiService;