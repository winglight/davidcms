/**
 * Created by chenyu2 on 13-12-24.
 */
function IntroController($scope) {
    $scope.company = {};

    $http.get('/company').success(function(data, status, headers, config) {
        if(data.flag){
            $scope.company = data.data;
        }else{
            alert("Couldn't get any company.");
        }
    });

    $scope.save = function () {

    };
}