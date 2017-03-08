app.controller('LoginController',function($scope,$rootScope,$location,$state) {
    $scope.continueMsg=true;
    var flag;
$state.go("home");
    $scope.submitForm = function(isValid) {

        //$scope.submitted = true;
        if (isValid) {
            console.log(localStorage.getItem("Student_details"));
            var details=localStorage.getItem("Student_details");
            details=JSON.parse(details);
            console.log(details);
            for(var i =0;i<details.length;i++) {
                var y = JSON.parse(details[i]);
                
                console.log(y.email_Address);
                if (y.email_Address === "abc@gmail.com" && y.password_pass ==="12345") {
                    flag = true;
                    //$state.go("home");

                }
                else{
                    flag=false;
                }
            }
            if(flag==true)
            {
                $state.go("home");
            }
            else
            {
                $scope.continueMsg=false;
            }


        }

    };

});