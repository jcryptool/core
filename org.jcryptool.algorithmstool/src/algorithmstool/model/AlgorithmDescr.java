package algorithmstool.model;

import java.util.List;

public class AlgorithmDescr implements IAlgorithmDescr {

	private String name;
	private List<String> path;
	private String perspective;
	
	public AlgorithmDescr(String name, List<String> path, String perspective) {
		super();
		this.name = name;
		this.path = path;
		this.perspective = perspective;
	}
	
	public static String makePathString(List<String> path, String sep) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < path.size(); i++) {
			String p = path.get(i);
			sb.append(p);
			if(i!=path.size()-1) {
				sb.append(sep);
			}
		}
		return sb.toString();
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public List<String> getPath() {
		return path;
	}
	public String getPathString(String sep) {
		return AlgorithmDescr.makePathString(this.getPath(), sep);
	}
	
	
	@Override
	public String getPerspective() {
		return perspective;
	}
	
	@Override
	public String toString() {
		String mask = "'%s'\t%s\t%s";
		return String.format(mask, getName(), getPerspective(), getPathString(" > "));
	}
	
}
