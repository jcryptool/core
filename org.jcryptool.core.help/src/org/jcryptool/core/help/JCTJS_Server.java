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
		return Platform.getBundle("org.jcryptool.core.help").getEntry(".");
	}
	
	public String makeUrlStringFor(String projectRelativePath) {
		return String.format("http://127.0.0.1:%s/%s", getPort(), projectRelativePath);
	}
	
	public JCTJS_Server(int open_port) {
		this.port = open_port;
		URL rootResource = getRootURL();
		System.out.println(rootResource);
		Resource baseResource = Resource.newResource(rootResource);
		Server server = new Server(port);

		ResourceHandler resourceHandler = new ResourceHandler(new ResourceService());
		resourceHandler.setDirAllowed(true);
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[]{"index.html"});
		resourceHandler.setBaseResource(baseResource);
		
 		URL res = Platform.getBundle("org.jcryptool.core.help").getEntry("./javascript/test.txt");
		System.out.println();
		InputStream stream = null;
		try {
			stream = Resource.newResource(res).getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		try {
			while((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new HandlerList().setHandlers(new Handler[]{resourceHandler, new DefaultHandler()});
		server.setHandler(new HandlerList());

		this.server = server;
	}
	
	public void start() throws Exception {
		this.server.start();
	}
	
}
