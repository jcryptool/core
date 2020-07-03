package org.jcryptool.core.help;

import java.util.Optional;

public class JCTJS_Server {
	
	private static Optional<JCTJS_Server> instance;
	
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
		// TODO Auto-generated method stub
		return 31339;
	}

	private int port;
	public int getPort() {
		return this.port;
	}

	public JCTJS_Server(int open_port) {
		this.port = open_port; //TODO: this must be dynamical
		Resource baseResource = null; //TODO
		Server server = new Server(port);

		// Create the ResourceHandler. It is the object that will actually handle the request for a given file. It is
		// a Jetty Handler object so it is suitable for chaining with other handlers as you will see in other examples.
		ResourceHandler resourceHandler = new ResourceHandler();

		// Configure the ResourceHandler. Setting the resource base indicates where the files should be served out of.
		// In this example it is the current directory but it can be configured to anything that the jvm has access to.
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[]{"index.html"});
		resourceHandler.setBaseResource(baseResource);

		// Add the ResourceHandler to the server.
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[]{resourceHandler, new DefaultHandler()});
		server.setHandler(handlers);

		return server;
	}

	    public static void main(String[] args) throws Exception
	    {
	        int port = ExampleUtil.getPort(args, "jetty.http.port", 8080);
	        Path userDir = Paths.get(System.getProperty("user.dir"));
	        PathResource pathResource = new PathResource(userDir);

	        Server server = createServer(port, pathResource);

	        // Start things up! By using the server.join() the server thread will join with the current thread.
	        // See "http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Thread.html#join()" for more details.
	        server.start();
	        server.join();
	    }
	
}
