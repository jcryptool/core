package org.jcryptool.core.help;

import java.util.Optional;

import org.eclipse.core.runtime.Platform;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.ResourceService;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.PathResource;
import org.eclipse.jetty.util.resource.Resource;


public class JCTJS_Server {
	
	private static Optional<JCTJS_Server> instance = Optional.empty();
	
	public static JCTJS_Server getInstance() {
		if(instance.isEmpty()) {
			instance = Optional.of(startServer());
			return getInstance();
		}
		return instance.get();
	}
	
	private static JCTJS_Server startServer() {
		int open_port = get_open_port();

		return new JCTJS_Server(open_port);
	}

	private static int get_open_port() {
		// TODO get open port
		return 31339;
	}

	private int port;
	private Server server;
	public int getPort() {
		return this.port;
	}

	public static URL getRootURL() {
		return Platform.getBundle("org.jcryptool.core.help").getEntry(".");
	}
	
	public JCTJS_Server(int open_port) {
		this.port = open_port; //TODO: this must be dynamical
		URL rootResource = getRootURL();
		System.out.println(rootResource);
		Resource baseResource = Resource.newResource(rootResource);
// 		Resource baseResource = Resource.newClassPathResource("."); //TODO
		Server server = new Server(port);
//	        Path userDir = Paths.get(System.getProperty("user.dir"));
//	        PathResource pathResource = new PathResource(userDir);

		ResourceService resourceService = new ResourceService();
		// Create the ResourceHandler. It is the object that will actually handle the request for a given file. It is
		// a Jetty Handler object so it is suitable for chaining with other handlers as you will see in other examples.
		ResourceHandler resourceHandler = new ResourceHandler(resourceService);

		
//		System.out.println(JCTJS_Server.class.getResource("."));
//		System.out.println(getRootURL());
//		System.out.println(JCTJS_Server.class.getResource("./test.txt"));
//		System.out.println(JCTJS_Server.class.getResource("./javascript/test.txt"));
// 		InputStreamReader testreader = new InputStreamReader(testResource);
// 		BufferedReader bufferedReader = new BufferedReader(testreader);
// 		String line;
// 		try {
//			while((line = bufferedReader.readLine()) != null) {
//				System.out.println("test.txt:: " + line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		// Configure the ResourceHandler. Setting the resource base indicates where the files should be served out of.
		// In this example it is the current directory but it can be configured to anything that the jvm has access to.
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[]{"index.html"});
		resourceHandler.setBaseResource(baseResource);

		// Add the ResourceHandler to the server.
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[]{resourceHandler, new DefaultHandler()});
		server.setHandler(handlers);

		this.server = server;
	}
	
	public void start() throws Exception {
		this.server.start();
	}
	
}
