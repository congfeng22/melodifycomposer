package com.generate.jsp;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
public class MarkovGenerator {
	public ArrayList<Integer> generate(HashMap <Integer, int[][]> database, HashMap <String, int[]> markovbase, int measures, int order){
        int beatsWritten = 0;
        ArrayList <Integer> melody = new ArrayList<Integer>();
        Random rand = new Random();
        ArrayList<Integer> possibleFirstNotes = new ArrayList<>(database.keySet());
        int firstNote = possibleFirstNotes.get(rand.nextInt(possibleFirstNotes.size()));
        melody.add(firstNote);
        int [] cumulpossibleSecondNotes = makeCumulative(database.get(firstNote)[9]);
        int secondNote = binarySearch(rand.nextInt(cumulpossibleSecondNotes[cumulpossibleSecondNotes.length - 1]) + 1, cumulpossibleSecondNotes, 0, cumulpossibleSecondNotes.length - 1);
        int [] cumulpossibleFirstBeats = makeCumulative(getColumn(database.get(firstNote), secondNote));
        int firstBeat = binarySearch(rand.nextInt(cumulpossibleFirstBeats[cumulpossibleFirstBeats.length - 1]) + 1, cumulpossibleFirstBeats, 0, cumulpossibleFirstBeats.length - 1);
        melody.add(firstBeat);
        melody.add(secondNote);
        beatsWritten += actualBeats(firstBeat);
        while (melody.size() < (order*2) - 1){
            int currNote = melody.get(melody.size() - 1);
            int [] cumulpossibleNextNotes = makeCumulative(database.get(currNote)[9]);
            int nextNote = binarySearch(rand.nextInt(cumulpossibleNextNotes[cumulpossibleNextNotes.length - 1]) + 1, cumulpossibleNextNotes, 0, cumulpossibleNextNotes.length - 1);
            int lastBeat = melody.get(melody.size()-2);
            int lastlastBeat = 0;
            int lastlastlastBeat = 0;
            if (melody.size() >=4) lastlastBeat = melody.get(melody.size()-4);
            if (melody.size() >=6) lastlastlastBeat = melody.get(melody.size()-4);
            int remains = beatsWritten%4;
            int nextBeat;
            if (remains == 0){
                if (lastBeat == 8 && lastlastlastBeat !=8){
                    nextBeat=8;
                    if (lastlastBeat == 8)beatsWritten += actualBeats(8);
                }
                else{
                    int [] cumulpossibleNextBeats = makeCumulative(getColumn(database.get(currNote),nextNote));
                    nextBeat = binarySearch(rand.nextInt(cumulpossibleNextBeats[cumulpossibleNextBeats.length - 1]) + 1, cumulpossibleNextBeats, 0, cumulpossibleNextBeats.length - 1);
                }
            }
            else{
                if (remains == 1 || remains == 2){
                    int [] cumulpossibleNextBeats = makeCumulative(getColumn(database.get(currNote), nextNote));
                    //System.out.println(cumulpossibleNextBeats[1]);
                    if (cumulpossibleNextBeats[1] > 0)nextBeat = binarySearch(rand.nextInt(cumulpossibleNextBeats[1])+1, cumulpossibleNextBeats, 0, cumulpossibleNextBeats.length - 1);
                    else{
                        if (beatsWritten %4 == 2 && melody.get(melody.size()-2)==0 || beatsWritten%4==1) nextBeat =0;
                        else nextBeat =1;
                    }
                }
                else nextBeat = 0;
            }
            if (nextBeat != 8) beatsWritten += actualBeats(nextBeat);
            melody.add(nextBeat);
            melody.add(nextNote);
        }
        //first x notes created with x notes and x beats
        while (beatsWritten < measures * 16){
            int currNote = melody.get(melody.size()-1);
            String notegram = "";
            for (int i = order; i > 0; i--) notegram += Integer.toString(melody.get(melody.size() - i*2 +1));
            if (markovbase.containsKey(notegram)){
                int [] cumulativePossibleNextNotes = makeCumulative(markovbase.get(notegram));
                int nextNote = binarySearch(rand.nextInt(cumulativePossibleNextNotes[cumulativePossibleNextNotes.length - 1]) + 1, cumulativePossibleNextNotes, 0, cumulativePossibleNextNotes.length - 1);
                int lastBeat = melody.get(melody.size()-2);
                int lastlastBeat = 0;
                int lastlastlastBeat = 0;
                if (melody.size() >=4) lastlastBeat = melody.get(melody.size()-4);
                if (melody.size() >=6) lastlastlastBeat = melody.get(melody.size()-4);
                int remains = beatsWritten%4;
                int nextBeat;
                if (remains == 0){
                    if (lastBeat == 8 && lastlastlastBeat !=8){
                        nextBeat=8;
                        if (lastlastBeat == 8)beatsWritten += actualBeats(8);
                    }
                    else{
                        int [] cumulpossibleNextBeats = makeCumulative(getColumn(database.get(currNote), nextNote));
                        nextBeat = binarySearch(rand.nextInt(cumulpossibleNextBeats[cumulpossibleNextBeats.length - 1]) + 1, cumulpossibleNextBeats, 0, cumulpossibleNextBeats.length - 1);
                    }
                }
                else{
                    if (remains == 1 || remains == 2){
                        int [] cumulpossibleNextBeats = makeCumulative(getColumn(database.get(currNote), nextNote));
                        //System.out.println(cumulpossibleNextBeats[1]);
                        if (cumulpossibleNextBeats[1] > 0)nextBeat = binarySearch(rand.nextInt(cumulpossibleNextBeats[1])+1, cumulpossibleNextBeats, 0, cumulpossibleNextBeats.length - 1);
                        else{
                            if (beatsWritten %4 == 2 && melody.get(melody.size()-2)==0 || beatsWritten%4==1) nextBeat =0;
                            else nextBeat =1;
                        }
                    }
                    else nextBeat = 0;
                }
                melody.add(nextBeat);
                if (nextBeat != 8) beatsWritten += actualBeats(nextBeat);
                if (beatsWritten < measures * 16) melody.add(nextNote);
            }
            else{
                int currNote2 = melody.get(melody.size() - 1);
                int [] cumulpossibleNextNotes2 = makeCumulative(database.get(currNote)[9]);
                int nextNote2 = binarySearch(rand.nextInt(cumulpossibleNextNotes2[cumulpossibleNextNotes2.length - 1]) + 1, cumulpossibleNextNotes2, 0, cumulpossibleNextNotes2.length - 1);
                int lastBeat2 = melody.get(melody.size()-2);
                int lastlastBeat2 = 0;
                int lastlastlastBeat2 = 0;
                if (melody.size() >=4) lastlastBeat2 = melody.get(melody.size()-4);
                if (melody.size() >=6) lastlastlastBeat2 = melody.get(melody.size()-4);
                int remains2 = beatsWritten%4;
                int nextBeat2;
                if (remains2 == 0){
                    if (lastBeat2 == 8 && lastlastlastBeat2 !=8){
                        nextBeat2=8;
                        if (lastlastBeat2 == 8)beatsWritten += actualBeats(8);
                    }
                    else{
                        int [] cumulpossibleNextBeats = makeCumulative(getColumn(database.get(currNote2),nextNote2));
                        nextBeat2 = binarySearch(rand.nextInt(cumulpossibleNextBeats[cumulpossibleNextBeats.length - 1]) + 1, cumulpossibleNextBeats, 0, cumulpossibleNextBeats.length - 1);
                    }
                }
                else{
                    if (remains2 == 1 || remains2 == 2){
                        int [] cumulpossibleNextBeats = makeCumulative(getColumn(database.get(currNote2), nextNote2));
                        //System.out.println(cumulpossibleNextBeats[1]);
                        if (cumulpossibleNextBeats[1] > 0)nextBeat2 = binarySearch(rand.nextInt(cumulpossibleNextBeats[1])+1, cumulpossibleNextBeats, 0, cumulpossibleNextBeats.length - 1);
                        else{
                            if (beatsWritten %4 == 2 && melody.get(melody.size()-2)==0 || beatsWritten%4==1) nextBeat2 =0;
                            else nextBeat2 =1;
                        }
                    }
                    else nextBeat2 = 0;
                }
                if (nextBeat2 != 8) beatsWritten += actualBeats(nextBeat2);
                melody.add(nextBeat2);
                if (beatsWritten < measures*16) melody.add(nextNote2);
            }
        }
        if (beatsWritten < 0){
            int excess = beatsWritten - measures * 16 ;
            int correctLast = melody.get(melody.size()-1) - excess;
            int correctLastNum = 0;
            if (correctLast==1) correctLastNum = 0;
            if (correctLast==2) correctLastNum = 1;
            if (correctLast==3) correctLastNum = 2;
            if (correctLast==4) correctLastNum = 3;
            if (correctLast==6) correctLastNum = 4;
            if (correctLast==8) correctLastNum = 5;
            if (correctLast==12) correctLastNum = 6;
            if (correctLast==16) correctLastNum = 7;
            melody.remove(melody.size() - 1);
            melody.add(correctLastNum);
        }
        return melody;
    }
    public int [] makeCumulative(int [] arr){
        int [] ret = new int[arr.length];
        ret[0] = arr[0];
        for (int i = 1; i < arr.length; i++){
            ret[i] = arr[i] + ret[i-1];
        }
        return ret;
    }
    public int[] getColumn(int[][] matrix, int column) {
        /*return IntStream.range(0, matrix.length)
                .map(i -> matrix[i][column]).toArray(); */
        int [] ret = new int [9];
        for (int r = 0; r < 9; r++){
            ret[r] = matrix[r][column];
        }
        return ret;
    }
    public int binarySearch(int num, int[] arr, int lidx, int ridx){
        /*
        if (lidx==ridx) return lidx;
        int mid = (lidx+ridx)/2;
        if (arr[mid]>num) return binarySearch(num,arr,lidx,mid-1);
        if (arr[mid]<num) return binarySearch(num,arr,mid+1,ridx);
        else return binarySearch(num,arr,lidx,mid);

         */

        if (lidx==ridx) return lidx;
        int m = (lidx+ridx)/2;
        if (arr[m]>=num) return binarySearch(num,arr,lidx,m);
        else return binarySearch(num,arr,m+1,ridx);


    }
    public int actualBeats(int beat){
        if (beat == 0) return 1;
        if (beat == 1) return 2;
        if (beat == 2) return 3;
        if (beat == 3 || beat == 8) return 4;
        if (beat == 4) return 6;
        if (beat == 5) return 8;
        if (beat == 6) return 12;
        else return 16;

    }
    public int firstNoneZeroIdx (int [] arr){
        for (int i = 0; i < arr.length; i++){
            if (arr[i] != 0) return i;
        }
        return -100;
    }
	public static void main(String[] args)throws FileNotFoundException {
		System.out.println("im running MarkovGenerator");
		String file = "./data/Thompson_Stopping by woods on a snowy evening.txt"; //A flat
        String file2 = "./data/Debussy_The snow is dancing.txt"; //F
        String file3 = "./data/Korngold_Der Schneemann.txt"; //A
        String file4 = "./data/Schubert_Serenade.txt";
        String file5 = "./data/Schubert_Fantasia.txt";
        String file6 = "./data/Sibelius_Violin Concerto in d minor.txt";
        HashMap <Integer, int[][]> database = new HashMap<>();
        ReadingFiles read = new ReadingFiles();
        ArrayList<ArrayList<Integer>> arrfile = read.transpose(file, "A flat", "B");
        ArrayList<ArrayList<Integer>> arrfile2 = read.transpose(file2, "F", "F");
        ArrayList <ArrayList<Integer>>arrfile3 = read.transpose(file3, "A", "B");
        ArrayList<ArrayList<Integer>> arrfile4 = read.transpose(file4, "F", "F");
        ArrayList<ArrayList<Integer>> arrfile5 = read.transpose(file5, "A flat", "E flat");
        ArrayList <ArrayList<Integer>> arrfile6 = read.transpose(file6, "F", "E flat");
        read.buildDatabase(arrfile, database);
        read.buildDatabase(arrfile2, database);
        read.buildDatabase(arrfile3, database);
        read.buildDatabase(arrfile4, database);
        read.buildDatabase(arrfile5, database);
        read.buildDatabase(arrfile6, database);
        BuildMarkov test = new BuildMarkov();
        HashMap<String, int[]> mdatabase = new HashMap<>();
        test.buildMarkov(3, arrfile4, mdatabase, database);
        test.buildMarkov(2, arrfile5, mdatabase, database);
        test.buildMarkov(2, arrfile6, mdatabase, database);
        //Generator generator = new Generator();
        MarkovGenerator mgenerator = new MarkovGenerator();
        //int [] arrtest = {0, 0, 0, 0, 2, 2, 2, 2};
        //System.out.println(generator.binarySearch(0, arrtest, 0, 7));
        ArrayList <Integer> generatedMelody = new ArrayList<>(mgenerator.generate(database, mdatabase, 2, 2));
        System.out.println(generatedMelody.toString());
        SoundPlayer player = new SoundPlayer();
        //player.play(generatedMelody);

	}

}