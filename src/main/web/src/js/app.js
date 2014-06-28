'use strict';

var angular = require('angular');
var angularRoute = require("angular-route");

var app = angular.module('app', [
  angularRoute.name,
  require('./home/HomeModule').name,
  require('./login/LoginModule').name
]);

app.config(['$routeProvider', function($routeProvider) {

  $routeProvider.otherwise({
    redirectTo: '/home'
  });

}]);

module.exports = app;
