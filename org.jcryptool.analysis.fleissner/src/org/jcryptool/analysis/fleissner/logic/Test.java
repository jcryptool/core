package org.jcryptool.analysis.fleissner.logic;

import java.util.ArrayList;

public class Test {
    
    private static boolean[][] grilleFilled;
    private static boolean[][] grilleMove1;
    private static boolean[][] grilleMove2;
    private static boolean[][] grilleMove3;
    private static boolean[][] grillePossible;
    private static int templateLength = 5;
    private static ArrayList<int[]> coordinates;
    
//  setState
  public static void setState(int x, int y, boolean state) {
      grilleFilled[x][y]=state;
      grilleMove1[templateLength-1-y][x]=state;
      grilleMove2[templateLength-1-x][templateLength-1-y]=state;
      grilleMove3[y][templateLength-1-x]=state;
      grillePossible[x][y]=!state;
      grillePossible[templateLength-1-y][x]=!state;
      grillePossible[templateLength-1-x][templateLength-1-y]=!state;
      grillePossible[y][templateLength-1-x]=!state;
  }
  
//cleargrille
  
  public static void clearGrille() {
      for (int y = 0; y < templateLength; y++) {
          for (int x = 0; x < templateLength; x++) {
              grilleFilled[x][y]=false;
              grilleMove1[x][y]=false;
              grilleMove2[x][y]=false;
              grilleMove3[x][y]=false;
              grillePossible[x][y]=true;
          }
      }
      if (templateLength%2==1) {
          grillePossible[templateLength/2][templateLength/2]=false;
      }
  }
  
//rotate
public static void rotate() {
    int newX, newY;
    coordinates = new ArrayList<>();
    
    for (int y = 0; y < templateLength; y++) {
        for (int x= 0;x<templateLength;x++) {
            if (grilleFilled[x][y]) {       
                newX = templateLength-1-y;
                newY = x;
                int[] coordinate = {newX,newY};
                coordinates.add(coordinate);
            }
        }
    }       

    clearGrille();

    for (int[] coordinate : coordinates) {
        setState(coordinate[0], coordinate[1], true);
    }
}

public static void print() {
    String s="\n\nFilled:\n";

    for (int y = 0; y < templateLength; y++) {
        for (int x = 0; x < templateLength; x++) {
            s+= (grilleFilled[x][y] ? "X" : "-");
        }
        s+="\n";
    }
    System.out.println(s);
}

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        Konstruktor
        grilleFilled = new boolean[templateLength][templateLength];
        grilleMove1 = new boolean[templateLength][templateLength];
        grilleMove2 = new boolean[templateLength][templateLength];
        grilleMove3 = new boolean[templateLength][templateLength];
        grillePossible = new boolean[templateLength][templateLength];
        
        for (int y = 0; y < templateLength; y++) {
            for (int x = 0; x < templateLength; x++) {
                grilleFilled[x][y]=false;
                grilleMove1[x][y]=false;
                grilleMove2[x][y]=false;
                grilleMove3[x][y]=false;
                grillePossible[x][y]=true;
            }
        }
        
        if (templateLength%2==1) {
            grillePossible[templateLength/2][templateLength/2]=false;
        }
        setState(0,0,true);
        setState(3,0,true);
        setState(2,1,true);
        setState(0,3,true);
        setState(3,3,true);
        setState(2,4,true);
        
        print();
        rotate();
        print();
        rotate();
        print();
        rotate();
        print();

    }

}
