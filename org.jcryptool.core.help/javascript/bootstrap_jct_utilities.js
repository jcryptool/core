// _bootstrap_JCTJS_PORT is set outside of this script.
// _bootstrap_JCTHelpsystem_PORT is set outside of this script.

// this script then constructs the necessary information and loads the necessary libraries
// from the bundle org.jcryptool.core.help
// an example url would look like this: http://127.0.0.1:<PORT>/javascript/test.txt

function JCTUrl(rel) {
	return "127.0.0.1:" + _bootstrap_JCTJS_PORT + "/" + rel	
}

function HelpsystemURL(rel) {
	return "127.0.0.1:" + _bootstrap_JCTHelpsystem_PORT + "/" + rel	
}

// this is a welcome message that displays info to prove that javascript 
// has been initialized with info for 
// - port for the help system server (eclipse-framework-controlled)
// - port for the JCTJS server (injected into the help system source code, JCrypTool-developer controlled)

alert("Hello Javascript! I'd now load: " + JCTUrl("javascript/<magic>.js") + "; the helpsystem root is: " + HelpsystemURL(""))

