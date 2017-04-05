'use strict';

// Declare app level module which depends on views, and components
var app  = angular.module('mongoapp', [
    'ngRoute'
    ,'mongoapp.services'
])
app.config(['$routeProvider', function ($routeProvider) {

    $routeProvider.when('/login', {
        templateUrl: 'login.html',
        controller: 'loginCntrl'
    }).when('/home', {
        templateUrl: 'home.html',
        controller: 'homecntrl'
    }).
        when('/register',{
        templateUrl : 'register.html',
        controller : 'registerCntrl'
    }).
    otherwise({
        redirectTo: '/register'
    });
}])
    var userData;
app.controller('loginCntrl',['$scope','$location','$http', function ($scope, $location,$http) {
        $scope.login = function (username, password) {
            console.log(username);
            var url = "http://localhost:8080/MongoRestServiceExample/restService/user";
            //Read the database to check for the user.
            var res = $http.get(url+"?name="+username+"&password="+password);

            res.success(function(result, status, headers, config) {
                if (result.length = 1) {
                    userData = result[0];//contains the details of the logged in user.
                    $location.path('/home');
                } else {
                    alert('Sorry, check your credentials.')
                }
            });
            res.error(function(result, status, headers, config) {
                console.log(result);
            });

        }
    }])
    .controller('registerCntrl',['$scope','$location','$http', function ($scope,$location,$http) {
        var url = "http://localhost:8080/MongoRestServiceExample/restService/user";
        $scope.createAccount = function (data) {
            //Post the data to the database to create user.
            var res = $http.post(url, data);

            res.success(function(result, status, headers, config) {
                $location.path('/login');
                console.log(result);
            });
            res.error(function(data, status, headers, config) {
                console.log(data);
                alert("There was some issue while registering. Please try again.")
            });


        }
    }])
    .controller('homecntrl', function ($scope) {
        $scope.user = {};
        //Reading the logged in user details
        if(userData!=null)
        {
            $scope.user.name = userData.name.toString().toUpperCase();
        }

    });
