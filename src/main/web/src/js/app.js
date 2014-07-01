'use strict';
var fs = require("fs");

var angular = require('angular');
var angularRoute = require("angular-route");

var authInterceptor = require('./common/AuthInterceptor');
var apiService = require('./common/ApiService');

var app = angular.module('app', [
  angularRoute.name,
  require('./login/LoginModule').name,
  require('./home/HomeModule').name,
  require('./navbar/NavbarModule').name
]);

app.factory('authInterceptor', authInterceptor);
app.config(['$httpProvider', function ($httpProvider) {
  $httpProvider.interceptors.push('authInterceptor');
}]);

app.service('api', apiService);

app.config(['$routeProvider', function($routeProvider) {
  $routeProvider.otherwise({
    redirectTo: '/home'
  });
}]);

module.exports = app;
