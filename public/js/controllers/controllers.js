/**
 * Created by chenyu2 on 13-12-24.
 */
angular.module('myApp',['ngGrid']);

var cellEditableTemplate = "<input ng-class=\"'colt' + col.index\" ng-input=\"COL_FIELD\" ng-model=\"COL_FIELD\" ng-blur=\"updateEntity(col, row)\"/>";
var uploadTemplate = '<div>    <img ng-model="COL_FIELD" ng-click="uploadImage(col, row)">    <button ng-click="uploadImage(col, row)">Upload</button></div>';
var ctypes = ["HALL", "SERVICE", "ACTIVITY", "SUBADDRESS"];

function IntroController($scope, $http) {
    $scope.company = {};

    $http.get('/company').success(function(data, status, headers, config) {
        if(data.flag){
            $scope.company = data.data;
        }else{
            alert("Couldn't get any company.");
        }
    });

    $scope.save = function () {
        $http.put('/company', $scope.company ).success(function(data, status, headers, config) {
            if(data.flag){
                $scope.company = data.data;
                alert("Save successfully.");
            }else{
                alert("Couldn't save company. Error:" + data);
            }
        });
    };
}

function CmsController($scope, $http) {
    $scope.cmslist = [];
    $scope.cmstypes = [
        {id:0, name:'Hall'},
        {id:1, name:'Service'},
        {id:2, name:'Activity'},
        {id:3, name:'SubAddress'}
    ];

    $scope.cmstype = $scope.cmstypes[0].id;

    $scope.gridOptions = { data: 'cmslist',
        enableCellSelection: true,
        enableRowSelection: false,
        enableCellEdit: true,
        columnDefs: [
            {field: 'name', displayName: 'Name', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
            {field: 'description', displayName: 'description', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
            {field: 'url', displayName: 'url', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
            {field: 'phoneNumber', displayName: 'phoneNumber', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
            {field: 'smallPic', displayName: 'smallPic', enableCellEdit: false, cellTemplate: uploadTemplate},
            {field:'bigPic', displayName:'bigPic', enableCellEdit: false, cellTemplate: uploadTemplate}
        ] };

    // Update Entity on the server side
    $scope.updateEntity = function(column, row) {
        var content = row.entity;
        var http_method = "PUT";
        if(!content.id){
            http_method = "POST";
            content.contentType = ctypes[$scope.cmstype];
        }
        $http({method: http_method, url: '/content', data:content}).success(function(data, status, headers, config) {
                if(data.flag){
                    alert("Successfully save content.");
                }else{
                    alert("Failed to save content.");
                }
            });
    }

    $scope.$watch('cmstype', function(){
            $http.get('/contents/' + $scope.cmstype).success(function(data, status, headers, config) {
                if(data.flag){
                    $scope.cmslist = data.data;
                    $scope.cmslist.push({});
                }else{
                    alert("Couldn't get any content.");
                }
            });
    }, false);

    $scope.save = function () {
        $http.put('/company', $scope.company ).success(function(data, status, headers, config) {
            if(data.flag){
                $scope.company = data.data;
                alert("Save successfully.");
            }else{
                alert("Couldn't save company. Error:" + data);
            }
        });
    };
}