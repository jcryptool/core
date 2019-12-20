package org.jcryptool.functionlistgen;

import java.util.LinkedList;
import java.util.List;

public class FunctionalityRecord {
	public String primaryId = null; // required field
	public String secondaryId = ""; // not required but necessary for some because of language-specific sorting
	public Character howImplemented = null;
	public List<String> path = new LinkedList<String>();

	public void setPath(List<String> path) {
		this.path = new LinkedList<String>();
		for (String pathelement : path) {
			String replaced = pathelement.replaceAll("\\s*\\(OID.*\\)\\s*", "");
			this.path.add(replaced);
		}
	}

	public void setPrimaryId(String primaryId) {
		this.primaryId = primaryId;
	}

	public void setSecondaryId(String secondaryId) {
		this.secondaryId = secondaryId;
	}

	public void setHowImplemented(Character howImplemented) {
		this.howImplemented = howImplemented;
	}

	public int compareTo(FunctionalityRecord other) {
		int cmp1 = this.primaryId.compareTo(other.primaryId);
		int cmp2 = this.secondaryId.compareTo(other.secondaryId);
		int cmp = -1000;
		if (cmp1 == 0) {
			cmp = cmp2;
		} else {
			cmp = cmp1;
		}

//		System.out.println(String.format("%d:%d,%d\n%s<>%s -- %s\n%s<>%s -- %s", 
//				cmp, cmp1, cmp2,
//				this.primaryId, this.secondaryId, this.path.toString(),
//				other.primaryId, other.secondaryId, other.path.toString()
//		));
		return cmp;
	}
	
	public String getFunctionality() {
		return this.path.get(this.path.size()-1);
	}
	
	public String createCSVLine() {
		String firstfield = String.format("[%s]", this.howImplemented);
		String secondfield = String.format("%s", this.joinPath());
		return String.format(";%s;%s", firstfield, secondfield);
	}
	
	private String joinPath() {
		String joinedPath = "";
		for (String pathElement : this.path) {
			if (! joinedPath.contentEquals("")) {
				joinedPath = joinedPath + " \\ ";
			}
			joinedPath = joinedPath + pathElement;
			
		}
		return joinedPath;
	}
}