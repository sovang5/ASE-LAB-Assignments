#!/usr/bin/env node

var pluginlist = ["cordova-plugin-compat","cordova-plugin-console","cordova-plugin-device","cordova-plugin-splashscreen","cordova-plugin-statusbar","cordova-plugin-tts","cordova-plugin-whitelist","ionic-plugin-keyboard","org.apache.cordova.speech.speechrecognition",""];

var exec = require('child_process').exec;

function puts(error, stdout, stderr) {
    console.log(stdout);
}

pluginlist.forEach(function(plug) {
    exec("cordova plugin add " + plug, puts);
});
