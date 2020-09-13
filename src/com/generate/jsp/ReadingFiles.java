package com.generate.jsp;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
public class ReadingFiles {
	//returned transposed ArrayList of ArrayLists while each sub-list is [note, beat, note, beat...]
	public ArrayList<ArrayList<Integer>> transpose(String file, String Otonality, String Ntonality) throws FileNotFoundException {
        String[] majorTonalities = {"C", "D flat", "D", "E flat", "E", "F", "G flat", "G", "A flat", "A", "B flat", "B"};
        String[] minorTonalities = {"a", "b flat", "b", "c", "c sharp", "d", "e flat", "e", "f", "f sharp", "g", "g sharp"};
        int OtonalityNum = 0;
        int NtonalityNum = 0;
        if (Arrays.asList(majorTonalities).contains(Otonality)) OtonalityNum = Arrays.asList(majorTonalities).indexOf(Otonality);
        else OtonalityNum = Arrays.asList(minorTonalities).indexOf(Otonality);
        if (Arrays.asList(majorTonalities).contains(Ntonality))NtonalityNum = Arrays.asList(majorTonalities).indexOf(Ntonality);
        else NtonalityNum = Arrays.asList(minorTonalities).indexOf(Ntonality);
        int numOfTrans = NtonalityNum - OtonalityNum; //-10 <= numOfTrans <= 10
        ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
        InputStream i = ReadingFiles.class.getResourceAsStream(file);
        Scanner scanner = new Scanner(i);
        //major to...
        if (Arrays.asList(majorTonalities).contains(Otonality)) {
        	//major to major
        	if (Arrays.asList(majorTonalities).contains(Ntonality)) {
        		while (scanner.hasNextLine()) {
                	String line = scanner.nextLine();
                	ArrayList <Integer> melodyLine = new ArrayList <Integer>();
                	Scanner linescanner = new Scanner(line);
                	while (linescanner.hasNext()) {
                		int note = Integer.parseInt(linescanner.next());
                		int beat = Integer.parseInt(linescanner.next());
                		int transposedNewNote = note + numOfTrans;
                        if (transposedNewNote < 0) melodyLine.add(transposedNewNote + 12);
                        else if (transposedNewNote > 24) melodyLine.add(transposedNewNote - 12);
                        else melodyLine.add(transposedNewNote);
                        melodyLine.add(beat);	
                        //System.out.println(melodyLine.get(melodyLine.size()-2));
                	}
                	linescanner.close();
                    ret.add(melodyLine);	
                }
        	}
        	//major to minor
        	else {
        		numOfTrans -= 3; //-13 <= numOfTrans <= 7
        		while (scanner.hasNextLine()) {
        			String line = scanner.nextLine();
        			ArrayList <Integer> melodyLine = new ArrayList <Integer>();
        			Scanner linescanner = new Scanner(line);
                	while (linescanner.hasNext()) {
                		int note = Integer.parseInt(linescanner.next());
                		int beat = Integer.parseInt(linescanner.next());
                		int wrongThirdNote = NtonalityNum + 1;
               			int wrongSixthNote = NtonalityNum + 6;
               			int transposedNewNote = note + numOfTrans;
               			if (transposedNewNote%12 == wrongThirdNote || transposedNewNote%12 == wrongThirdNote - 12) transposedNewNote -= 1;
               			if (transposedNewNote%12 == wrongSixthNote || transposedNewNote%12 == wrongSixthNote - 12) transposedNewNote -= 1;
               			if (transposedNewNote < -24) melodyLine.add(transposedNewNote + 36);
               			else if (transposedNewNote < -12) melodyLine.add(transposedNewNote + 24);
               			else if (transposedNewNote < 0) {
               				melodyLine.add(transposedNewNote + 12);
               			}
               			else if (transposedNewNote > 24) melodyLine.add(transposedNewNote - 12);
               			else melodyLine.add(transposedNewNote);
               			melodyLine.add(beat);
               			//System.out.println(melodyLine.get(melodyLine.size()-2));
                	}
                	linescanner.close();
                	ret.add(melodyLine);
        			}
           }
      }
      //minor to...
      else if (Arrays.asList(minorTonalities).contains(Otonality)) {
    	  //minor to major 
    	  if (Arrays.asList(majorTonalities).contains(Ntonality)) {
    		  numOfTrans += 3; //-7 <= numOfTrans <= 13
    		  while (scanner.hasNextLine()) {
               		String line = scanner.nextLine();
               		ArrayList <Integer> melodyLine = new ArrayList<Integer>();
               		Scanner linescanner = new Scanner(line);
               		while (linescanner.hasNext()) {
               			int note = Integer.parseInt(linescanner.next());
               			int beat = Integer.parseInt(linescanner.next());
                        int wrongThirdNote = OtonalityNum;
                        int wrongSixthNote = OtonalityNum + 5;
                        int sevenminus = OtonalityNum +7;
                        int transposedNewNote = note + numOfTrans;
                        if (note%12 == wrongThirdNote||note%12 == wrongThirdNote-12||note%12 == wrongSixthNote||note%12 == wrongSixthNote-12||note%12 == sevenminus||note%12 == sevenminus-12) transposedNewNote += 1;
                        // do not have to consider six plus -> adding numOfTrans is exactly right
                        if (transposedNewNote < 0) melodyLine.add(transposedNewNote + 12);
                        else if (transposedNewNote > 36) melodyLine.add(transposedNewNote-24);
                        else if (transposedNewNote > 24) melodyLine.add(transposedNewNote - 12);
                        else melodyLine.add(transposedNewNote);
                        melodyLine.add(beat);
                        //System.out.println(melodyLine.get(melodyLine.size()-2));
               		}
               		linescanner.close();
               		ret.add(melodyLine);  	
               	}    		   
    	  }
    	  //minor to minor
    	  else {
    		   while (scanner.hasNextLine()) {
              		String line = scanner.nextLine();
              		ArrayList <Integer> melodyLine = new ArrayList <Integer>();
              		Scanner linescanner = new Scanner(line);
              		while (linescanner.hasNext()) {
              			int note = Integer.parseInt(linescanner.next());
              			int beat = Integer.parseInt(linescanner.next());
              			int sixthplus = OtonalityNum + 6;
                        int seventhminus = OtonalityNum + 7;
                        int transposedNewNote = note + numOfTrans; //-10 <= numOfTrans <= 10
                        if (note%12 == sixthplus|| note%12 == sixthplus - 12) transposedNewNote -= 1;
                        else if (note%12 == seventhminus || note%12 == seventhminus - 12) transposedNewNote += 1;
                        if (transposedNewNote < 0) melodyLine.add(transposedNewNote + 12);
                        else if (transposedNewNote > 24) melodyLine.add(transposedNewNote - 12);
                        else melodyLine.add(transposedNewNote);
                        melodyLine.add(beat);
                        //System.out.println(melodyLine.get(melodyLine.size()-2));
              		}
              		linescanner.close();
            		ret.add(melodyLine);                  		
              	}  
    	  }
       }
       scanner.close();
       return ret;       
    }
    public void buildDatabase(ArrayList<ArrayList<Integer>> arrdata, HashMap<Integer, int[][]> database) {
        for (ArrayList<Integer> data : arrdata) {
        	int length = data.size();
            for (int i = 0; i < length - 4; i = i + 2) {
                if (!database.containsKey(data.get(i))) {
                    int[][] twodarray = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                    database.put(data.get(i), twodarray);
                }
                database.get(data.get(i))[data.get(i + 1)][data.get(i + 2)] += 1;
                database.get(data.get(i))[9][data.get(i + 2)] += 1;
            }
            if (length - 2 >= 0 && database.containsKey(data.get(length-2))) {   
            	if (!database.containsKey(data.get(length-4))) {
                    int[][] twodarray = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                    database.put(data.get(length-4), twodarray);
                }
                database.get(data.get(length-4))[data.get(length-3)][data.get(length-2)] += 1;
                database.get(data.get(length-4))[9][data.get(length-2)] += 1;
            }
        }
    }
    public void buildSimpleDatabase(ArrayList<ArrayList<Integer>> arrdata, HashMap <Integer, int[]> simpleDatabase) {
    	for (ArrayList<Integer> data : arrdata) {
        	int length = data.size();
            for (int i = 0; i < length - 4; i = i + 2) {
                if (!simpleDatabase.containsKey(data.get(i))) {
                    int[] nextNoteArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    simpleDatabase.put(data.get(i), nextNoteArray);
                }
                simpleDatabase.get(data.get(i))[data.get(i + 2)] += 1;
            }
            if (length - 2 >= 0 && simpleDatabase.containsKey(data.get(length-2))) {   
            	if (! simpleDatabase.containsKey(data.get(length-4))) {
                    int[] nextNoteArray2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    simpleDatabase.put(data.get(length-4), nextNoteArray2);
                }
                simpleDatabase.get(data.get(length-4))[data.get(length-2)] += 1;
            }
        }
    }
    public static void main (String [] args) {
    	System.out.println("I'm running ReadingFiles");
    }

}