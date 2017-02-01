var app = angular.module('GoogleDirection', ['ngSanitize']);

app.controller('googlemapoutput', function ($scope, $http) {
   
});
angular.module('GoogleDirection', [])
    .controller('googlemapoutput', function ($scope, $http, $location) {

        var map;
        var mapOptions;
        var directionsDisplay = new google.maps.DirectionsRenderer({
            draggable: true
        });
        var directionsService = new google.maps.DirectionsService();

        $scope.initialize = function () {
            var pos = new google.maps.LatLng(0, 0);
            var mapOptions = {
                zoom: 3,
                center: pos
            };

            map = new google.maps.Map(document.getElementById('map-canvas'),
                mapOptions);
        };
        $scope.calcRoute = function () {
            var end = document.getElementById('endlocation').value;
            var start = document.getElementById('startlocation').value;
            var originLatLong = [];
            var desLatLong = [];
            var request = {
                origin: start,
                destination: end,
                travelMode: google.maps.TravelMode.DRIVING
            };

            directionsService.route(request, function (response, status) {
                if (status == google.maps.DirectionsStatus.OK) {
                    directionsDisplay.setMap(map);
                    directionsDisplay.setDirections(response);
                    console.log(status);
                }

            });
            //after this write any thing

            //Origin

            var geo = new google.maps.Geocoder;
            geocoder = new google.maps.Geocoder();
            var startaddress = start;
            geocoder.geocode({ 'address': startaddress }, function (results, status) {
                if (status == google.maps.GeocoderStatus.OK) {

                    originLatLong[0] = results[0].geometry.location.lat();
                    originLatLong[1] = results[0].geometry.location.lng();


                    //LatLong Code

                    var latlng = new google.maps.LatLng(originLatLong[0], originLatLong[1]);

                    geocoder.geocode({ 'latLng': latlng }, function (results, status) {
                        if (status == google.maps.GeocoderStatus.OK) {
                            console.log(results)
                            if (results[1]) {

                                //find country name
                                for (var i = 0; i < results[0].address_components.length; i++) {
                                    for (var b = 0; b < results[0].address_components[i].types.length; b++) {

                                        //there are different types that might hold a city admin_area_lvl_1 usually does in come cases looking for sublocality type will be more appropriate
                                        if (results[0].address_components[i].types[b] == "administrative_area_level_1") {
                                            //this is the object you are looking for
                                            city = results[0].address_components[i];
                                            break;
                                        }
                                    }
                                }



                                $http.get('https://api.wunderground.com/api/36b799dc821d5836/conditions/q/'+city.short_name+'/'+start+'.json').success(function(data){
                                    // $http.get(wthrURL).success(function (data) {
                                    console.log(data);
                                    temp = data.current_observation.temp_f;
                                    icon = data.current_observation.icon_url;
                                    weather = data.current_observation.weather;
                                    console.log(temp);
                                    $scope.currentweather = {
                                        html: "Currently " + temp + " &deg; F and " + weather + ""
                                    }
                                    $scope.currentIcon = {
                                        html: "<img src='" + icon + "'/>"
                                    }

                                    console.log($scope.currentIcon);
                                })



                                //$scope.cityName = city.short_name;

                            } else {

                                alert("No results found");
                            }
                        } else {

                            alert("Geocoder failed due to: " + status);
                        }
                    });



                }

                else {
                    alert("Geocode was not successful for the following reason: " + status);
                }
            });

            //after this write any thing

            //Destination


            var endaddress = end;
            geocoder.geocode({ 'address': endaddress }, function (results, status) {
                if (status == google.maps.GeocoderStatus.OK) {

                    desLatLong[0] = results[0].geometry.location.lat();
                    desLatLong[1] = results[0].geometry.location.lng();


                    //LatLong Code

                    var dstlatlng = new google.maps.LatLng(desLatLong[0], desLatLong[1]);

                    geocoder.geocode({ 'latLng': dstlatlng }, function (results, status) {
                        if (status == google.maps.GeocoderStatus.OK) {
                            console.log(results)
                            if (results[1]) {

                                //find country name
                                for (var i = 0; i < results[0].address_components.length; i++) {
                                    for (var b = 0; b < results[0].address_components[i].types.length; b++) {

                                        //there are different types that might hold a city admin_area_lvl_1 usually does in come cases looking for sublocality type will be more appropriate
                                        if (results[0].address_components[i].types[b] == "administrative_area_level_1") {
                                            //this is the object you are looking for
                                            city = results[0].address_components[i];
                                            break;
                                        }
                                    }
                                }





                                $http.get('https://api.wunderground.com/api/36b799dc821d5836/conditions/q/' + city.short_name + '/' + end +'.json').success(function(data){
                                    //alert('Destination State' + city.short_name);
                                    console.log(data);
                                    temp = data.current_observation.temp_f;
                                    icon = data.current_observation.icon_url;
                                    weather = data.current_observation.weather;
                                    console.log(temp);
                                    $scope.descurrentweather = {
                                        html: "Currently " + temp + " &deg; F and " + weather + ""
                                    }
                                    $scope.descurrentIcon = {
                                        html: "<img src='" + icon + "'/>"
                                    }

                                })



                                //$scope.cityName = city.short_name;

                            } else {

                                alert("No results found");
                            }
                        } else {

                            alert("Geocoder failed due to: " + status);
                        }
                    });



                }

                else {
                    alert("Geocode was not successful for the following reason: " + status);
                }
            });



            /////////////////////////////////////////////////////////////







//bofore this write any thing


        };

        $scope.registerUser = function(name,password,cnf_password,emailid)
        {

            if (password === cnf_password){
                localStorage.setItem('name',name);
                localStorage.setItem('password',password);
                localStorage.setItem('emailid',emailid);
                alert('succesfully registered, Press Ok');
                window.location = "/First_proj/index.html";
            }
            else {
                alert("Password Mismatch");
            }

        };

        $scope.valid_user = function(emailid,password)
        {

            if ( localStorage.getItem('emailid') === emailid){

                if ( localStorage.getItem('password') === password){
                    window.location = "/First_proj/home.html";
                }
                else {
                    alert("Wrong username or Password");
                }
            }


        };
        $scope.sendto = function()
        {

            window.location = "/First_proj/register.html";

        };

        google.maps.event.addDomListener(window, 'load', $scope.initialize);

    });