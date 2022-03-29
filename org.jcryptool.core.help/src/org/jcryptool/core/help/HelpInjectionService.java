package org.jcryptool.core.help;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class HelpInjectionService {
	public static class HelpInjector {
		public String toR;
		public Supplier<String> rW;
		public HelpInjector(String toR, Supplier<String> rW) {
			this.toR = toR;
			this.rW = rW;
		}
	}
	public static List<HelpInjector> injectors = new LinkedList();
	public static void addInjector(String toR, Supplier<String> rW) {
		var hi = new HelpInjector(toR,rW);
		injectors.add(hi);
	}

}
