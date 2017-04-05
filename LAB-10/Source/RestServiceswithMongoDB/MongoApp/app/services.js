/**
 * Created by raghu on 2/29/16.
 */
var services = angular.module("mongoapp.services", []);

var url = "http://localhost:8080/MongoRestServiceExample/restService/user";

services.factory('MongoRESTService', function($http) {
    return {
        login: function(username, password, callback) {

            var res = $http.get(url+"?name="+username+"&password="+password);

            res.success(function(data, status, headers, config) {
                console.log(data);
                callback(data);
            });
            res.error(function(data, status, headers, config) {
                console.log(data);
            });        },

        register: function(user) {
            console.log(user);
            var res = $http.post(url, user);

            res.success(function(data, status, headers, config) {
                console.log(data);
            });
            res.error(function(data, status, headers, config) {
                console.log(data);
            });
        }
    }
});