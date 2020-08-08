// _bootstrap_JCTJS_PORT is set outside of this script.
// _bootstrap_JCTHelpsystem_PORT is set outside of this script.

// this script then constructs the necessary information and loads the necessary libraries
// from the bundle org.jcryptool.core.help
// an example url would look like this: http://127.0.0.1:<PORT>/javascript/test.txt

function JCTUrl(rel) {
	return "http://127.0.0.1:" + _bootstrap_JCTJS_PORT + "/" + rel	
}

function HelpsystemURL(rel) {
	return "http://127.0.0.1:" + _bootstrap_JCTHelpsystem_PORT + "/" + rel	
}

// returns the document of the help content frame
function HelpDocument() {
	return window.frames["HelpFrame"].document;	
}


// loads the MathJax library into the help content frame
// however, I have not gotten this to work.
function LoadMathjax() {
	LoadJavascript("javascript/MathJax-master/es5/tex-mml-chtml.js", "MathJax-script");
	MathJax.typeset()
// 	alert("typeset!")
}

// loads a javascript file into the help content frame
// however, I have not gotten it to work
function LoadJavascript(scriptfile, id="") {
	var script = HelpDocument().createElement('script');
	if(id != "") {
		script.id = id;
	}
// script.onload = function () {
// 	alert("loaded!")
// };
	script.src = JCTUrl(scriptfile);
// 	alert('would now load... ' + script.src)
	HelpDocument().head.appendChild(script);
// 	alert('should be loaded..?')
}

// this is a welcome message that displays info to prove that javascript 
// has been initialized with info for 
// - port for the help system server (eclipse-framework-controlled)
// - port for the JCTJS server (injected into the help system source code, JCrypTool-developer controlled)

// alert("Hello Javascript! I'd now load: " + JCTUrl("javascript/<magic>.js") + "; the helpsystem root is: " + HelpsystemURL(""))

