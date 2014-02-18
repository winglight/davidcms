/**
 * Created by chenyu2 on 13-12-24.
 */
var app = angular.module('myApp',['ngGrid', 'angularFileUpload', 'fundoo.services']);

app.directive('fancybox', function() {
    return {
        restrict: 'AC',
        link: function (scope, element, attrs) {
            $(element).fancybox({'titlePosition':'inside','type':'image'});
        }
    };
});

var cellEditableTemplate = "<input ng-class=\"'colt' + col.index\" ng-input=\"COL_FIELD\" ng-model=\"COL_FIELD\" ng-blur=\"updateEntity(col, row)\"/>";
var uploadTemplate = '<div> <input type="file" name="files[]" ng-file-select="uploadImage($files, col, row)"/>   <a class="fancybox" data-fancybox-group="gallery" fancybox ng-if="isShowImg(COL_FIELD)" ng-href="/showImage/{{COL_FIELD}}"><img ng-src="/showImage/{{COL_FIELD}}" style="width:100px;height:100px" ></a></div>';
var ctypes = ["HALL", "SERVICE", "ACTIVITY", "SUBADDRESS", "ORDERITEM"];

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

app.controller('CmsController', ['$scope', '$http', '$upload', 'createDialog', function($scope, $http, $upload, createDialogService){
    $scope.cmslist = [];
    $scope.cmstypes = [
        {id:0, name:'廳會'},
        {id:1, name:'服務'},
        {id:2, name:'推廣活動'},
        {id:3, name:'聯繫我們'},
        {id:4, name:'預訂內容'}
    ];

    $scope.cmstype = $scope.cmstypes[0].id;

    $scope.gridOptions = { data: 'cmslist',
        rowHeight: 150,
        showSelectionCheckbox:true,
        enableCellSelection: false,
        enableRowSelection: true,
        selectedItems: [],
        multiSelect:false,
        enableCellEdit: false,
        plugins:[new ngGridFlexibleHeightPlugin()],
        columnDefs: [
            {field: 'name', displayName: '名稱', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
            {field: 'description', displayName: '描述', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
            {field: 'url', displayName: 'URL', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
            {field: 'phoneNumber', displayName: '電話號碼', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
            {field: 'smsNumber', displayName: '短信號碼', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
            {field: 'smallPic', displayName: '列表圖片', enableCellEdit: false, cellTemplate: uploadTemplate, width: '**'},
            {field:'bigPic', displayName:'詳情圖片', enableCellEdit: false, cellTemplate: uploadTemplate, width: '**'}
        ] };

    $scope.isShowImg = function(url){
        return (url) && (url.length > 0);
    };

    $scope.uploadImage = function($files, column, row) {
        //$files: an array of files selected, each file has name, size, and type.
        for (var i = 0; i < $files.length; i++) {
            var file = $files[i];
            $scope.upload = $upload.upload({
                url: '/upload', //upload.php script, node.js route, or servlet url
//                method: POST,
                // headers: {'headerKey': 'headerValue'}, withCredential: true,
                data: {cid: row.entity.id, isBig: (column.field == 'bigPic')},
                file: file
                // file: $files, //upload multiple files, this feature only works in HTML5 FromData browsers
                /* set file formData name for 'Content-Desposition' header. Default: 'file' */
//                fileFormDataName: files
                /* customize how data is added to formData. See #40#issuecomment-28612000 for example */
                //formDataAppender: function(formData, key, val){}
            }).progress(function(evt) {
                    console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
                }).success(function(data, status, headers, config) {
                    // file is uploaded successfully
                    console.log(data);
                    if(column.field == 'bigPic'){
                        row.entity.bigPic = data;
                    }else{
                        row.entity.smallPic = data;
                    }
                });
            //.error(...)
            //.then(success, error, progress);
        }
    };

    // Update Entity on the server side
    $scope.updateEntity = function(column, row) {
        $scope.currentContent = row.entity;
        $scope.saveContent();
    };

    // Update Entity on the server side
    $scope.saveContent = function() {
        var content = $scope.currentContent;
        var pos = -1;
        var http_method = "PUT";
        if(!content.id){
            http_method = "POST";
            content.contentType = ctypes[$scope.cmstype];
        }else{
            pos = $scope.cmslist.indexOf(content);
        }
        $http({method: http_method, url: '/content', data:content}).success(function(data, status, headers, config) {
                if(data.flag){
                    if(!content.id){
                        $scope.cmslist.push(data.data);
                    }else{
                        $scope.cmslist[pos] = data.data;
                    }
                    $scope.alert("Successfully save content.");
                }else{
                    $scope.alert("Failed to save content.");
                }
            });
    };

    $scope.setContent = function(content){
        $scope.currentContent = content;
    };

    $scope.addContent = function(){
        $scope.currentContent = {};
        createDialogService("/assets/js/controllers/editortemplate.html",{
            id: 'editor',
            title: 'Editor Modal Dialog',
            success:{
                label: 'Save',
                fn: $scope.saveContent
            },
            scope: $scope
        });
    };

    $scope.editContent = function(){
        var items = $scope.gridOptions.selectedItems;
        if(items.length == 0){
            $scope.alert("Please select one content before click edit button.");
        }else{
            $scope.currentContent = items[0];

            createDialogService("/assets/js/controllers/editortemplate.html",{
                id: 'editor',
                title: 'Editor Modal Dialog',
                success:{
                    label: 'Save',
                    fn: $scope.saveContent
                },
                scope: $scope
            });
        }
    };

    $scope.showEditor = function(content){
        bootbox.dialog({
            message: "<span class='bigger-110'>I am a custom dialog with smaller buttons</span>",
            buttons:
            {
                "Save" :
                {
                    "label" : "<i class='icon-ok'></i> Save",
                    "className" : "btn-sm btn-success",
                    "callback": function() {
                        //Example.show("great success");
                    }
                },
                "Cancel" :
                {
                    "label" : "Danger!",
                    "className" : "btn-sm",
                    "callback": function() {
                        //Example.show("uh oh, look out!");
                    }
                }
            }
        });
    }

    $scope.deleteContent = function(){
        var items = $scope.gridOptions.selectedItems;
        if(items.length == 0){
            $scope.alert("Please select one content before click delete button.");
        }else{
            var content = items[0];
            if(content.id){
                bootbox.confirm("Are you sure to delete content?", function(result) {
                    if(result) {
                        $http.delete('/content/' + content.id).success(function(data, status, headers, config) {
                            var index = $scope.cmslist.indexOf(content);
                            $scope.gridOptions.selectItem(index, false);
                            $scope.cmslist.splice(index, 1);
                            $scope.alert("Successfully to delete content: " + content.name);
                        });
                    }
                });

            }else{
                $scope.alert("New content has not been saved yet.");
            }
        }
    };

    $scope.$watch('cmstype', function(){
            $http.get('/contents/' + $scope.cmstype).success(function(data, status, headers, config) {
                if(data.flag){
                    $scope.cmslist = data.data;

                }else{
                    $scope.alert("Couldn't get any content.");
                }
            });
    }, false);

    $scope.save = function () {
        $http.put('/company', $scope.company ).success(function(data, status, headers, config) {
            if(data.flag){
                $scope.company = data.data;
                $scope.alert("Save successfully.");
            }else{
                $scope.alert("Couldn't save company. Error:" + data);
            }
        });
    };

    $scope.alert = function(msg){
        jQuery.gritter.add({
            title: 'Message Box',
            text: msg,
            class_name: 'gritter-info gritter-center gritter-light'
        });
    }
}]);