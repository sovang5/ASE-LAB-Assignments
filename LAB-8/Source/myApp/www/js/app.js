var imageApp=angular.module("starter", ["ionic","ngCordova","firebase"]);

imageApp.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    if(window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
    }
    if(window.StatusBar) {
      StatusBar.styleDefault();
    }
  });
});

imageApp.config(function($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state("firebase", {
            url: "/firebase",
            templateUrl: "templates/firebase.html",
            controller: "FirebaseController",
            cache: false
        })
     .state("register", {
            url: "/register",
            templateUrl: "templates/register.html",
            controller: "FirebaseController"
        })
      .state("home", {
            url: "/home",
            templateUrl: "templates/home.html",
            controller:"FirebaseController"
        })
        .state("secure", {
            url: "/secure",
            templateUrl: "templates/secure.html",
            controller: "SecureController"
        })
    .state("scanbarcode", {
            url: "/secure",
            templateUrl: "templates/barcodescan.html",
            controller: "FirebaseController"
        })
    .state("scanbarcoderesult", {
            url: "/secure",
            templateUrl: "templates/barcodescanresult.html",
            controller: "FirebaseController"
        })
     .state("scanner", {
            url: "/scanner",
            templateUrl: "templates/scanner.html",
            controller: "FirebaseController"
        });
    $urlRouterProvider.otherwise('/firebase');
});

imageApp.controller("FirebaseController", function($scope, $state, $firebaseAuth) {

    var fbAuth = $firebaseAuth();
    $scope.loginFailureMessage='';
    $scope.login = function(username, password) {
        fbAuth.$signInWithEmailAndPassword(username,password).then(function(authData) {
            $state.go("home");
        }).catch(function(error) {
              $scope.loginFailureMessage=error.message;
            console.error("ERROR: " + error);
        });
    }
 $scope.LoginwithFacebook = function(){
 console.log("clicked");
 $cordovaOauth.facebook("FACEBOOK_APP_ID", ["email"]).then(function(result) {
 alert("Auth Success..!!"+result);
 }, function(error) {
 alert("Auth Failed..!!"+error);
 });
 };
    $scope.register = function(username, password) {
        $scope.successmessage='';
        $scope.message='';
        fbAuth.$createUserWithEmailAndPassword(username,password).then(function(userData) {
            return fbAuth.$signInWithEmailAndPassword(username,
                password);
        }).then(function(authData) {
            $scope.successmessage='Registration Successful';
            $state.go("register");
        }).catch(function(error) {
            $scope.message=error.message;
            console.error("ERROR: " + error);
        });
    }
       
     $scope.speech = function(){
        $state.go('secure')        
    }
     
    $scope.scanBarcode = function() {
        $state.go('scanner')
    }
    $scope.scan = function() {
        $state.go('scanbarcode')
    }
$scope.scanresult = function() {
        $state.go('scanbarcoderesult')
    }
     var fbLoginSuccess = function(response) {
    if (!response.authResponse){
      fbLoginError("Cannot find the authResponse");
      return;
    }

    var authResponse = response.authResponse;

    getFacebookProfileInfo(authResponse)
    .then(function(profileInfo) {
      // For the purpose of this example I will store user data on local storage
      UserService.setUser({
        authResponse: authResponse,
				userID: profileInfo.id,
				name: profileInfo.name,
				email: profileInfo.email,
                location: profileInfo.location,
                education: profileInfo.education,
        picture : "http://graph.facebook.com/" + authResponse.userID + "/picture?type=small",
                type: 'facebook'
      });
      $ionicLoading.hide();
      $state.go('tab.dash');
    }, function(fail){
      // Fail get profile info
      console.log('profile info fail', fail);
    });
  };

  // This is the fail callback from the login method
  var fbLoginError = function(error){
    console.log('fbLoginError', error);
    $ionicLoading.hide();
  };

  // This method is to get the user profile info from the facebook api
  var getFacebookProfileInfo = function (authResponse) {
    var info = $q.defer();

    facebookConnectPlugin.api('/me?fields=email,name,location,education&access_token=' + authResponse.accessToken, null,
      function (response) {
				console.log(response);
        info.resolve(response);
      },
      function (response) {
				console.log(response);
        info.reject(response);
      }
    );
    return info.promise;
  };

  //This method is executed when the user press the "Login with facebook" button
  $scope.facebookSignIn = function() {
    facebookConnectPlugin.getLoginStatus(function(success){
      if(success.status === 'connected'){
        console.log('getLoginStatus', success.status);
    		var user = UserService.getUser('facebook');
    		if(!user.userID){
					getFacebookProfileInfo(success.authResponse)
					.then(function(profileInfo) {
						// For the purpose of this example I will store user data on local storage
						UserService.setUser({
							authResponse: success.authResponse,
							userID: profileInfo.id,
							name: profileInfo.first_name,
							email: profileInfo.email,
                            location: profileInfo.location,
                            education: profileInfo.education,
							picture : "http://graph.facebook.com/" + success.authResponse.userID + "/picture?type=small",
                            type: 'facebook'
						});

						$state.go('tab.dash');
					}, function(fail){
						// Fail get profile info
						console.log('profile info fail', fail);
					});
				}else{
					$state.go('app.home');
				}
      } else {
				console.log('getLoginStatus', success.status);
				$ionicLoading.show({
          template: 'Logging in...'
        });
        facebookConnectPlugin.login(['email', 'public_profile'], fbLoginSuccess, fbLoginError);
      }
    });
  };

    
});

//secure controller
imageApp.controller("ScannerController", function($scope, $ionicHistory, $firebaseObject, $firebaseArray, $firebaseAuth, $cordovaCamera,$state) {

 var vm = this;

        vm.scan = function(){
            $ionicPlatform.ready(function() {
                $cordovaBarcodeScanner
                    .scan()
                    .then(function(result) {
                        // Success! Barcode data is here
                        vm.scanResults = "We got a barcode\n" +
                        "Result: " + result.text + "\n" +
                        "Format: " + result.format + "\n" +
                        "Cancelled: " + result.cancelled;
                    }, function(error) {
                        // An error occurred
                        vm.scanResults = 'Error: ' + error;
                    });
            });
        };

        vm.scanResults = '';

});

imageApp.controller("SecureController", function($scope, $ionicHistory, $firebaseObject, $firebaseArray, $firebaseAuth, $cordovaCamera,$state) {

 $scope.data = {
    speechText: ''
  };
  $scope.recognizedText = '';
 
  $scope.speakText = function() {
  window.TTS.speak({
           text: $scope.data.speechText,
           locale: 'en-GB',
           rate: 1.5
       }, function () {
           // Do Something after success
       }, function (reason) {
           // Handle the error case
       });
  };
 
  $scope.record = function() {
    var recognition = new SpeechRecognition();
    recognition.onresult = function(event) {
        if (event.results.length > 0) {
            $scope.recognizedText = event.results[0][0].transcript;
            $scope.$apply()
        }
    };
    recognition.start();
  };

   

});
