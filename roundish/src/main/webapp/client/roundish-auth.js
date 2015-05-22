/**
 * Roundish Authentication module
 * @param window
 * @param angular
 * @param undefined
 */
(function(window, angular, undefined) {'use strict';

// Create the module
var module = angular.module('roundish-auth', ['roundish-config','roundish-log','ngRoute']);

// Auth service
module.provider('$rdAuth', function() {
	this.$get = function($http, $location, $route, $rdConfig, $rdLog) {
		if ( ! $rdConfig.loginRoute ) {
			Console.error("Login route must be configured before using $rdAuth");
		} else if ( ! $rdConfig.applicationPath ) {
			Console.error("Application path must be configured before using $rdAuth");
		} else {
			return {
				user: null,
				init: function() {
					var $rdAuth = this;
					$rdAuth.initializing = true;
					$http
						.get($rdConfig.applicationPath+'/auth')
						.success(function(data, status, headers, config) {
							$rdAuth.initializing = false;
							if ( ! data ) {
								$rdAuth.check($route.current);
							} else {
								$rdAuth.user = data;
							}
						})
						.error(function (data, status, headers, config) {
							$rdAuth.initializing = false;
							$location.path($rdConfig.loginRoute);
							if ( data && data.message ) {
								$rdLog.error(data.message, data.details);
							} else {
								$rdLog.error('Unknown error while authenticating'); /* TODO Localize it */
							}
						});
				},
				check: function(route) {
					var $rdAuth = this;
					if ( !$rdAuth.initializing && route.$$route && route.$$route.secured && ! $rdAuth.user ) {
						$location.path($rdConfig.loginRoute);
					}
				},
				login: function(login, password, success, error) {
					var $rdAuth = this;
					var loginInfos = {
						login: login,
						password: password
					};
					$http
						.post($rdConfig.applicationPath+'/auth', loginInfos)
						.success(function(data, status, headers, config) {
							$rdAuth.user = data;
							if ( success && success instanceof Function ) {
								success(data);
							}
						})
						.error(function (data, status, headers, config) {
							if ( error && error instanceof Function ) {
								error(data);
							} else {
								if ( data && data.message ) {
									$rdLog.error(data.message, data.details);
								} else {
									$rdLog.error('Unknown error while authenticating (maybe the server is down...)'); /* TODO Localize it */
								}
							}
						});
				},
				logout: function(success, error) {
					var $rdAuth = this;
					$http
						.delete($rdConfig.applicationPath+'/auth')
						.success(function(data, status, headers, config) {
							$rdAuth.user = null;
							if ( success && success instanceof Function ) {
								success(data);
							}
						})
						.error(function (data, status, headers, config) {
							if ( error && error instanceof Function ) {
								error(data);
							} else {
								if ( data && data.message ) {
									$rdLog.error(data.message, data.details);
								} else {
									$rdLog.error('Unknown error while login out (maybe the server is down...)'); /* TODO Localize it */
								}
							}
						});
				}
			};
		}
	};
});

// Check secured routes on route change
module.run(function($rootScope, $rdAuth) {
	$rdAuth.init();
	$rootScope.$on('$routeChangeStart', function(event, currRoute, prevRoute){
		$rdAuth.check(currRoute);
	});
});

})(window, window.angular);