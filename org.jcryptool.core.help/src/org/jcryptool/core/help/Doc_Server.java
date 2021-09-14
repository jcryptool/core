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
import java.net.ServerSocket;
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


public class Doc_Server {
	
	private static Optional<Doc_Server> instance = Optional.empty();
	
	public static Doc_Server getInstance() {
		if(instance.isEmpty()) {
			instance = Optional.of(createAndTryStartServer());
			return getInstance();
		}
		return instance.get();
	}
	
	/**
	 * creates a server instance and tries to start it. Returns the server instance.
	 * users should check if isServing is true TODO: implement (not implemented yet)
	 * 
	 * @return
	 */
	private static Doc_Server createAndTryStartServer() {
		int open_port = get_open_port();
		Doc_Server server = new Doc_Server(open_port);
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return server;
	}

	private static int get_open_port() {
		try (ServerSocket socket = new ServerSocket(0);) {
			int openport = socket.getLocalPort();
			socket.close();
			return openport;
		} catch (IOException e) {
			e.printStackTrace();
			// if something fails, try port 31339. Guaranteed to work (R).
			return 31339;
		}
	}

	private int port;
	private Server server;
	public int getPort() {
		return this.port;
	}

	public static URL getRootURL() {
 		return HelpPlugin.getInstance().getBundle().getEntry("/");
	}
	
	public String makeUrlStringFor(String projectRelativePath) {
		return String.format("http://127.0.0.1:%s/%s", getPort(), projectRelativePath);
	}
	
	public Doc_Server(int open_port) {
		this.port = open_port;
//		try {
//			org.eclipse.help.internal.server.WebappManager.start(webappName);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		URL rootResource = getRootURL();

 		System.out.println("Doc Server root resource: " + rootResource);
 		System.out.println("Doc Server root resource: classloader: " + HelpPlugin.getInstance().getClass().getClassLoader());
 		System.out.println("Doc Server root resource: classloader type: " + HelpPlugin.getInstance().getClass().getClassLoader().getClass().toString());
		Resource baseResource = Resource.newResource(rootResource);
		Server server = new Server(port);

		ResourceService resourceService = new ResourceService() {
			
		};
		ResourceHandler resourceHandler = new ResourceHandler(resourceService) {
			
		};
		resourceHandler.setDirAllowed(true);
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[]{"index.html"});
		resourceHandler.setBaseResource(baseResource);
		
// 		URL res = Platform.getBundle("org.jcryptool.core.help").getEntry("./javascript/test.txt");
//		System.out.println();

		HandlerList handlerList = new HandlerList();
		handlerList.setHandlers(new Handler[]{resourceHandler, new DefaultHandler()});
		server.setHandler(handlerList);

		this.server = server;
	}

	private String slurpUrl(URL res) {
		InputStream stream = null;
		try {
			stream = Resource.newResource(res).getInputStream();
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		String result = "";
		try {
			while((line = reader.readLine()) != null) {
				result = result + line + "\n";
				
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public void start() throws Exception {
		this.server.start();
	}
	
}
