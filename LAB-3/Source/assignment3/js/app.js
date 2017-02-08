var app = angular.module('myApp', []);

app.controller('PersonController', function ($scope,$http) {
    //google text to speech Api
    $scope.callme = function(){
        var text=document.getElementById('firstName').value;
        //window.alert('hello world'+text);
        console.log(text);
        var utterance = new SpeechSynthesisUtterance(text);
        window.speechSynthesis.speak(utterance);


        // code for sentiment
        var callback = $http.get("http://gateway-a.watsonplatform.net/calls/text/TextGetTextSentiment" +
            "?apikey=d0e7bf68cdda677938e6c186eaf2b755ef737cd8" +
            "&outputMode=json&text=" + text);
        callback.success(function (data) {
            if (data != null && data.docSentiment != null) {
                // window.alert(data.docSentiment.type);
                $scope.ReviewWithSentiment = {
                    "reviewText": text,
                    "sentiment": data.docSentiment.type,
                    "score": data.docSentiment.score
                };
                document.getElementById('div_ReviewList').style.display = 'block';


            }
        })




    }
    $scope.valid_user = function(email_id,password)
    {

        if ( email_id === 'abc@gmail.com'){

            if (password === '12345'){
                window.location = "/assignment3/home.html";
            }
            else {
                alert("Wrong username or Password");
            }
        }


    };


});