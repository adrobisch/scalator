var LoginController = [ '$scope', 'api', '$location', function(scope, api, location) {
  scope.username = "";
  scope.password = "";

  scope.login = function () {
    api.authenticate(scope.username, scope.password).then(function onSuccess() {
      location.path("/home");
    }, function onError() {
      alert("onerror");
    })
  };
  
  scope.logout = function () {
    api.logout().then(function onSuccess() {
    	location.path("/login");	
    });
  };

}];

module.exports = LoginController;