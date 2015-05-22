/**
 * Roundish log module
 * Provide a message handler
 * @param window
 * @param angular
 * @param undefined
 */
(function(window, angular, undefined) {'use strict';

// Create the module
var module = angular.module('roundish-log', ['roundish-config']);

// rd-log elements list mapped by their respective log level
var scopesList = [];

// Log levels
module.constant('rdLogLevels',{
	debug: {
		name: 'debug',
		class: 'info',
		level: 1
	},
	info: {
		name: 'info',
		class: 'info',
		level: 2
	},
	sucess: {
		name: 'success',
		class: 'success',
		level: 3
	},
	warn: {
		name: 'warn',
		class: 'warning',
		level: 4
	},
	error: {
		name: 'error',
		class: 'danger',
		level: 5
	}
});

// Configuration
module.config(function($httpProvider, $compileProvider){
	
	// Log every Ajax error automatically
	$httpProvider.responseInterceptors.push(function($timeout, $q, $rdLog) {
        return function(promise) {
            return promise.then(function(successResponse) {
                return successResponse;
            }, function(errorResponse) {
            	var details = null;
            	if ( errorResponse.data && errorResponse.data.details ) {
            		details = errorResponse.data.details;
            	}
            	var message = null;
            	if ( errorResponse.data && errorResponse.data.message ) {
            		message = errorResponse.data.message;
            	} else if ( details ) {
            		message = "An error occured"; // TODO Localize it
            	}
            	if ( message ) {
            		$rdLog.error(errorResponse.data.message, errorResponse.data.details);
            	}
                return $q.reject(errorResponse);
            });
        };
    });
});

// Log service
module.provider('$rdLog', function() {
	this.$get = function(rdLogLevels) {
		return {
			log: function(level, message, details) {
		        var logObject = {
	            	level: level,
	            	message: message,
	            	details: details
	            };
	            scopesList[level.name].each(function(name, scope) {
	            	scope.addLog(logObject);
	            });
	        },
	        debug: function(message, details) {
	        	this.log(rdLogLevels.debug, message, details);
	        },
	        info: function(message, details) {
	        	this.log(rdLogLevels.info, message, details);
	        },
	        success: function(message, details) {
	        	this.log(rdLogLevels.success, message, details);
	        },
	        warn: function(message, details) {
	        	this.log(rdLogLevels.warn, message, details);
	        },
	        error: function(message, details) {
	        	this.log(rdLogLevels.error, message, details);
	        }
		};
	};
});

// Logs directive
// Auto bind log event with a controller action
module.directive('rdLogs', ['$timeout','rdLogLevels', function($timeout, rdLogLevels) {
	return {
		restrict: 'E',
	    replace: true,
	    scope: {
	    	levelName: '@rdLevel',
	    	onLog: '=rdOnlog'
	    },
	    templateUrl: 'roundish/templates/roundish-log.html', /* FIXME Relative path should be a pain... */
		link: function(scope, element, attrs) {
			
			// Scope initialization
			scope.logs = [];
			scope.closed = true;
			scope.addLog = function(log) {
				var onAfterLog = null;
				log.shown = true;
				log.showDetails = false;
				if ( scope.onLog && scope.onLog instanceof Function ) {
					onAfterLog = scope.onLog(scope,log);
	    		}
				scope.logs.push(log);
				
				// Use a timeout to ensure that "onAfterLog" is called after the bindings are applied
				$timeout(function() {
					if ( onAfterLog && onAfterLog instanceof Function ) {
						onAfterLog(scope,log);
					}
				}, 1);
	    	};
	    	scope.clear = function() {
	    		scope.logs = [];
	    	}
	    	scope.toggleDetails = function(log) {
	    		log.showDetails = !log.showDetails;
	    	}
	    	scope.hasDetails = function(log) {
	    		return log.details ? true : false;
	    	}
	    	scope.close = function(log) {
	    		log.shown = false;
	    	}
			
			// Find the directive log level
			var level = null;
			if ( scope.levelName && rdLogLevels[scope.levelName] ) {
				level = rdLogLevels[scope.levelName];
			}
			scope.level = level;
			
			// Apply the directive to the level
			for ( var i in rdLogLevels ) {
				if ( ! level || level.level <= rdLogLevels[i].level ) {
					if ( ! scopesList[rdLogLevels[i].name] ) {
						scopesList[rdLogLevels[i].name] = $();
					}
					scopesList[rdLogLevels[i].name].push(scope);
				}
			}
		}
	}
}]);

})(window, window.angular);