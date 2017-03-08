
app.controller('RegisterController',function($scope,$rootScope,$location,$state) {
    $scope.continueMsg=true;
    $scope.submitForm = function(isValid) {
        console.log(angular.element("#lastName").val());
        //$scope.submitted = true;
        if (isValid) {
            var firstname=angular.element("#firstName").val();
            var email=angular.element("#emailAddressEP").val();
            var password=angular.element("#password").val();
            var lastname=angular.element("#lastName").val();
            var Student_details = localStorage.Student_details?JSON.parse(localStorage.Student_details):[];
            var student = JSON.stringify
            ({
                first_Name : firstname,
                last_Name : lastname,
                email_Address : email,
                password_pass:password,

            });
            Student_details.push(student);
            localStorage.setItem("Student_details", JSON.stringify(Student_details));
            $scope.continueMsg=false;
//alert("devi");
            //var firstname=angular.element("#firstName").val();
            // var Student_details = localStorage.Student_details?JSON.parse(localStorage.Student_details):[];
            //var student = JSON.stringify
            //({
            //first_Name : firstname,

            //});
            //localStorage.setItem("firstname",JSON.stringify(firstname));
            //localStorage.setItem("Student_details", JSON.stringify(Student_details));

        }
    }


});

