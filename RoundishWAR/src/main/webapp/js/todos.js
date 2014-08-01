(function(window, angular, undefined) {'use strict';
	
	var app = angular.module('todos', ['roundish']);
	
	// ============================== APPLICATION ==============================
	
	app.config(function ($routeProvider, $locationProvider, $rdConfigProvider) {
		$routeProvider.when('/login', {templateUrl: 'view/login.html', controller: 'LoginController', secured: false});
	    $routeProvider.when('/list', {templateUrl: 'view/list.html', controller: 'ListController', secured: true});
	    $routeProvider.when('/add', {templateUrl: 'view/add.html', controller: 'AddController', secured: true});
	    $routeProvider.when('/:id/edit', {templateUrl: 'view/add.html', controller: 'EditController', secured: true});
	    $routeProvider.when('/:id', {templateUrl: 'view/display.html', controller: 'DisplayController', secured: true});
	    $routeProvider.otherwise({redirectTo: '/list'});
	    $locationProvider.hashPrefix('!'); // Enable ajax crawling
	    $rdConfigProvider.setLoginRoute("/login");
	    $rdConfigProvider.setApplicationPath("rest");
	});
	
	// ============================== RESOURCES ==============================
	
	app.factory('Todo', ['$rdRest', function ($rdRest) {
	    return $rdRest.repository('todos');
	}]);
	
	// ============================== CONTROLLERS ==============================
	
	app.controller('MainController', ['$scope', '$location', '$rdAuth', function ($scope, $location, $rdAuth) {
		$scope.logout = function() {
			$rdAuth.logout(function(){
				$location.path('/login');
			});
		};
	    $scope.$on('$routeChangeSuccess', function(event, currRoute, prevRoute){
	    	$scope.logged = $rdAuth.user;
		});
	}]);
	
	app.controller('LoginController', ['$scope', '$location', '$rdAuth', function ($scope, $location, $rdAuth) {
	    $scope.user = '';
	    $scope.password = '';
		$scope.login = function() {
			$rdAuth.login($scope.user, $scope.password, function(user) {
	            $location.path('/list');
	        });
	    };
	}]);
	
	app.controller('ListController', ['$scope', '$location', 'Todo', function ($scope, $location, Todo) {
	    $scope.todos = Todo.query();
	    $scope.deleteTodo = function (todo) {
	        todo.$delete(function () {
	        	$scope.todos.splice($scope.todos.indexOf(todo), 1);
	        	$location.path("/list");
	        });
	    };
	    $scope.toggleTodo = function (todo) {
	        todo.$update(function () {
	        	$location.path('/list');
	        });
	    };
	    $scope.todosLeft = function () {
	        return $scope.todos.filter(function (t) {
	            return ! t.done;
	        });
	    };
	    $scope.refresh = function() {
	    	$scope.todos = Todo.query();
	    };
	}]);
	
	app.controller('AddController', ['$scope', '$location', 'Todo', function ($scope, $location, Todo) {
	    $scope.todo = new Todo();
	    $scope.saveTodo = function () {
	        Todo.save($scope.todo, function () {
	            $location.path('/list');
	        });
	    };
	}]);
	
	app.controller('EditController', ['$scope', '$location', '$routeParams', 'Todo', function ($scope, $location, $routeParams, Todo) {
	    $scope.todo = Todo.get({id: $routeParams.id});
	    $scope.saveTodo = function () {
	        Todo.update($scope.todo, function () {
	            $location.path('/list');
	        });
	    };
	}]);
	
	app.controller('DisplayController', ['$scope', '$routeParams', 'Todo', function ($scope, $routeParams, Todo) {
	    $scope.todo = Todo.get({id: $routeParams.id});
	}]);

})(window, window.angular);
