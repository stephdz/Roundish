/**
 * Roundish Configuration module
 * @param window
 * @param angular
 * @param undefined
 */
(function(window, angular, undefined) {'use strict';

// Create the module
var module = angular.module('roundish-config', []);

// Configuration service
module.provider('$rdConfig', function() {
	var loginRoute = null;
	this.setLoginRoute = function(route) {
		loginRoute = route;
	};
	var applicationPath = null;
	this.setApplicationPath = function(path) {
		applicationPath = path;
	};
	this.$get = function() {
		return {
			loginRoute: loginRoute,
			applicationPath: applicationPath
		};
	};
});

})(window, window.angular);