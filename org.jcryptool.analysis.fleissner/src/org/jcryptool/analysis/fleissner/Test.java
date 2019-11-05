package org.jcryptool.analysis.fleissner;

//import java.time.Duration;
import java.util.ArrayList;

public class Test{
    
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
//        Konstruktor
//        grilleFilled = new boolean[templateLength][templateLength];
//        grilleMove1 = new boolean[templateLength][templateLength];
//        grilleMove2 = new boolean[templateLength][templateLength];
//        grilleMove3 = new boolean[templateLength][templateLength];
//        grillePossible = new boolean[templateLength][templateLength];
//        
//        for (int y = 0; y < templateLength; y++) {
//            for (int x = 0; x < templateLength; x++) {
//                grilleFilled[x][y]=false;
//                grilleMove1[x][y]=false;
//                grilleMove2[x][y]=false;
//                grilleMove3[x][y]=false;
//                grillePossible[x][y]=true;
//            }
//        }
//        
//        if (templateLength%2==1) {
//            grillePossible[templateLength/2][templateLength/2]=false;
//        }
//        setState(0,0,true);
//        setState(3,0,true);
//        setState(2,1,true);
//        setState(0,3,true);
//        setState(3,3,true);
//        setState(2,4,true);
//        
//        print();
//        rotate();
//        print();
//        rotate();
//        print();
//        rotate();
//        print();

//      long timeInMs = 60300L; 
//        
//        // Create a TimeUnit object 
//        TimeUnit time = TimeUnit.MINUTES; 
//  
//        // Convert Minutes to milliseconds 
//        // using convert() method 
//        System.out.println("Time " + timeInMinutes 
//                           + " minutes in milliSeconds = "
//                           + time.convert(timeInMinutes, 
//                                          TimeUnit.MINUTES)); 
//        output += "Finished analysis in "+end+" milliseconds";
        
//        Duration duration = Duration.ofDays(10); 
//        Duration duration = Duration.ofMillis(60300); 
//
//    System.out.println("Duration: " + duration); 
//
//    // Get the number of days 
//    // using toDays() method 
//    System.out.println(duration.toMinutes()); 
        
//    long timeInMs = 280685,timeInDays=0, timeInHours=0, timeInMinutes=0, timeInSeconds;
//    Duration duration = Duration.ofMillis(timeInMs); 
//    timeInSeconds = timeInMs/1000;
//    Duration duration = Duration.ofSeconds(timeInSeconds);
//    if (duration.toMinutes()>0) {
//        timeInMinutes = duration.toMinutes();
//        timeInSeconds-=timeInMinutes*60;
//        if (duration.toHours()>0) {
//            timeInHours = duration.toHours();
//            timeInMinutes-=timeInHours*60;
//            if (duration.toDays()>0) {
//                timeInDays = duration.toDays();
//                timeInHours-=timeInDays*24;
//            }
//        }
//    }
//    timeInDays = duration.toDays();
//    duration=duration.minusDays(timeInDays);
//    timeInHours = duration.toHours();
//    duration=duration.minusHours(timeInHours);
//    timeInMinutes = duration.toMinutes();
//    duration=duration.minusMinutes(timeInMinutes);
//    timeInSeconds = duration.getSeconds();
//    DurationFormatUtils.formatDuration(timeInMs, "**H:mm:ss**", true);


//    String.format("%02d:%02d:%02d", timeInHours, timeInMinutes, timeInSeconds);
    
//    SimpleDateFormat format = new SimpleDateFormat("DD:HH:mm:ss");
//    format.setTimeZone(TimeZone.getTimeZone("GMT"));
//    format.getNumberFormat();
//    return format.format(new Date(duration));
        
//        ArrayList<Integer> indizes = new ArrayList<>();
//        for (int i =0; i<30; i++) {
//            for(int j=0; j<30; j++) {
//                for (int k=0; k<30; k++) {
//                    for (int l=0; l<30; l++) {
//                        int index = i*((int) Math.pow(30,3))+j*((int) Math.pow(30,2))+k*((int) Math.pow(30,1))+l*((int) Math.pow(30,0));
//                        if (indizes.contains(index)) {
//                            System.out.println("Index bereits vorhanden");
//                            break;
//                        }else {
//                            indizes.add(index);
//                        }
//                    }
//                }
//            }
//        }
//        System.out.println("No Indexcrash");
        
        char[] key = {'K','E','Y'};
        int i= 0;
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String plaintext = "DURCHDIETEILUNGDESROEMISCHENREICHESINWESTROMUNDOSTROMWOKONSTANTINDASALTEBYZANTIONZURNEUENHAUPTSTADTKONSTANTINOPELAUSBAUTEENTWICKELNSICHZWEIUNTERSCHIEDLICHEKONFESSIONENDIEIHREDIFFERENZENZUEINEMNICHTGERINGENTEILIMJEWEILIGENUMGANGMITDENBILDERNDESHEILIGENSEHENWAEHRENDDASALTEROMNACHDENSTUERMENDERVOELKERWANDERUNGSZEITZUMZENTRUMDERROEMISCHKATHOLISCHENKIRCHEAUFSTEIGTENTFALTETSICHINKONSTANTINOPELDASORTHODOXECHRISTENTUMZUDENLEISTUNGENDERBYZANTINISCHENKUNSTGEHOERTDIEENTWICKLUNGEINESMOBILENKULTBILDESDERIKONEDIEZUEINEMZENTRALENBESTANDTEILDERORTHODOXENLITURGIEWIRDSOLITAERODERALSBILDERWANDIKONOSTASESTEHTSIEIMZENTRUMDERBILDERVEREHRUNGUNDBILDETVIELENEUEDARSTELLUNGSFORMENAUSIHRERFOLGRUFTALSGEGENBEWEGUNGDENBILDERSTREITHERVORINDEMSICHDIEBEIDENGRUNDSAETZLICHENHALTUNGENZUBILDERNFUERDIEGESAMTEGESCHICHTEDERKUNSTEXEMPLARISCHGEGENUEBERSTEHENIKONOKLASTENUNDIKONODULENUNTERKAISERJUSTINIANENTSTEHENNEUEKULTURELLEZENTRENAUCHIMWESTENBESONDERSRAVENNAWIRDMITBAUWERKENUNDBILDERSCHMUCKAUFGEWERTET";
        String ciphertext = "";
        int[] plainNumbers = new int[plaintext.length()];
        
//        for (int l= 0; l<plaintext.length(); l++) {
//            for (int g=0; g<alphabet.length();g++) {
//                if (plaintext.charAt(l)==alphabet.charAt(g))
//                    plainNumbers[l] = g;
//            }
//        }
//        
//        for (int j=0; j<plaintext.length(); j++) {
//            for (int k=0; k<alphabet.length(); k++) {
//                if (key[i]==alphabet.charAt(k)) {
//                    int index = (k+plainNumbers[j])%26;
//                    ciphertext += alphabet.charAt(index);
//                }
//            }
//            i++;
//            if (i==3)
//                i=0;
//        }
//        System.out.println(ciphertext);
        int[] counter = new int[alphabet.length()];
        
        String newCipher = "NYPMLBSIROMJERENIQBSCWMQMLCXVCSGFOWGXACCXPYQSXHMCXPYQUYOMXWRKRRSRBKWYVXCLCXKRRSSLJYPXISORFKYNDWRKHRUSLCXYXXGXSNOPYEWZKYROILDAGMOCVRQSGFJACSYLDIPCGFSIBVMARIIYRDOWQSSLORBSIGRVCNMDPIPORXORXEIGXIKXMARXEOVGXKCXXCSPGWNCGIGVMEORSWKYXKKSXBORZSPBOVLNIQRIGVMEORQOLCXAYOLPORBNEQKPROVMWRYMLBORQDYCBQCXHCBZMOPIOVUKRBOVSXKQJIGDDSWDCXXPEQBOVPYIKSWAROYDLMVMQMLCXOGBGFOESPWROMEDILDJYVXCDWGMLGXOMXWRKRRSRMZIJNEQYVRRSBYBCMLPSWRORREQXEHCXPCSWREREORBOVZIDYXXGXMQMLCXOSXWRQIFYIPDHGOILDAGMOJEREOMLOWKYFGVILUYJDFGVHCCHCBMIYRCNMCJYCSRCWDCXXPKPCXFCCXYXHROMJNIPYVRRSBYBCXPGDYPQMCGMPNWMVMRKIPYHCBEJCFGVHCBAYXHGUSLYWRKWCCXCRXQSIGWDCXXPEQBOVZSPBOVTOVCRVSXKSXHZSPBOXTSIJORCEIBKVQDIJVYLQWDYVKORYEWGRVCBJMVKPEJRKPQQIEORZOACQYLQHCXFGVHCBWRBIGDLCBZMBMLNIKCMARHGOFCSHCXKPERBCECDDJSGFORFKPREREORXEFGVHCBRDEIPNMCQIQKQROKCCGFSGFDIBOVIERQDIVOQNVEPSWARKCQILEIZOVQDIFORGUSLYOJKWRORSXHGUSLYHSVILERROVIKMQOVHEWRSRGKRCXXQDIFORLOYCUYJDYPOPJODCXXPORYEGFSQUOWRORZOWMXHCBWPKZCXRYGMPNQGDFYEACBOCXYLNFGVHCBWARQSMOYEJEOACBXCD";
        for (int n=4; n<newCipher.length();n+=5) {
            for (int m=0; m<alphabet.length(); m++){
                if (newCipher.charAt(n)==alphabet.charAt(m)) {
                    counter[m]++;
                }
            }   
        }
        String ausgabe = "";
        for (int f=0; f<alphabet.length();f++) {
            ausgabe +=alphabet.charAt(f)+": "+counter[f]+", ";
        }
        System.out.print(ausgabe);



//    System.out.println("Analysis finished in: "+String.format("%02d:%02d:%02d:%02d", timeInDays, timeInHours, timeInMinutes, timeInSeconds)/*+timeInDays+":"+timeInHours+":"+timeInMinutes+":"+timeInSeconds*/+" (dd:hh:mm:ss)"); 
    
//  System.out.println("Analysis finished in: "+format.format(new Date(timeInMs)));
    }

}
