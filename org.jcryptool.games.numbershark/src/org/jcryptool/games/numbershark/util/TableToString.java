package org.jcryptool.games.numbershark.util;

import org.eclipse.swt.widgets.Table;

public class TableToString {
	StringBuffer content = new StringBuffer();
	public TableToString(Table table){
		int numOfColumns = table.getColumnCount();
		int numOfRows = table.getItemCount();
		for(int i = 0; i < numOfColumns - 1; i++){
			content.append("\"" + table.getColumn(i).getText() + "\";");
		}
		content.append("\"" + table.getColumn(numOfColumns-1).getText() + "\"");
		for(int j = 0; j < numOfRows; j++){
			content.append("\n");
			for(int i = 0; i < numOfColumns - 1; i++){
				content.append("\"" + table.getItem(j).getText(i) + "\";");
			}
			content.append("\"" + table.getItem(j).getText(numOfColumns-1) + "\"");
		}
	}
	public String getContentToCSV(){
		return content.toString();
	}
	public void print(){
		System.out.print(content.toString());
	}
}
