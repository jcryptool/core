package org.jcryptool.games.numbershark.util;

/**
 *  
 * @author Daniel Löffler * 
 * @version 0.9.5
 *
 */
public class ScoreTableRow {

	private String move;
	private String takenNumbers;
	private String points;
	private String lostScore;
	private String sharkPoints;
	private String remainingNumbers;
	private String lostNumbers;
	
		
	public String getTakenNumbers() {
		return takenNumbers;
	}
	public void setTakenNumbers(String takenNumbers) {
		this.takenNumbers = takenNumbers;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getLostScore() {
		return lostScore;
	}
	public void setLostScore(String lostScore) {
		this.lostScore = lostScore;
	}
	public String getSharkPoints() {
		return sharkPoints;
	}
	public void setSharkPoints(String sharkPoints) {
		this.sharkPoints = sharkPoints;
	}
	public String getRemainingNumbers() {
		return remainingNumbers;
	}
	public void setRemainingNumbers(String remainingNumbers) {
		this.remainingNumbers = remainingNumbers;
	}
	public String getLostNumbers() {
		return lostNumbers;
	}
	public void setLostNumbers(String lostNumbers) {
		this.lostNumbers = lostNumbers;
	}
	public String getMove() {
		return move;
	}
	public void setMove(String move) {
		this.move = move;
	}	
}
