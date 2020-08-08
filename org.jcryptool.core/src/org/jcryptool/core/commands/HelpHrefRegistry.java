package org.jcryptool.core.commands;

import java.util.HashMap;

public class HelpHrefRegistry {
	private static HelpHrefRegistry instance = null;
	private HashMap<String, String> registry;

	public HelpHrefRegistry() {
		this.registry = new HashMap<String, String>();
	}
	
	public String registerHrefFor(String pluginId, String helpHref) {
		return registry.put(pluginId, helpHref);
	}
	
	public String getHrefFor(String pluginId) {
		return this.registry.getOrDefault(pluginId, null);
	}

	public static HelpHrefRegistry getInstance() {
		if( HelpHrefRegistry.instance == null) {
			HelpHrefRegistry.instantiante();
			return getInstance();
		} else {
			return HelpHrefRegistry.instance;
		}
	}

	private static void instantiante() {
		instance = new HelpHrefRegistry();
	}
}
