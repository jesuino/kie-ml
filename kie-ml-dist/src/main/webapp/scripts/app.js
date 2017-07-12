var KieMLApp = angular.module('KieMLApp', []);

var CONTAINERS_URI = "rest/server/kieml/containers";
var MODELS_URI = "/rest/server/containers/{id}/kieml";
var RUN_URI = "/rest/server/containers/{id}/kieml/{modelId}";


KieMLApp.controller('KieMLController', function($scope, $http) {
	
	var error = function(message) {
		console.log(status);
		$scope.loading = false;
		$scope.error = message;
		$('#errorAlert').show();
		$scope.result  = null;
	};
	
	$http.get(CONTAINERS_URI).success(function(kieContainers){
		$scope.kieContainers = kieContainers.result['kie-containers']['kie-container'];
	}).error(error);
	
	$scope.loadModelsForContainer = function(){
		var modelsUri = MODELS_URI.replace("{id}", $scope.selectedContainer['container-id']);
		$http.get(modelsUri).success(function(models){
			$scope.models = models.result.ModelList.model;
		}).error(error);
	}
	
	$scope.runModel = function(){
		input = {};
		input["org.fxapps.ml.api.model.Input"] = {};
		input["org.fxapps.ml.api.model.Input"].url = $scope.inputUrl;
		input["org.fxapps.ml.api.model.Input"].text = $scope.inputText;
		$scope.loading = true;
		if($scope.selectedModel.inputType === 'text' && (!$scope.inputText || $scope.inputText === '')) {
			error("Text is required!");
			return;
		}
		if($scope.selectedModel.inputType === 'binary' && (!$scope.inputUrl || $scope.inputUrl === '')) {
			error("URL is required!");
			return;
		}
		var runUri = RUN_URI.replace("{id}", $scope.selectedContainer['container-id']).replace("{modelId}", $scope.selectedModel.id);
		$http.post(runUri, input).success(function(response) {
			$scope.result = response.result.Result;
			$scope.loading = false;
			$scope.inputUrl = null;
			 $scope.inputText = null;
		}).error(error);
		
	}
	
	$('#lblLoading').each(function() {
		var elem = $(this);
		setInterval(function() {
			elem.fadeToggle(600);
		}, 400);
	});
	$scope.closeAlert = function(){
		$('#errorAlert').hide();
	}
	
});