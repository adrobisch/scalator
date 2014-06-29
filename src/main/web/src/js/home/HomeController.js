var HomeController =  function($scope, api) {
  api.userInfo().then(function withUserInfo(response){
    $scope.user = response.data.displayName;
  });
};
HomeController.$inject = ['$scope', 'api'];

module.exports = HomeController;