'use strict';

var myApp = angular.module('myApp', ['ngRoute']);

myApp.config(function ($routeProvider,$qProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "list.html",
            controller: "appController"
        })
        .when("/uploadfile", {
            templateUrl: "uploadfile.html",
            controller: "uploadController"
        });
    $qProvider.errorOnUnhandledRejections(false);
});

myApp.controller('appController', function ($scope,$http,$window,fileService,$location) {
    console.log('appController starting');
    $http.get("/file-manager-services/file-manager-services/getfiles").then(function(response) {
    		console.log(response.data);
        $scope.files = response.data;
    });
    
    $scope.download=function(filename){
    		// var payload='{"fileName":"'+filename+'"}';
    		var payload='fileName='+filename;
    		console.log(payload);
    		$http.get("/file-manager-services/file-manager-services/download?"+payload).then(function(data, status, headers, config){
    			var anchor = angular.element('<a/>');
    		     anchor.attr({
    		         href: 'data:attachment/csv;charset=utf-8,' + encodeURI(data),
    		         target: '_blank',
    		         download: filename
    		     })[0].click();
    		     anchor.remove();
    		});
    }
    
    $scope.delete=function(filename){
    		var payload='fileName='+filename;
    		$http.get("/file-manager-services/file-manager-services/delete?"+payload).then(function(data){
    			console.log(data);
    		});
    		$window.location.reload();
    }	
    
    $scope.fileClicked=function(fileName){
    		fileService.setFileName(fileName);
    		$location.path("/uploadfile"); 
    }
});

myApp.controller('uploadController', function ($scope,fileUpload,fileService,$http){
	$scope.fileUploaded=false;
	var fileName=fileService.getFileName();
	if (fileName === null){
		console.log("upload file")
		$scope.title="Upload File";
		$scope.action="Upload";
		$scope.uploadFile = function(){
	        var file = $scope.myFile;
	        var fd = new FormData();
	        fd.append('firstName', $scope.firstName);
	        fd.append('lastName', $scope.lastName);
	        fd.append('uploadedFile', $scope.myFile);
	        fd.append('fileDesc', $scope.fileDesc);
	        console.log('file is ' );
	        console.dir(file);
	        var uploadUrl = "/file-manager-services/file-manager-services/upload";
	        fileUpload.uploadFileToUrl(fd, uploadUrl);
	        $scope.fileUploaded=true;
	        fileService.setFileName(null);
	    };	
	} else {
		$scope.title="Update File";
		$scope.action="Update";
		fileName = fileName.replace(/\..+$/, '');
		console.log("file selected:"+fileName);
		$http.get("/file-manager-services/file-manager-services/getfiles/"+fileName).then(function(response) {
    			console.log(response.data);
			$scope.selectedFile = response.data;
			$scope.firstName=response.data.owner.split(" ")[0];
			$scope.lastName=response.data.owner.split(" ")[1];
			$scope.fileDesc=response.data.fileDesc;
			fileService.setFileName(null);
    });
	}
	
});

myApp.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

myApp.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(fd, uploadUrl){
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .then(function(response){
        		console.log("response")
        })
    }
}]);

myApp.service('fileService', function() {
	var fileName = null;

    return {
        getFileName: function () {
            return fileName;
        },
        setFileName: function(value) {
        		fileName = value;
        }
    }  
	});