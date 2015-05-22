/**
 * Roundish Require utility (not an Angular module)
 */

/**
 * Roundish require constructor
 */
var roundish_require = function() {
	var require = this;

	/*
	 * Fields initialization
	 */
	
	require.context = {
		currentLoadingModule: null,
		modules: {},
		libraries: {},
	};
	
	/*
	 * Public methods
	 */
	
	/**
	 * Define a module
	 * @param {String} module Module name
	 * @param {Array} dependencies Dependencies
	 * @param {Function} onDependenciesLoaded Callback called after all dependencies are loaded
	 */
	require.module = function(module, dependencies, onDependenciesLoaded) {
		require.context.modules[module] = {
			module: module,
			dependencies: dependencies,
			onDependenciesLoaded: onDependenciesLoaded || function(){}
		};
		var baseUrl = require.getBaseUrl(module);
		for ( var i = 0; i < dependencies.length; i++ ) {
			if ( ! require.context.modules[module] ) {
				require.loadScript(dependencies[i], baseUrl);
			}
		}
	};

	/**
	 * Require dependencies
	 * @param {Array} dependencies Dependencies
	 * @param {Function} onDependenciesLoaded Callback called after all dependencies are loaded
	 */
	require.require = function(dependencies, onDependenciesLoaded) {
		require.define(require.currentLoadingModule, dependencies, onDependenciesLoaded);
	};
	
	/*
	 * Private methods
	 */
	
	/**
	 * Loads a script from its filename
	 * @param {String} script Module script filename
	 * @param {String} baseUrl Base URL
	 * @param {Function} onScriptLoaded Called when script is fully loaded  
	 */
	require.loadScript = function(script, baseUrl, onScriptLoaded) {
		
		// Check current loading module
		if ( require.currentLoadingModule ) {
			Console.error("Possible problem in Roundish Dependencies management : new module request whereas one is already loading.");
		}
		
		// Initialize callback to clear loading module
		var fullOnScriptLoaded = function() {
			require.currentLoadingModule = null;
			if ( ! onScriptLoaded || typeof(onScriptLoaded) != "Function" ) {
				onScriptLoaded();
			}
		};
		
		// Load the script
		var scriptElement = document.createElement("script")
		scriptElement.setAttribute("type","text/javascript")
		scriptElement.setAttribute("src", baseUrl + "/"  + script);
		scriptElement.onload = fullOnScriptLoaded; // Most browsers
		scriptElement.onreadystatechange = function() { // Old IE
			if ( this.readyState == "complete" ) {
				fullOnScriptLoaded();
			}
		}
		var head = document.getElementById("head");
		head.appendChild(scriptElement);
	};
	
	/**
	 * Loads a Roundish application using a script tag like this :
	 *   <script type="text/javascript" rd-app="app.js" src="roundish/roundish.js" />
	 */
	require.load = function() {
		
		// Load all application scripts
		var scripts = document.getElementsByTagName("script");
		if ( scripts.length > 0 ) {
			var i = 0;
			var nextScript = function() {
				i++;
				if ( i < scripts.length ) {
					require.loadScript(appScript, "./", nextScript);
				} else {
					
					// All are loaded, now load standalone libraries and then, start modules
					require.loadLibraries();
				}
			};
			require.loadScript(appScript, "./", nextScript);
		}
	};
	
	// TODO To be coded
	require.loadLibraries = function() {
		
	};
	
	// TODO To be coded
	require.getBaseUrl = function(module) {
		
	};
	
	// Load at startup
	this.load();
}();


/**
 * Define an external library (not using roundish-require)
 * @param library
 * @param dependencies
 */
function library(library, dependencies) {
	
}

/**
 * Define a module (using roundish-require)
 * @param {String} module Module name
 * @param {Array} dependencies Dependencies
 * @param {Function} onDependenciesLoaded Callback called after all dependencies are loaded
 */
function module(module, dependencies, onDependenciesLoaded) {
	roundish_require.module(module, dependencies, onDependenciesLoaded);
}

/**
 * Require dependencies
 * @param {Array} dependencies Dependencies
 * @param {Function} onDependenciesLoaded Callback called after all dependencies are loaded
 */
function require(dependencies, onDependenciesLoaded) {
	roundish_require.require(dependencies, onDependenciesLoaded);
)