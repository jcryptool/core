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
function LoadJavascript(scriptfile, id="", onload="") {
	// var script = HelpDocument().createElement('script'); // TODO: doesnt work because of HelpDocument()
	script = document.createElement('script')
	if(id != "") {
		script.id = id;
	}
	script.src = JCTUrl(scriptfile);
	if(onload != "") {
		script.onload = onload;
	}
	// HelpDocument().head.appendChild(script); // TODO: doesnt work because of HelpDocument()
	document.head.appendChild(script);
}

// optional args: contentselector (defaults to "body", but could be e.g. ".content") if there is a <div id="content">parse_only_here_for_TOC_headings</div>
function TOC_generate_default(headingSelector, ...restargs) {
	var contentselector = "body"
	if(restargs.length > 0) {
		contentselector = restargs[0]	
	}
	// required libraries
	LoadJavascript("javascript/jquery.js")
	LoadJavascript("javascript/tocbot-4.11.2/dist/tocbot.min.js", "tocbotscript", function() {
		$(document).ready(function() {
			tocbot.init({
				tocSelector: '.TOC',			 // Where to render the table of contents.
				contentSelector: contentselector,   // Where to grab the headings to build the table of contents.
				headingSelector: headingSelector, // Which headings to grab inside of the contentSelector element.
				hasInnerContainers: true,      // For headings inside relative or absolute positioned containers within content.
				listClass: 'toc-list',
				listItemClass: 'toc-list-item',
				isCollapsedClass: 'is-collapsed',
				collapsibleClass: 'is-collapsible',
				collapseDepth: 0,
			});
		})
	})

}

// this is a welcome message that displays info to prove that javascript 
// has been initialized with info for 
// - port for the help system server (eclipse-framework-controlled)
// - port for the JCTJS server (injected into the help system source code, JCrypTool-developer controlled)

// alert("Hello Javascript! I'd now load: " + JCTUrl("javascript/<magic>.js") + "; the helpsystem root is: " + HelpsystemURL(""))

