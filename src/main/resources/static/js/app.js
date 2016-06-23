(function(){
    var app = angular.module('heatMapApp', ['nya.bootstrap.select']);

    app.service('fileUpload', ['$http', function ($http) {
        this.uploadFileToUrl = function (file, uploadUrl, form) {
            var fd = new FormData();
            if(file)
                fd.append('file', file);
            $http.post(uploadUrl, fd, {
                transformRequest: angular.identity
                , headers: {
                    'Content-Type': undefined
                }
            }).then(function successCallback(response) {
                console.log(response);
            }, function errorCallback(response) {
                console.log(response);
            });

        }
    }]);

    app.directive('fileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            scope: false
            , link: function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;

                element.bind('change', function () {
                    scope.$apply(function () {
                        modelSetter(scope.$parent, element[0].files[0]);
                    });
                });
            }
        };
    }]);

    app.controller('HeatMapController', ['$scope', 'fileUpload', '$http', function ($scope, fileUpload, $http) {
        $scope.availableCategories = [];
        $scope.chosenCategories = [];
        $scope.filterUpdateProgress = false;
        $scope.map = {};
        $scope.heatmap = {};
        $scope.testData = { data : []};
        
        $scope.uploadFile = function () {
            var file = $scope.myFile;
            var uploadUrl = "/upload-data";
            fileUpload.uploadFileToUrl(file, uploadUrl, null);
        };

        $scope.updateCategories = function(){
            var updateUrl = "/available-categories";
            $scope.filterUpdateProgress = true;
            $http({
                method: 'GET',
                url : updateUrl
            }).then(function successfulCallback(response) {
                $scope.availableCategories = response.data;
                $scope.filterUpdateProgress = false;
            }, function errorCallback(response){
               alert(response); 
            });
        };

        $scope.applyFilters = function(){
            var crimeDataUrl = '/data';
            var params = {'categoriesId' : $scope.chosenCategories};
            $http({
                method: 'GET',
                url : crimeDataUrl,
                params : params
            }).then(function successfulCallback(response) {
                $scope.testData.data = response.data;
                setMapData($scope.testData);
            }, function errorCallback(response) {
                alert(response);
            });
        };

        angular.element(document).ready(function () {
            if(window.location.pathname == "/" )
                $scope.applyFilters();
        });
    }]);
    
})();


