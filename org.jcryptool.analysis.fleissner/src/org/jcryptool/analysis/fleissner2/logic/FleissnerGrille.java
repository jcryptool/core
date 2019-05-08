package org.jcryptool.analysis.fleissner2.logic;
import java.util.ArrayList;

public class FleissnerGrille {
	 
	private boolean[][] grilleFilled;
	private boolean[][] grilleMove1;
	private boolean[][] grilleMove2;
	private boolean[][] grilleMove3;
	private boolean[][] grillePossible;
	
	private ArrayList<int[]> possibleTemplates = null;
	
//	Constructor builds grille and all of its rotations with parameter length as well as grillePossible 
//	to check if a coordinate is still free to fill
	public FleissnerGrille(int templateLength)
	{
		grilleFilled = new boolean[templateLength][templateLength];
		grilleMove1 = new boolean[templateLength][templateLength];
		grilleMove2 = new boolean[templateLength][templateLength];
		grilleMove3 = new boolean[templateLength][templateLength];
		grillePossible = new boolean[templateLength][templateLength];
		
		for (int y = 0; y < templateLength; y++) {
			for (int x = 0; x < templateLength; x++) {
				this.grilleFilled[x][y]=false;
				this.grilleMove1[x][y]=false;
				this.grilleMove2[x][y]=false;
				this.grilleMove3[x][y]=false;
				this.grillePossible[x][y]=true;
			}
		}
		
		if (templateLength%2==1) {
			this.grillePossible[templateLength/2][templateLength/2]=false;
		}
	}
//	sets a hole in coordinate (x,y) or deletes it
	public void setState(int x, int y, boolean state)
	{
		int templateLength = this.grilleFilled.length;
		
		this.grilleFilled[x][y]=state;
		this.grilleMove1[templateLength-1-y][x]=state;
		this.grilleMove2[templateLength-1-x][templateLength-1-y]=state;
		this.grilleMove3[y][templateLength-1-x]=state;
		this.grillePossible[x][y]=!state;
		this.grillePossible[templateLength-1-y][x]=!state;
		this.grillePossible[templateLength-1-x][templateLength-1-y]=!state;
		this.grillePossible[y][templateLength-1-x]=!state;
	}
	
	public boolean isPossible(int x, int y)
	{
		return this.grillePossible[x][y];
	}
	
	public boolean isFilled(int x, int y)
	{
		return this.grilleFilled[x][y];
	}
	
//	changes hole (x,y) to one of its rotations
	public void change(int x, int y, int move)
	{
		int newX=0, newY=0;
		int templateLength = this.grilleFilled.length;
		switch (move) {
		case 1:
			newX = templateLength-1-y;
			newY = x;
			break;
		case 2:
			newX = templateLength-1-x;
			newY = templateLength-1-y;
			break;
		case 3:
			newX = y;
			newY = templateLength-1-x;
			break;
		default:
			break;
		}
		this.setState(x, y, false);
		this.setState(newX, newY, true);
	}
	
//	sets hole back from change to one of its rotation
	public void undoChange(int x, int y, int move)
	{
		int newX=0, newY=0;
		int templateLength = this.grilleFilled.length;
		switch (move) {
		case 1:
			newX = templateLength-1-y;
			newY = x;
			break;
		case 2:
			newX = templateLength-1-x;
			newY = templateLength-1-y;
			break;
		case 3:
			newX = y;
			newY = templateLength-1-x;
			break;
		default:
			break;
		}
		this.setState(newX, newY, false);
		this.setState(x, y, true);
	}
	
//	created for brute force method, begins at the end (bottom right corner) and deletes latest hole and returns coordinates of that hole
	public int[] undoLastStep()
	{
		int templateLength = this.grilleFilled.length;
		int[] coordinates= new int[2];
		
		for (int j = templateLength-1; j >= 0; j--) {
			for (int i = templateLength-1; i >= 0; i--) {
				if (this.isFilled(i,j)) {
					this.setState(i, j, false);
						coordinates[0] = i;
						coordinates[1] = j;
						return coordinates;
				}
			}
		}
		return coordinates;
	}
	
//	builds all possible (and distinct) grilles with parameter length (and additonal parameter holes)
	public ArrayList<int[]> bruteForce(int templateLength, int holes) {
		
		int pitch=0;
		boolean nextPitch = true;
		int[] coordinates= new int[2];
		int[] template;
		this.possibleTemplates = new ArrayList<int[]>();
		
		do {
			for (int y=0;y<templateLength;y++) {
				for (int x=0;x<templateLength;x++) {
					if (this.isPossible(x,y)&&(nextPitch)) {
						this.setState(x, y, true);
						pitch++;
						if (pitch==holes) {
							template = saveTemplate(holes);
							this.possibleTemplates.add(template);
							pitch--;
							this.undoLastStep();
							template=new int[holes*2];
						}
					}
					if (x==coordinates[0]&&y==coordinates[1]) {
						
						nextPitch = true;
					}
				}
			}
			pitch--;
			coordinates = this.undoLastStep();
			nextPitch = false;
		}
		while (pitch>=0);
		return this.possibleTemplates;
	}
	
//	saves the current grille into an array and returns that array
	public int[] saveTemplate(int holes) {
		
		int templateLength = this.grilleFilled.length;
		int[] coordinates = new int[holes*2];
		int k=0;
		
		for (int y=0;y<templateLength;y++) {
			for (int x=0;x<templateLength;x++) {
				if (this.isFilled(x, y)) {
					coordinates[k]=x;
					k++;
					coordinates[k]=y;
					k++;
				}
			}
		}
		return coordinates;
	}
	
//	rotates current grille to the right (one general grille move). Used in Hill-Climbing method
	public void rotate() {

		int newX, newY;
		int templateLength = this.grilleFilled.length;
		ArrayList<int[]> coordinates = new ArrayList<>();
		
		for (int y = 0; y < templateLength; y++) {
			for (int x= 0;x<templateLength;x++) {
				if (this.isFilled(x,y)) {		
					newX = templateLength-1-y;
					newY = x;
					int[] coordinate = {newX,newY};
					coordinates.add(coordinate);
				}
			}
		}		
		this.clearGrille();
		for (int[] coordinate : coordinates) {
			this.setState(coordinate[0], coordinate[1], true);
		}
	}
	
//	sets grille back to default/ deletes all holes
	public void clearGrille() {
		
		int templateLength = this.grilleFilled.length;
		
		for (int y = 0; y < templateLength; y++) {
			for (int x = 0; x < templateLength; x++) {
				this.grilleFilled[x][y]=false;
				this.grilleMove1[x][y]=false;
				this.grilleMove2[x][y]=false;
				this.grilleMove3[x][y]=false;
				this.grillePossible[x][y]=true;
			}
		}
		if (templateLength%2==1) {
			this.grillePossible[templateLength/2][templateLength/2]=false;
		}
	}
	
//	Decrypting method. Decrypts a ciphertext using the current grille
	public String decryptText(ArrayList<char[][]> encryptedText)
	{
		String decryptedText = "";
		int textLength = encryptedText.get(0).length;

		for (char[][] text:encryptedText) 
		{
			for (int y = 0; y < textLength; y++) {
				for (int x = 0; x < textLength; x++) {
					if (this.grilleFilled[x][y])
					{
						decryptedText+=text[x][y];
					}
				}
			}
			for (int y = 0; y < textLength; y++) {
				for (int x = 0; x < textLength; x++) {
					if (this.grilleMove1[x][y])
					{
						decryptedText+=text[x][y];

					}
				}
			}
			for (int y = 0; y < textLength; y++) {
				for (int x = 0; x < textLength; x++) {
					if (this.grilleMove2[x][y])
					{
						decryptedText+=text[x][y];
						
					}
				}
			}
			for (int y = 0; y < textLength; y++) {
				for (int x = 0; x < textLength; x++) {
					if (this.grilleMove3[x][y])
					{
						decryptedText+=text[x][y];
					}
				}
			}
		}
		return decryptedText;
	}	
	
	
//	Encryption method. Encrypts a plaintext with parameter grille
	public String encryptText(String plaintext, int[] coordinates)
	{
	    String X = "X";
		String encryptedText = "";
		int templateLength = this.grilleFilled.length;
		int holes = ((coordinates.length)/2);
		int textChar = 0;
		char[][] template = new char[templateLength][templateLength];
		
		for (int i=0;i<holes;i++) {
			this.setState(coordinates[2*i], coordinates[2*i+1], true);
		}

		do
		{
			for (int y = 0; y < templateLength; y++) {
				for (int x = 0; x < templateLength; x++) {
					if (this.grilleFilled[x][y])
					{
						template[x][y]=plaintext.charAt(textChar);
						textChar++;
					}
				}
			}
			
			for (int y = 0; y < templateLength; y++) {
				for (int x = 0; x < templateLength; x++) {
					if (this.grilleMove1[x][y])
					{	
						template[x][y]=plaintext.charAt(textChar);
						textChar++;
					}
				}
			}

			for (int y = 0; y < templateLength; y++) {
				for (int x = 0; x < templateLength; x++) {
					if (this.grilleMove2[x][y])
					{
						template[x][y]=plaintext.charAt(textChar);
						textChar++;
					}
				}
			}
			
			for (int y = 0; y < templateLength; y++) {
				for (int x = 0; x < templateLength; x++) {
					if (this.grilleMove3[x][y])
					{
						template[x][y]=plaintext.charAt(textChar);
						textChar++;
					}
				}
			}
//			fill gaps
			if (templateLength%2==1) {
				template[templateLength/2][templateLength/2]=  (char) ('A' + 26*Math.random());
			}

//			build encrypted string
			for (int y=0;y<templateLength;y++) {
				for (int x=0;x<templateLength;x++) {
					encryptedText+=template[x][y];
				}
			}
		}
		while (textChar<plaintext.length()-1);
		return encryptedText;
	}
	
//	Sets current grille to given grille in parameter.
	public void useTemplate(int[] template, int templateLength) {

		if(template == null) {
			return;
		}
		else {
			int holes = template.length/2;
			clearGrille();
			for (int i=0;i<holes;i++) {
				this.setState(template[2*i],template[(2*i)+1],true);
			}
		}
	}
	

	@Override
//	builds a visualization of the current grille with an "X" at the coordinates of a hole and a "-" else
	public String toString() {
		
		String s="\n\nFilled:\n";
		int textLength = this.grilleFilled.length;

		for (int y = 0; y < textLength; y++) {
			for (int x = 0; x < textLength; x++) {
				s+= (this.grilleFilled[x][y] ? "X" : "-");
			}
			s+="\n";
		}
		return s;
	}
	
	public boolean[][] getGrilleFilled() {
		return this.grilleFilled;
	}

	public ArrayList<int[]> getPossibleTemplates() {
		return this.possibleTemplates;
	}
}
