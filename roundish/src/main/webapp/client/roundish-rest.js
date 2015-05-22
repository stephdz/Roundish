/**
 * Roundish REST module
 * Overrides ngResource module to add :
 *  - an update method
 * @param window
 * @param angular
 * @param undefined
 */
(function(window, angular, undefined) {'use strict';

// Create the module
var module = angular.module('roundish-rest', ['roundish-config','ngResource']);

// REST service
// TODO Add a function for calling RESTMethods
module.provider('$rdRest', function() {
	this.$get = function($resource, $rdConfig) {
		if ( ! $rdConfig.applicationPath ) {
			Console.error("Application path must be configured before using $rdRest");
		} else {
			return {
				repository: function(name) {
					return $resource($rdConfig.applicationPath+'/'+name+'/:id', {id: '@id'}, {update: {method: 'PUT'} });
				}
			};
		}
	};
});

})(window, window.angular);