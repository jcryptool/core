package org.jcryptool.analysis.fleissner.UI;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextPresentation {

	public String text = "";
	public boolean withScrollHorizontal = true;
	public boolean expandedInitially = false;
	public boolean tryFullLineWidth = false;
	
	public String getText() {
		return this.text.trim();
	}
	
	public List<String> getLines() {
		return Arrays.asList(this.getText().split("\\r?\\n"));
	}

	public String getFirstLine() {
		return this.getLines().stream().findFirst().get();
	}
	
	public List<String> getLinesAfterFirst() {
		return this.getLines().stream().skip(1).collect(Collectors.toList());
	}

	public String getLinesAfterFirstAsString() {
		StringBuilder builder = new StringBuilder();
		for( String line: this.getLinesAfterFirst() ) {
			builder.append(line);
			builder.append("\n");
		}
		return builder.toString();

	}
	
}
