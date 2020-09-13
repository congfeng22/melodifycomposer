package com.generate.jsp;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;

public class melodyGenerator {
	private int[] possibleNextNotes;
	private int[] cumulativePossibleNextNotes;
	private int[] cumulpossibleNextNotes;
	public ArrayList<Integer> generator (HashMap <Integer, int[]> simpleDatabase, HashMap <String, int[]> markovbase, ArrayList<ArrayList<Integer>> beats, int order, String tonality, int chordOne, int chordTwo, int chordThree, int chordFour){
		String[] majorTonalities = {"C", "D flat", "D", "E flat", "E", "F", "G flat", "G", "A flat", "A", "B flat", "B"};
        String[] minorTonalities = {"a", "b flat", "b", "c", "c sharp", "d", "e flat", "e", "f", "f sharp", "g", "g sharp"};
        Random rand = new Random();
        ArrayList<Integer> melody = new ArrayList<>();
        HashSet <Integer> chordNotes1 = new HashSet <>(); 
        ArrayList <String> majorTonalitiesList = new ArrayList<>();
        ArrayList <String> minorTonalitiesList = new ArrayList<>();
        HashSet<Integer> scaleNotes = new HashSet<>();
        for (String tone: majorTonalities) majorTonalitiesList.add(tone);
        for (String mtone: minorTonalities) minorTonalitiesList.add(mtone);
        int tonicNote = 0;
        int firstNote;       
        int totalBeats = 0;
        boolean isMajor = true;
        if (minorTonalitiesList.contains(tonality)) isMajor = false;
        for (int b:beats.get(0)) totalBeats += actualBeats(b);
        int totalBeatsPerBar =totalBeats/4;
        if (totalBeats%4 != 0) System.out.println("Wrong Beats!");
        ArrayList <Integer> possibleFirstNotes = new ArrayList<>(simpleDatabase.keySet());
        //first bar
        //chord specified
        if (chordOne!=8) {
        	//major
            if (isMajor) {
            	tonicNote = majorTonalitiesList.indexOf(tonality);
            	scaleNotes = majorInScaleNotes(tonicNote);
            	if (chordOne < 10) chordNotes1 = majorFindChordNotes(chordOne, tonicNote);
            	else if (chordOne < 100) chordNotes1 = minorSeventhChordNotes(tonicNote + majorDegreeToTonicNote(chordOne/10));
            	else if (chordOne < 1000) chordNotes1 = majorSeventhChordNotes(tonicNote + majorDegreeToTonicNote(chordOne/100));
            	else chordNotes1 = dominantSeventhChordNotes(tonicNote + majorDegreeToTonicNote(chordOne/1000));
            }
            //minor
            else {
            	tonicNote = minorTonalitiesList.indexOf(tonality) - 3;
            	scaleNotes = minorInScaleNotes(tonicNote);
            	if (chordOne < 10) chordNotes1 = minorFindChordNotes(chordOne, tonicNote);
            	else if (chordOne < 100) chordNotes1 = minorSeventhChordNotes(tonicNote + minorDegreeToTonicNote(chordOne/10));
            	else if (chordOne < 1000) chordNotes1 = majorSeventhChordNotes(tonicNote + minorDegreeToTonicNote(chordOne/100));
            	else chordNotes1 = dominantSeventhChordNotes(tonicNote + minorDegreeToTonicNote(chordOne/1000));
            }
           	possibleFirstNotes.retainAll(chordNotes1);
            if (possibleFirstNotes.size() == 0) possibleFirstNotes = new ArrayList<>(simpleDatabase.keySet());
            //System.out.println("possible first note size: " + possibleFirstNotes.size());
            firstNote = possibleFirstNotes.get(rand.nextInt(possibleFirstNotes.size()));
        }
        
        //chord not specified
        else {
            firstNote = possibleFirstNotes.get(rand.nextInt(possibleFirstNotes.size()));
        	int possibleChord1;
        	int possibleChord2;
        	int possibleChord3;
        	int decideDegree = rand.nextInt(3);
            //major
            if (isMajor) {
            	possibleChord1 = majorFindDegree(firstNote, tonicNote);
            	possibleChord2 = possibleChord1 + 2;
                if (possibleChord2 >7) possibleChord2 -=7;
                possibleChord3 = possibleChord1 - 2;
                if (possibleChord3 <7) possibleChord3 +=7;
                if (decideDegree ==0) chordNotes1 = majorFindChordNotes(possibleChord1, tonicNote);
                else if (decideDegree ==1) chordNotes1 = majorFindChordNotes(possibleChord2, tonicNote);
                else chordNotes1 = majorFindChordNotes(possibleChord3, tonicNote);
            }
            //minor
            else {
            	possibleChord1 = minorFindDegree(firstNote, tonicNote);
            	possibleChord2 = possibleChord1 + 2;
                if (possibleChord2 >7) possibleChord2 -=7;
                possibleChord3 = possibleChord1 - 2;
                if (possibleChord3 <7) possibleChord3 +=7;
                if (decideDegree ==0) chordNotes1 = minorFindChordNotes(possibleChord1, tonicNote);
                else if (decideDegree ==1) chordNotes1 = minorFindChordNotes(possibleChord2, tonicNote);
                else chordNotes1 = minorFindChordNotes(possibleChord3, tonicNote);
            }
        }
        ArrayList <Integer> firstBar = generateOneBarWithAssignedBeats(simpleDatabase, markovbase, firstNote, order, chordNotes1, beats.get(0), scaleNotes);
        for (int i = 0; i < firstBar.size() ; i++) {
        	melody.add(firstBar.get(i));
        }
        //System.out.println("size: " + melody.size());
        
        //second -> last bar 
        for (int i = 0; i <3; i++) {
        	HashSet <Integer> chordNotes2 = new HashSet<>();
        	int firstNote2;
        	int lastNote = melody.get(melody.size()-2);
        	int currentChord;
        	ArrayList <Integer> barBeats2 = new ArrayList<>();
        	if (simpleDatabase.get(lastNote) == null) {
            	lastNote -= 12;	
            }
        	if (simpleDatabase.get(lastNote) == null) System.out.println(lastNote + " has no next note");
        	if (i==0) {        		
        		currentChord = chordTwo;
        		barBeats2 = beats.get(1);
        	}
            else if (i==1) {
            	currentChord = chordThree;
            	barBeats2 = beats.get(2);
            }
        	else {
        		currentChord = chordFour;
        		barBeats2 = beats.get(3);
        	}

            //chord specified
            if (currentChord !=8) {
                if (isMajor) {
                	if (currentChord < 10) chordNotes2 = majorFindChordNotes(currentChord, tonicNote);
                	else if (currentChord < 100) chordNotes2 = minorSeventhChordNotes(tonicNote + majorDegreeToTonicNote(currentChord/10));
                	else if (currentChord < 1000)chordNotes2 = majorSeventhChordNotes(tonicNote + majorDegreeToTonicNote(currentChord/100));
                	else chordNotes2 = dominantSeventhChordNotes(tonicNote+ majorDegreeToTonicNote(currentChord/1000));
                }
                else {
                	if (currentChord < 10) chordNotes2 = minorFindChordNotes(currentChord, tonicNote);
                	else if (currentChord < 100) chordNotes2 = minorSeventhChordNotes(tonicNote + minorDegreeToTonicNote(currentChord/10));                	
                	else if (currentChord < 1000)chordNotes2 = majorSeventhChordNotes(tonicNote + minorDegreeToTonicNote(currentChord/100));
                	else chordNotes2 = dominantSeventhChordNotes(tonicNote + minorDegreeToTonicNote(currentChord/1000));
                }
            	int [] possibleFirstNotes2 = biasedMakeCumulative(chordNotes2, simpleDatabase.get(lastNote));
            	
            	firstNote2 = binarySearch(rand.nextInt(possibleFirstNotes2[possibleFirstNotes2.length - 1]) + 1, possibleFirstNotes2, 0, possibleFirstNotes2.length - 1);	
            }
            //chord not specified
            else {
            	int [] possibleFirstNotes2 = makeCumulative(simpleDatabase.get(lastNote));
            	firstNote2 = binarySearch(rand.nextInt(possibleFirstNotes2[possibleFirstNotes2.length - 1]) + 1, possibleFirstNotes2, 0, possibleFirstNotes2.length - 1);	
            	//major
            	int possibleDegree1;
                int possibleDegree2;
                int possibleDegree3;
                int decideDegree2 = rand.nextInt(3);
                if (isMajor) {
                	possibleDegree1 = majorFindDegree(firstNote2, tonicNote);
                	possibleDegree2 = possibleDegree1 + 2;
                    if (possibleDegree2 >7) possibleDegree2 -=7;
                    possibleDegree3 = possibleDegree1 - 2;
                    if (possibleDegree3 <7) possibleDegree3 +=7;
                    if (decideDegree2 ==0) chordNotes2 = majorFindChordNotes(possibleDegree1, tonicNote);
                    else if (decideDegree2 ==1) chordNotes2 = majorFindChordNotes(possibleDegree2, tonicNote);
                    else chordNotes2 = majorFindChordNotes(possibleDegree3, tonicNote);
                }
                //minor
                else {
                	possibleDegree1 = minorFindDegree(firstNote2, tonicNote);
                	possibleDegree2 = possibleDegree1 + 2;
                    if (possibleDegree2 >7) possibleDegree2 -=7;
                    possibleDegree3 = possibleDegree1 - 2;
                    if (possibleDegree3 <7) possibleDegree3 +=7;
                    if (decideDegree2 ==0) chordNotes2 = minorFindChordNotes(possibleDegree1, tonicNote);
                    else if (decideDegree2 ==1) chordNotes2 = minorFindChordNotes(possibleDegree2, tonicNote);
                    else chordNotes2 = minorFindChordNotes(possibleDegree3, tonicNote);
                }
            }
            ArrayList <Integer> currBar = generateOneBarWithAssignedBeats(simpleDatabase, markovbase, firstNote2, order, chordNotes2, barBeats2, scaleNotes);
            for (int k = 0; k < currBar.size() ; k++) {
            	melody.add(currBar.get(k));
            }
            //System.out.println("size: "+ melody.size());
        }
        return melody;
	}
	public ArrayList<Integer> generateOneBarWithAssignedBeats(HashMap <Integer, int[]> simpleDatabase, HashMap <String, int[]> markovbase, int firstNote, int order, HashSet<Integer> chordNotes, ArrayList <Integer> beats, HashSet<Integer> scaleNotes){
		ArrayList<Integer> bar = new ArrayList<>();	
		HashSet<Integer> inScale = new HashSet<>(scaleNotes);
		Random rand = new Random();
        bar.add(firstNote);
        bar.add(beats.get(0));
        int i = 1;
        inScale.addAll(chordNotes);
        //for (Integer note: chordNotes) System.out.println(note);
        //System.out.println("end chord notes");
        //for (Integer in: inScale) System.out.println(in);
        //System.out.println("end in scale ");
        while(bar.size() < (order*2) && i < beats.size()) {
        	int currNote = bar.get(bar.size()-2);
        	possibleNextNotes = biasedMakeCumulative(chordNotes, simpleDatabase.get(currNote));
        	int nextNote = binarySearch(rand.nextInt(possibleNextNotes[possibleNextNotes.length-1])+1, possibleNextNotes, 0, possibleNextNotes.length-1);
        	while (nextNote == currNote || !inScale.contains(nextNote)) nextNote = binarySearch(rand.nextInt(possibleNextNotes[possibleNextNotes.length-1])+1, possibleNextNotes, 0, possibleNextNotes.length-1);
        	int dif = Math.abs(nextNote - currNote);
        	if (dif > 7) {
        		if (Math.abs(nextNote - 12 - currNote) < dif && nextNote - 12 >= 0) nextNote -=12; 
        		else if (Math.abs(nextNote +12 - currNote) < dif && nextNote + 12 < 25)nextNote += 12;
        	}
        	bar.add(nextNote);
        	bar.add(beats.get(i));
        	i++; 	
        }
        //use Markov after forming the first notegram
        while (i < beats.size()) {
        	int currNote = bar.get(bar.size()-2);
        	if (simpleDatabase.get(currNote) == null) currNote -= 12;
            String notegram = "";
            for (int k = order; k > 0; k--) {
            	if (bar.get(bar.size()-k*2) < 10) notegram += "0";
            	notegram += Integer.toString(bar.get(bar.size() - k*2));
            }
            if (markovbase.containsKey(notegram)){
                cumulativePossibleNextNotes = biasedMakeCumulative(chordNotes, markovbase.get(notegram));
                int nextNote = binarySearch(rand.nextInt(cumulativePossibleNextNotes[cumulativePossibleNextNotes.length - 1]) + 1, cumulativePossibleNextNotes, 0, cumulativePossibleNextNotes.length - 1);  
                while (nextNote == currNote || !inScale.contains(nextNote)) {
            		nextNote = binarySearch(rand.nextInt(cumulativePossibleNextNotes[cumulativePossibleNextNotes.length-1])+1, cumulativePossibleNextNotes, 0, cumulativePossibleNextNotes.length-1);	
            	}
                int dif = Math.abs(nextNote - currNote);
                if (dif > 7) {
            		if (Math.abs(nextNote - 12 - currNote) < dif && nextNote - 12 >= 0) nextNote -=12; 
            		else if (Math.abs(nextNote +12 - currNote) < dif && nextNote +12 < 25)nextNote += 12;
            	}
                bar.add(nextNote);
                bar.add(beats.get(i));
                i++;
            }
            else{
                cumulpossibleNextNotes = biasedMakeCumulative(chordNotes, simpleDatabase.get(currNote));
                int nextNote = binarySearch(rand.nextInt(cumulpossibleNextNotes[cumulpossibleNextNotes.length - 1]) + 1, cumulpossibleNextNotes, 0, cumulpossibleNextNotes.length - 1);
                int lastBeat = bar.get(bar.size()-2);
                while (nextNote == currNote || !inScale.contains(nextNote)) {
            		nextNote = binarySearch(rand.nextInt(cumulpossibleNextNotes[cumulpossibleNextNotes.length-1])+1, cumulpossibleNextNotes, 0, cumulpossibleNextNotes.length-1);		
            	}
            	int dif = Math.abs(nextNote - currNote);
            	if (dif > 7) {
            		if (Math.abs(nextNote - 12 - currNote) < dif && nextNote - 12 >= 0) nextNote -=12; 
            		else if (Math.abs(nextNote +12 - currNote) < dif && nextNote + 12 < 25)nextNote += 12;
            	}
                bar.add(nextNote);
                bar.add(beats.get(i));
                i++;
            }
        }
		return bar;
	}
	public ArrayList <Integer> addingFastBeats(ArrayList<Integer> original, int times){
		if (times > original.size()/2 || times == 0) {
			return original;
		}
		ArrayList<Integer> ret = new ArrayList<>();
		HashSet <Integer> changeIdx = new HashSet<>();
		Random rand = new Random();
		while (changeIdx.size() < times) changeIdx.add(rand.nextInt(original.size()));	
		int i = 0;
		int added = 0;
		while (added < original.size() && i < original.size()) {
			int beat = original.get(i);
			if (!changeIdx.contains(i)) ret.add(beat);		
			else {
				if (original.get(i) == 1) {
					ret.add(0);
					ret.add(0);
				}
				else if (beat == 2) {
					ret.add(0);
					ret.add(0);
					ret.add(0);
				}
				else if (beat == 3) {
					int decide = rand.nextInt(1);
					if (decide == 0) {
						ret.add(1);
						ret.add(1);
					}
					else {
						ret.add(8);
						ret.add(8);
						ret.add(8);
					}
				}
				else if (beat == 4) {
					ret.add(2);
					ret.add(0);
					ret.add(1);
					
				}
				else if (beat == 5) {
					ret.add(3);
					ret.add(3);
				}
				else if (beat == 6) {
					ret.add(4);
					ret.add(1);					
					ret.add(3);
				}
				else if (beat == 7) {
					ret.add(5);
					ret.add(5);
				}
				else {
					ret.add(beat);
				}
			}
			i++;
			added++;
		}
		return ret;
	}
	public ArrayList<Integer> addingSlowBeats (ArrayList <Integer> original, int times) {
		if (times > original.size()/2 || times == 0) {
			return original;
		}
		ArrayList <Integer> ret = new ArrayList<>();
		TreeSet <Integer> changeIdx = new TreeSet<>();
		int [] beats = {1, 2, 3, 4, 6, 8, 12, 16};
		TreeSet <Integer> existingBeats = new TreeSet<>();
		for (int b:beats) existingBeats.add(b);
		Random rand = new Random();
		while (changeIdx.size() < times) {
			changeIdx.add(rand.nextInt(original.size() - 1));
		}	
		int i = 0;
		int added = 0;
		while (added < original.size() && i < original.size()) {
			int beat = original.get(i);
			if (!changeIdx.contains(i)) ret.add(beat);
			else {
				if (original.get(i+1) == 8 || beat == 8) ret.add(beat);
				else {
					int sum = actualBeats(beat) + actualBeats(original.get(i+1));
					if (existingBeats.contains(sum)) {
						if (sum <= 4) {
							ret.add(sum - 1);
						}
						else {
							if (sum == 6) ret.add(4);
							else if (sum == 8) ret.add(5);
							else if (sum == 12) ret.add(6);
							else ret.add(7);
						}
						i++;
					}
					else ret.add(beat);
				}
			}
			added++;
			i++;
		}
		
		return ret;
	}
	public int[] biasedMakeCumulative(HashSet<Integer> chordNotes, int[] arr) {
		if (arr == null) System.out.println("null array");
		int [] ret = new int[arr.length];
		int originalTotal = 0;
		for (int num:arr) originalTotal += num;
		ret[0] = arr[0];
		for (int i = 1; i < arr.length; i++) {
			ret[i] = arr[i] + ret[i-1];
			if (chordNotes.contains(i) && arr[i]!=0) ret[i] += originalTotal*3;
		}
		return ret;
	}
	public int [] makeCumulative(int [] arr){
        int [] ret = new int[arr.length];
        ret[0] = arr[0];
        for (int i = 1; i < arr.length; i++){
            ret[i] = arr[i] + ret[i-1];
        }
        return ret;
    }
	public int actualBeats(int beat){
        if (beat == 0) return 1;
        else if (beat == 1) return 2;
        else if (beat == 2) return 3;
        else if (beat == 3 || beat == 8) return 4;
        else if (beat == 4) return 6;
        else if (beat == 5) return 8;
        else if (beat == 6) return 12;
        else if (beat == 7) return 16;
        else {
        	System.out.println("no such beat: " +  beat);
        	return -1;
        }
    }
	public int binarySearch(int num, int[] arr, int lidx, int ridx){
        if (lidx==ridx) return lidx;
        int m = (lidx+ridx)/2;
        if (arr[m]>=num) return binarySearch(num,arr,lidx,m);
        else return binarySearch(num,arr,m+1,ridx);
    }
	public HashSet<Integer> majorFindChordNotes (int degree, int tonicNote){
		HashSet<Integer> ret = new HashSet<>();
		int [] a47 = {-24, -20, -17, -12, -8, -5, 0, 4, 7, 12, 16, 19, 24};
		int [] a37 = {-24, -21, -17, -12, -9, -5, 0, 3, 7, 12, 15, 19, 24};
		int [] a36 = {-24, -21, -18, -12, -9, -6, 0, 3, 6, 12, 15, 18, 24};
		HashSet <Integer> fourSeven = new HashSet<>();
		HashSet <Integer> threeSeven = new HashSet<>();
		HashSet <Integer> threeSix = new HashSet<>();
		for (int num: a47) fourSeven.add(num);
		for (int num: a37) threeSeven.add(num);
		for (int num: a36) threeSix.add(num);
		int root = tonicNote;
		//I chord: root, root+4, root+7; 0<=root<=11
		if (degree == 1) {
			for (int i = 0; i < 25; i++) if (fourSeven.contains(i - root)) ret.add(i);
		}
		//ii chord: root, root+3, root+7; 2<=root<=13 
		else if (degree == 2) {
			root += 2;
			for (int i = 0; i < 25; i++) if (threeSeven.contains(i - root)) ret.add(i);
				
		}
		//iii chord: root, root+3, root+7; 4<=root<=15 
		else if (degree == 3) {
			root += 4;
			for (int i = 0; i < 25; i++) if (threeSeven.contains(i - root)) ret.add(i);
		}
		//IV chord: root, root+4, root+7; 5<=root<=16
		else if (degree == 4) {
			root += 5;
			for (int i = 0; i < 25; i++) if (fourSeven.contains(i - root)) ret.add(i);
		}
		//V chord: root, root+4, root+7; 7<=root<=18
		else if (degree == 5) {
			root += 7;
			for (int i = 0; i < 25; i++) if (fourSeven.contains(i - root)) ret.add(i);
		}
		//vi chord: root, root+3, root+7; 9<=root<=20 
		else if (degree == 6) {
			root += 9;
			for (int i = 0; i < 25; i++) if (threeSeven.contains(i - root)) ret.add(i);				
		}
		//vii chord: root, root+3, root+6; 11<=root<=22
		else {
			root += 11;
			for (int i = 0; i < 25; i++) if (threeSix.contains(i - root)) ret.add(i);
		}
		return ret;
	}
/*
	public HashSet<Integer> majorSeventhChordNotes(int degree, int tonicNote) {
		//0, 2, 4, 5, 7, 9, 11
		HashSet<Integer> ret = new HashSet<>();
		int root = tonicNote;
		//I chord: root, root+4, root+7, root+10; 0<=root<=11
		if (degree == 1) {
			for (int i = 0; i < 25; i++) {
				//-8, -5, -2, 0, 4, 7, 10, 12, 16, 19, 22, 24
				if (i > root+7) {
					if (i==root+10||i==root+12||i==root+16||i==root+19||i == root+22 ||i==root+24) ret.add(i);
				}
				else {
					if (i==root+7||i==root||i==root+4||i==root-2 || i==root-8||i==root-5) ret.add(i);
				}
			}
		}
		//ii chord: root, root+3, root+7, root+10; 2<=root<=13 
		else if (degree == 2) {
			root += 2;
			for (int i = 0; i < 25; i++) {
				//-12, -9, -5, -2, 0, 3, 7, 10, 12, 15, 19, 22
				if (i>root) {
					if (i==root+7||i==root + 10||i==root+12||i==root+15||i==root+19 || i==root+22) ret.add(i);
						
				}
				else {
					if (i==root+3 ||i==root||i == root-2 || i==root-5||i==root-9||i==root-12) ret.add(i);
				}
			}
		}
		//iii chord: root, root+3, root+7, root+10; 4<=root<=15 
		else if (degree == 3) {
			root += 4;
			for (int i = 0; i < 25; i++) {
				//-14, -12, -9, -5, -2, 0, 3, 7, 10, 12, 15, 19
				if (i>root) {
					if (i==root+3||i==root+7||i==root+10 ||i==root+12||i==root+15||i==root+19) ret.add(i);
				}
				else {
					if (i==root||i==root-2||i==root-5||i==root-9||i==root-12 ||i==root-14) ret.add(i);
				}
			}
		}
		//IV chord: root, root+4, root+7, root+11; 5<=root<=16
		else if (degree == 4) {
			root += 5;
			for (int i = 0; i < 25; i++) {
				//-13, -12, -8, -5, -1, 0, 4, 7, 11, 12, 16, 19
				if (i>root) {
					if (i==root+4||i==root+7||i==root+11||i==root+12||i==root+16||i==root+19)ret.add(i);
				}
				else {
					if(i==root||i==root-1||i==root-5||i==root-8||i==root-12 ||i==root-13) ret.add(i);
				}
			}
		}
		//V chord: root, root+4, root+7, root+10; 7<=root<=18
		else if (degree == 5) {
			root += 7;
			for (int i = 0; i < 25; i++) {
				//-17, -14, -12, -8, -5, -2, 0, 4, 7, 10, 12, 16
				if (i>=root) {
					if (i==root||i==root+4||i==root+7||i==root+12||i==root+16)ret.add(i);
				}
				else {
					if(i==root-2||i==root-5||i==root-8||i==root-12||i==root-14||i==root-17) ret.add(i);
				}
			}
		}
		//vi chord: root, root+3, root+7, root+10; 9<=root<=20 
		else if (degree == 6) {
			root += 9;
			for (int i = 0; i < 25; i++) {
				//-17, -14, -12, -9, -5, -2, 0, 3, 7, 10, 12, 15
				if (i>=root) {
					if (i==root||i==root+3||i==root+7||i==root+12||i==root+15) ret.add(i);
				}
				else {
					if (i==root-2||i==root-5||i==root-9||i==root-12||i==root-14||i==root-17) ret.add(i);
				}
			}
		}
		//vii chord: root, root+3, root+6, root+10; 11<=root<=22
		else {
			root += 11;
			for (int i = 0; i < 25; i++) {
				//-21, -18, -14, -12, -9, -6, -2, 0, 3, 6, 10, 12
				if (i>=root -6) {
					if (i==root-6||i==root-2||i==root||i==root+3||i==root+6||i==root+12) ret.add(i);
				}
				else {
					if (i==root-9||i==root-12||i==root-14||i==root-18||i==root-21) ret.add(i);
				}
			}
		}
		return ret;
	}
*/
	public HashSet<Integer> minorFindChordNotes(int degree, int tonicNote){
		HashSet<Integer> ret = new HashSet<>();
		int [] a47 = {-24, -20, -17, -12, -8, -5, 0, 4, 7, 12, 16, 19, 24};
		int [] a37 = {-24, -21, -17, -12, -9, -5, 0, 3, 7, 12, 15, 19, 24};
		int [] a36 = {-24, -21, -18, -12, -9, -6, 0, 3, 6, 12, 15, 18, 24};
		int [] a48 = {-24, -20, -16, -12, -8, -4, 0, 4, 8, 12, 16, 20, 24};
		HashSet <Integer> fourSeven = new HashSet<>();
		HashSet <Integer> threeSeven = new HashSet<>();
		HashSet <Integer> threeSix = new HashSet<>();
		HashSet <Integer> fourEight = new HashSet<>();
		for (int num: a47) fourSeven.add(num);
		for (int num: a37) threeSeven.add(num);
		for (int num: a36) threeSix.add(num);
		for (int num: a48) fourEight.add(num);
		int root = tonicNote;
		//i chord: root, root+3, root+7; -3<=root<=8
		if (degree == 1) {
			for (int i = 0; i < 25; i++) if (threeSeven.contains(i - root)) ret.add(i);
		}
		//ii chord: root, root+3, root+6; -1<=root<=10
		else if (degree == 2) {
			root += 2;
			for (int i = 0; i < 25; i++) if (threeSix.contains(i - root)) ret.add(i);
		}
		//III chord: root, root+4, root+8; 0<=root<=11 (seventh note +1)
		else if (degree == 3) {
			root += 3;
			for (int i = 0; i < 25; i++) if (fourEight.contains(i - root)) ret.add(i);
		}
		//iv chord: root, root+3, root+7; 2<=root<=13
		else if (degree == 4) {
			root += 5;
			for (int i = 0; i < 25; i++) if (threeSeven.contains(i - root)) ret.add(i);
		}
		//V chord: root, root+4, root+7; 4<=root<=15 (seventh note +1)
		else if (degree == 5) {
			root += 7;
			for (int i = 0; i < 25; i++) if (fourSeven.contains(i - root)) ret.add(i);
		}
		//vi chord: root, root+4, root+7; 5<=root<=16
		else if (degree == 6) {
			root += 8;
			for (int i = 0; i < 25; i++) if (fourSeven.contains(i - root)) ret.add(i);
		}
		//vii chord: root, root+3, root+6; 8<=root<=19 (seventh note + 1)
		else {
			root += 11;
			for (int i = 0; i < 25; i++) if (threeSix.contains(i - root)) ret.add(i);
		}
		return ret;
	}
	
	public HashSet<Integer> majorSeventhChordNotes(int tonicNote) {
		HashSet<Integer> ret = new HashSet<>();
		//major chord: root, root+4, root+7, root+11; 0<=tonicNote<=11
		int [] notes = {tonicNote - 24, tonicNote - 20, tonicNote - 17, tonicNote - 13, tonicNote - 12, tonicNote-8, tonicNote-5, tonicNote-1, tonicNote, tonicNote+4, tonicNote+7, tonicNote+11, tonicNote+12, tonicNote+16, tonicNote+19, tonicNote+23, tonicNote+24};
		HashSet<Integer> sNotes = new HashSet<>();
		for (int n:notes) sNotes.add(n);
		for (int i = 0; i < 25; i++) {
			if (sNotes.contains(i)) ret.add(i);
		}
		return ret;
	}
	public HashSet<Integer> minorSeventhChordNotes(int tonicNote){
		HashSet<Integer> ret = new HashSet<>();
		//i chord: root, root+3, root+7, root+10; 0 <= tonicNote <= 11
		int [] notes = {tonicNote - 24, tonicNote - 21, tonicNote - 17, tonicNote - 14, tonicNote-12, tonicNote - 9, tonicNote-5, tonicNote-2, tonicNote, tonicNote+3, tonicNote+7, tonicNote +10, tonicNote+12, tonicNote+15, tonicNote+19, tonicNote+22, tonicNote+24};
		HashSet<Integer> sNotes = new HashSet<>();
		for (int n: notes) sNotes.add(n);
		for (int i = 0; i < 25; i++) {
			if (sNotes.contains(i)) ret.add(i);
		}
		return ret;
	}
	public HashSet<Integer> dominantSeventhChordNotes(int tonicNote){
		HashSet<Integer> ret = new HashSet<>();
		//i chord: root, root+4, root+7, root+10; 0 <= tonicNote <= 11
		int [] notes = {tonicNote - 24, tonicNote - 20, tonicNote - 17, tonicNote - 14, tonicNote-12, tonicNote - 8, tonicNote-5, tonicNote-2, tonicNote, tonicNote+4, tonicNote+7, tonicNote +10, tonicNote+12, tonicNote+16, tonicNote+19, tonicNote+22, tonicNote+24};
		HashSet<Integer> sNotes = new HashSet<>();
		for (int n: notes) sNotes.add(n);
		for (int i = 0; i < 25; i++) {
			if (sNotes.contains(i)) ret.add(i);
		}
		return ret;
	}
	//note = root note for degree x chord
	//0, 2, 4, 5, 7, 9, 11
	public int majorFindDegree (int note, int tonicNote) {
		int difference = note - tonicNote;
		if (difference <0) difference+=12;
		if (difference <=5) {
			if (difference == 0) return 1;
			else if (difference == 2) return 2;
			else if (difference == 4) return 3;
			else if (difference == 5)return 4;
			else return -1;
		}
		else {
			if (difference == 7) return 5;
			else if (difference == 9) return 6;
			else if (difference == 11)return 7;
			else return -1;
			
		}
	}
	//0, 2, 3, 5, 7, 8, 11
	public int minorFindDegree (int note, int tonicNote) {
		int difference = note - tonicNote;
		if (difference <0) difference+=12;
		if (difference <=5) {
			if (difference == 0) return 1;
			else if (difference == 2) return 2;
			else if (difference == 3) return 3;
			else if (difference ==5 )return 4;
			else return -1;
		}
		else {
			if (difference == 7) return 5;
			else if (difference == 8) return 6;
			else if (difference ==11) return 7;
			else return -1;
			
		}
	}
	//0, 2, 3, 5, 7, 8, 11
	public int minorDegreeToTonicNote(int degree) {
		if (degree < 5) {
			if (degree == 1) return 0;
			else if (degree == 2) return 2;
			else if (degree == 3) return 3;
			return 5;
		}
		else {
			if (degree == 5) return 7;
			else if (degree == 6) return 8;
			return 10;
		}
	}
	//0, 2, 4, 5, 7, 9, 11
	public int majorDegreeToTonicNote(int degree) {
		if (degree <= 4) {
			if (degree == 1) return 0;
			else if (degree == 2) return 2;
			else if (degree == 3) return 4;
			return 5;
		}
		else {
			if (degree == 5) return 7;
			else if (degree == 6) return 9;
			return 11;
		}
	}
	public HashSet<Integer> majorInScaleNotes(int tonicNote){
		HashSet<Integer> ret = new HashSet<>();
		int [] diff = {0, 2, 4, 5, 7, 9, 11};
		for (int d : diff) {
			if (tonicNote + d < 25 && tonicNote + d >= 0)ret.add(tonicNote + d);
			if (tonicNote + 12 + d < 25) ret.add(tonicNote + 12 + d);
			if (tonicNote - 12 + d >= 0) ret.add((tonicNote - 12 + d));
			if (tonicNote + 24 + d <25) ret.add(tonicNote + 24 + d);
		}		
		return ret;
	}
	public HashSet<Integer> minorInScaleNotes(int tonicNote){
		HashSet<Integer> ret = new HashSet<>();
		int [] diff = {0, 2, 3, 5, 7, 8, 10, 11};
		for (int d : diff) {
			if (tonicNote + d < 25 && tonicNote + d >= 0)ret.add(tonicNote + d);
			if (tonicNote + 12 + d < 25) ret.add(tonicNote + 12 + d);
			if (tonicNote - 12 + d >= 0) ret.add((tonicNote - 12 + d));
			if (tonicNote + 24 + d <25) ret.add(tonicNote + 24 + d);
		}		
		return ret;
	}
	public boolean isFlatKey(String scale) {
		String[] flatArray = {"D flat", "E flat", "F", "G flat", "A flat", "B flat", "b flat", "c", "d", "e flat", "f", "g"};
		HashSet <String> flatKeys = new HashSet<>(Arrays.asList(flatArray));
		if (flatKeys.contains(scale)) return true;
		return false;
	}
	public int[] calmingChordProgression(){
		int[] ret = new int[4];
		HashMap <Integer, int[]> chords = new HashMap<>(); 
		int[] chord0 = {1, 5, 6, 4};
		int[] chord1 = {1, 5, 6, 3};
		int[] chord2 = {1, 67, 5, 5700};
		int[] chord3 = {1, 27, 1, 170};
		int[] chord4 = {1, 3700, 6700, 2700};
		int[] chord5 = {27, 3, 770, 1};
		int[] chord6 = {1, 470, 27, 5};
		int[] chord7 = {1, 3, 4, 1};
		int[] chord8 = {170, 570, 27, 470};
		int[] chord9 = {170, 37, 470, 5700};
		chords.put(0, chord0);
		chords.put(1, chord1);
		chords.put(2, chord2);
		chords.put(3, chord3);
		chords.put(4, chord4);
		chords.put(5, chord5);
		chords.put(6, chord6);
		chords.put(7, chord7);
		chords.put(8, chord8);
		chords.put(9, chord9);
		Random rand = new Random();
		int decide = rand.nextInt(10);
		return chords.get(decide);
	}
	public ArrayList<ArrayList<Integer>> calmingBeats() {
		ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
		Random rand = new Random();
		int decide = rand.nextInt(10);
		int[] beats0 = {5, 1, 1, 1, 1, 5, 1, 1, 1, 1, 3, 3, 5, 1, 1, 1, 1, 1, 1, 1, 1};
		int[] beats1 = {3, 3, 3, 3, 3, 3, 4, 1, 3, 3, 3, 3, 3, 3, 4, 1};
		int[] beats2 = {4, 1, 1, 4, 3, 1, 3, 1, 3, 1, 4, 3, 3, 3, 1, 4, 1, 1};
		int[] beats3 = {5, 3, 3, 5, 3, 8, 8, 8, 3, 5, 8, 8, 8, 8, 8, 8, 5, 3};
		int[] beats4 = {1, 1, 3, 4, 1, 1, 1, 5, 3, 1, 1, 3, 4, 1, 1, 1, 5, 3};
		int[] beats5 = {1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 5, 1, 1, 1, 5, 1, 1, 1, 1, 5, 1};
		int[] beats6 = {4, 1, 3, 1, 1, 1, 1, 1, 1, 5, 4, 1, 3, 1, 1, 1, 1, 1, 1, 5};
		int[] beats7 = {3, 3, 3, 3, 5, 3, 1, 1, 3, 3, 3 ,3, 6, 3};
		int[] beats8 = {0, 0, 0, 0, 4, 1, 3, 3, 4, 1, 3, 0, 0, 0, 0, 4, 1, 3, 3, 6 };
		int[] beats9 = {1, 1, 4, 1, 3, 1, 1, 6, 1, 1, 4, 1, 3, 1, 1, 6};
		ArrayList <Integer> beats = new ArrayList<>();
		if (decide <5) {
			if (decide == 0) return getFastBarBeats(beats0, 0, 0, 1, 0);
			else if (decide == 1) return getSlowBarBeats(beats1, 0, 1, 0, 1);
			else if (decide == 2) return getSlowBarBeats(beats2, 1, 1, 1, 1);
			else if (decide == 3) return getFastBarBeats(beats3, 1, 1, 1, 1);
			else return getSlowBarBeats(beats4, 1, 1, 1, 1);
		}
		else {
			if (decide == 5) return getSlowBarBeats(beats5, 2, 0, 1, 0);
			else if (decide == 6) return getSlowBarBeats(beats6, 0, 1, 0, 1);
			else if (decide == 7) return getSlowBarBeats(beats7, 1, 0, 1, 0);
			else if (decide == 8) return getFastBarBeats(beats8, 1, 1, 1, 1);
			else return getSlowBarBeats(beats9, 1, 0, 1, 0);
		}
	}
	public ArrayList<ArrayList<Integer>> sadBeats() {
		ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
		Random rand = new Random();
		int decide = rand.nextInt(10);
		int[] beats0 = {1, 1, 1, 1, 1, 1, 1, 1, 6, 1, 1, 1, 1, 1, 1, 3, 3, 5, 5};
		int[] beats1 = {5, 5, 6, 3, 3, 3, 3, 3, 6, 3};
		int[] beats2 = {3, 3, 5, 7, 4, 1, 1, 1, 1, 1, 7};
		int[] beats3 = {3, 6, 3, 5, 1, 1, 6, 3, 7};
		int[] beats4 = {3, 1, 1, 1, 1, 1, 1, 3, 3, 4, 1, 1, 3, 3, 4, 7};
		int[] beats5 = {4, 1, 3, 5, 1, 1, 3, 4, 1, 3, 3, 1, 1};
		int[] beats6 = {5, 3, 3, 4, 1, 5, 5, 3, 3, 4, 1, 5};
		int[] beats7 = {6, 2, 0, 6, 3, 3, 1, 1, 1, 1, 1, 1, 6, 3};
		int[] beats8 = {8, 8, 8, 6, 8, 8, 8, 4, 1, 3, 8, 8, 8, 6, 3, 3, 5};
		int[] beats9 = {5, 1, 1, 1, 1, 5, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 7};
		ArrayList <Integer> beats = new ArrayList<>();
		if (decide <5) {
			if (decide == 0) return getSlowBarBeats(beats0, 1, 0, 1, 0);
			else if (decide == 1) return getSlowBarBeats(beats1, 0, 0, 1, 0);
			else if (decide == 2) return getSlowBarBeats(beats2, 0, 0, 1, 0);
			else if (decide == 3) return getFastBarBeats(beats3, 0, 0, 0, 0);
			else return getSlowBarBeats(beats4, 1, 1, 1, 0);
		}
		else {
			if (decide == 5) return getSlowBarBeats(beats5, 2, 0, 1, 0);
			else if (decide == 6) return getSlowBarBeats(beats6, 0, 1, 0, 1);
			else if (decide == 7) return getSlowBarBeats(beats7, 1, 0, 1, 0);
			else if (decide == 8) return getFastBarBeats(beats8, 1, 1, 1, 1);
			else return getSlowBarBeats(beats9, 1, 0, 1, 0);
		}
	}
	public int[] sadChordProgression(){
		int[] ret = new int[4];
		HashMap <Integer, int[]> chords = new HashMap<>(); 
		int[] chord0 = {1, 27, 7700, 3};
		int[] chord1 = {1, 5, 6, 5};
		int[] chord2 = {1, 3, 7, 6};
		int[] chord3 = {1, 5, 2, 7};
		int[] chord4 = {1, 2, 4, 5};
		int[] chord5 = {1, 3, 4, 5};
		int[] chord6 = {1, 5, 6, 3};
		int[] chord7 = {1, 4, 1, 4};
		int[] chord8 = {1, 7, 4, 5};
		int[] chord9 = {1700, 4, 1, 5700};
		chords.put(0, chord0);
		chords.put(1, chord1);
		chords.put(2, chord2);
		chords.put(3, chord3);
		chords.put(4, chord4);
		chords.put(5, chord5);
		chords.put(6, chord6);
		chords.put(7, chord7);
		chords.put(8, chord8);
		chords.put(9, chord9);
		Random rand = new Random();
		int decide = rand.nextInt(10);
		return chords.get(decide);
	}
	public int[] joyfulChordProgression(){
		int[] ret = new int[4];
		HashMap <Integer, int[]> chords = new HashMap<>(); 
		int[] chord0 = {1, 6, 4, 5};
		int[] chord1 = {1, 4, 6, 57};
		int[] chord2 = {1, 3, 7, 5700};
		int[] chord3 = {1, 6, 3, 7};
		int[] chord4 = {1, 4, 5, 27};
		int[] chord5 = {1, 4, 5, 3700};
		int[] chord6 = {1, 2, 4, 5};
		int[] chord7 = {2, 5700, 7, 67};
		int[] chord8 = {1, 4, 5, 1};
		int[] chord9 = {27, 5700, 17, 27};
		chords.put(0, chord0);
		chords.put(1, chord1);
		chords.put(2, chord2);
		chords.put(3, chord3);
		chords.put(4, chord4);
		chords.put(5, chord5);
		chords.put(6, chord6);
		chords.put(7, chord7);
		chords.put(8, chord8);
		chords.put(9, chord9);
		Random rand = new Random();
		int decide = rand.nextInt(10);
		return chords.get(decide);
	}
	public ArrayList<ArrayList<Integer>> joyfulBeats() {
		ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
		Random rand = new Random();
		int decide = rand.nextInt(10);
		//int decide = rand.nextInt(10);
		int[] beats0 = {3, 3, 1, 3, 1, 1, 1, 1, 1, 4, 1, 3, 3, 1, 3, 1, 1, 1, 1, 1, 4, 1};
		int[] beats1 = {1, 1, 5, 1, 1, 1, 1, 5, 1, 1, 1, 1, 5, 1, 1, 1, 1, 5, 1, 1};
		int[] beats2 = {1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 4};
		int[] beats3 = {3, 1, 1, 3, 3, 5, 5, 3, 1, 1, 3, 3, 5, 5};
		int[] beats4 = {5, 5, 3, 1, 3, 4, 5, 5, 3, 1, 3, 4};
		int[] beats5 = {3, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1};
		int[] beats6 = {1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 5, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 3, 3};
		int[] beats7 = {3, 3, 1, 4, 1, 4, 5, 3, 3, 1, 4, 1, 4, 5};
		int[] beats8 = {2, 0, 2, 0, 3, 3, 2, 0, 2, 0, 5, 2, 0, 2, 0, 3, 3, 1, 4, 5};
		int[] beats9 = {5, 3, 3, 3, 1, 1, 5, 5, 3, 3, 3, 1, 1, 5};
		ArrayList <Integer> beats = new ArrayList<>();
		if (decide <5) {
			if (decide == 0) return getFastBarBeats(beats0, 1, 0, 1, 0);
			else if (decide == 1) return getFastBarBeats(beats1, 1, 1, 1, 1);
			else if (decide == 2) return getFastBarBeats(beats2, 0, 0, 0, 0);
			else if (decide == 3) return getFastBarBeats(beats3, 0, 1, 0, 1);
			else return getFastBarBeats(beats4, 1, 0, 1, 0);
		}
		else {
			if (decide == 5) return getFastBarBeats(beats5, 0, 0, 0, 0);
			else if (decide == 6) return getFastBarBeats(beats6, 0, 1, 0, 0);
			else if (decide == 7) return getFastBarBeats(beats7, 0, 1, 0, 1);
			else if (decide == 8) return getFastBarBeats(beats8, 0, 1, 0, 1);
			else return getFastBarBeats(beats9, 1, 0, 1, 0);
		}
	}
	public int[] fearfulChordProgression(){
		int[] ret = new int[4];
		HashMap <Integer, int[]> chords = new HashMap<>(); 
		int[] chord0 = {1, 4, 5, 27};
		int[] chord1 = {1, 3, 1, 370};
		int[] chord2 = {1, 3, 7, 1};
		int[] chord3 = {1, 6, 3, 7};
		int[] chord4 = {1, 3, 6, 1};
		int[] chord5 = {170, 57, 1700, 470};
		int[] chord6 = {4, 5, 4, 5};
		int[] chord7 = {5700, 1, 5700, 1};
		int[] chord8 = {1, 4, 7, 5};
		int[] chord9 = {1, 2, 1, 3};
		chords.put(0, chord0);
		chords.put(1, chord1);
		chords.put(2, chord2);
		chords.put(3, chord3);
		chords.put(4, chord4);
		chords.put(5, chord5);
		chords.put(6, chord6);
		chords.put(7, chord7);
		chords.put(8, chord8);
		chords.put(9, chord9);
		Random rand = new Random();
		int decide = rand.nextInt(10);
		return chords.get(decide);
	}
	public ArrayList<ArrayList<Integer>> fearfulBeats() {
		ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
		Random rand = new Random();
		int decide = rand.nextInt(10);
		int[] beats0 = {5, 3, 3, 5, 5, 3, 3, 5, 6, 3};
		int[] beats1 = {4, 1, 5, 5, 5, 4, 1, 5, 5, 5};
		int[] beats2 = {1, 1, 1, 1, 4, 1, 5, 5, 1, 1, 1, 1, 4, 1, 5, 5};
		int[] beats3 = {5, 1, 1, 1, 1, 7, 1, 1, 6, 1, 1, 1, 1, 5};
		int[] beats4 = {3, 3, 3, 3, 4, 1, 5, 3, 3, 4, 1, 7};
		int[] beats5 = {3, 3, 5, 3, 3, 5, 3, 3, 5, 7};
		int[] beats6 = {5, 4, 1, 5, 4, 1, 5, 4, 1, 3, 3, 5};
		int[] beats7 = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
		int[] beats8 = {4, 1, 4, 1, 4, 1, 4, 1, 4, 1, 4, 1, 4, 1, 4, 1};
		int[] beats9 = {6, 3, 6, 3, 6, 3, 6, 3};
		ArrayList <Integer> beats = new ArrayList<>();
		if (decide <5) {
			if (decide == 0) return getFastBarBeats(beats0, 0, 1, 1, 0);
			else if (decide == 1) return getFastBarBeats(beats1, 0, 1, 0, 1);
			else if (decide == 2) return getSlowBarBeats(beats2, 1, 0, 1, 0);
			else if (decide == 3) return getFastBarBeats(beats3, 1, 0, 1, 0);
			else return getSlowBarBeats(beats4, 1, 0, 0, 0);
		}
		else {
			if (decide == 5) return getFastBarBeats(beats5, 0, 1, 0, 0);
			else if (decide == 6) return getFastBarBeats(beats6, 0, 0, 0, 1);
			else if (decide == 7) return getSlowBarBeats(beats7, 0, 1, 1, 0);
			else if (decide == 8) return getFastBarBeats(beats8, 0, 1, 0, 0);
			else return getFastBarBeats(beats9, 0, 0, 1, 0);
		}
	}
	public ArrayList<ArrayList<Integer>> getSlowBarBeats(int[] beats, int one, int two, int three, int four){
		ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
		ArrayList <Integer> firstBarBeats = new ArrayList<>();
        ArrayList <Integer> secondBarBeats = new ArrayList<>();
        ArrayList <Integer> thirdBarBeats = new ArrayList<>();
        ArrayList <Integer> fourthBarBeats = new ArrayList<>();
        int beatsWritten = 0;
        int idx = 0;
        int totalBeatsPerBar = 0;
        int i = 0;
        while (i < beats.length) {
        	if (beats[i] != 8) {
        		totalBeatsPerBar += actualBeats(beats[i]);
        		i++;
        	}
        	else {
        		totalBeatsPerBar += 4;
        		i = i +3;
        	}
        }
        totalBeatsPerBar = totalBeatsPerBar/4;
        while (beatsWritten < totalBeatsPerBar) {
        	if (beats[idx] != 8) {
        		beatsWritten += actualBeats(beats[idx]);
        		firstBarBeats.add(beats[idx]);
        		idx++;
        	}
        	else {
        		beatsWritten += 4;
        		firstBarBeats.add(8);
        		firstBarBeats.add(8);
        		firstBarBeats.add(8);
        		idx += 3;
        	}
        }
        if (one == 0)ret.add(firstBarBeats);
        else ret.add(addingSlowBeats(firstBarBeats, one));
        beatsWritten = 0;
        while (beatsWritten < totalBeatsPerBar) {
        	if (beats[idx] != 8) {
        		beatsWritten += actualBeats(beats[idx]);
        		secondBarBeats.add(beats[idx]);
        		idx++;
        	}
        	else {
        		beatsWritten += 4;
        		secondBarBeats.add(8);
        		secondBarBeats.add(8);
        		secondBarBeats.add(8);
        		idx += 3;
        	}
        }
        beatsWritten = 0;
        if (two == 0) ret.add(secondBarBeats);
        else ret.add(addingSlowBeats(secondBarBeats, two));
        while (beatsWritten < totalBeatsPerBar) {
        	if (beats[idx] != 8) {
        		beatsWritten += actualBeats(beats[idx]);
        		thirdBarBeats.add(beats[idx]);
        		idx++;
        	}
        	else {
        		beatsWritten += 4;
        		thirdBarBeats.add(8);
        		thirdBarBeats.add(8);
        		thirdBarBeats.add(8);
        		idx += 3;
        	}
        	
        }
        if (three == 0) ret.add(thirdBarBeats);
        else ret.add(addingSlowBeats(thirdBarBeats, three));
        while (idx < beats.length) {
        	if (beats[idx] != 8) {
        		fourthBarBeats.add(beats[idx]);
        		idx++;
        	}
        	else {
        		fourthBarBeats.add(8);
        		fourthBarBeats.add(8);
        		fourthBarBeats.add(8);
        		idx += 3;
        	}
        	
        }
        if (four == 0) ret.add(fourthBarBeats);
        else ret.add(addingSlowBeats(fourthBarBeats, four));
		return ret;
	}
	public ArrayList<ArrayList<Integer>> getFastBarBeats(int[] beats, int one, int two, int three, int four){
		ArrayList<ArrayList<Integer>> ret = new ArrayList<>();
		ArrayList <Integer> firstBarBeats = new ArrayList<>();
        ArrayList <Integer> secondBarBeats = new ArrayList<>();
        ArrayList <Integer> thirdBarBeats = new ArrayList<>();
        ArrayList <Integer> fourthBarBeats = new ArrayList<>();
        int beatsWritten = 0;
        int idx = 0;
        int totalBeatsPerBar = 0;
        int i = 0; 
        while (i < beats.length) {
        	if (beats[i] != 8) {
        		totalBeatsPerBar += actualBeats(beats[i]);
        		i++;
        	}
        	else {
        		totalBeatsPerBar += 4;
        		i = i +3;
        	}
        }
        totalBeatsPerBar = totalBeatsPerBar/4;
        while (beatsWritten < totalBeatsPerBar) {       	
        	if (beats[idx] != 8) {
        		beatsWritten += actualBeats(beats[idx]);
        		firstBarBeats.add(beats[idx]);
        		idx++;
        	}
        	else {
        		beatsWritten += 4;
        		firstBarBeats.add(8);
        		firstBarBeats.add(8);
        		firstBarBeats.add(8);
        		idx += 3;
        	}
        	
        }
        if (one == 0)ret.add(firstBarBeats);
        else ret.add(addingFastBeats(firstBarBeats, one));
        beatsWritten = 0;
        while (beatsWritten < totalBeatsPerBar) {
        	if (beats[idx] != 8) {
        		beatsWritten += actualBeats(beats[idx]);
        		secondBarBeats.add(beats[idx]);
        		idx++;
        	}
        	else {
        		beatsWritten += 4;
        		secondBarBeats.add(8);
        		secondBarBeats.add(8);
        		secondBarBeats.add(8);
        		idx += 3;
        	}
        }
        beatsWritten = 0;
        if (two == 0) ret.add(secondBarBeats);
        else ret.add(addingFastBeats(secondBarBeats, two));
        while (beatsWritten < totalBeatsPerBar) {
        	if (beats[idx] != 8) {
        		beatsWritten += actualBeats(beats[idx]);
        		thirdBarBeats.add(beats[idx]);
        		idx++;
        	}
        	else {
        		beatsWritten += 4;
        		thirdBarBeats.add(8);
        		thirdBarBeats.add(8);
        		thirdBarBeats.add(8);
        		idx += 3;
        	}
        	
        }
        if (three == 0) ret.add(thirdBarBeats);
        else ret.add(addingFastBeats(thirdBarBeats, three));
        while (idx < beats.length) {
        	if (beats[idx] != 8) {
        		fourthBarBeats.add(beats[idx]);
        		idx++;
        	}
        	else {
        		fourthBarBeats.add(8);
        		fourthBarBeats.add(8);
        		fourthBarBeats.add(8);
        		idx += 3;
        	}
        	
        }
        if (four == 0) ret.add(fourthBarBeats);
        else ret.add(addingFastBeats(fourthBarBeats, four));
		return ret;
	}
	
	public String[] calmingGenerator(String scale, int mkov) throws FileNotFoundException{
		//updated
		String file1 = "01. Thompson_Stopping by woods on a snowy evening.txt"; //A flat
		String file2 = "02. Debussy_The snow is dancing.txt"; //F
        String file3 = "03. Korngold_Der Schneemann.txt"; //A
        String file4 = "04. Schubert_Serenade.txt";
        String file5 = "05. Schubert_Fantasia.txt";
        String file6 = "06. Sibelius_Violin Concerto in d minor.txt";
        String file7 = "07. Khachaturian_A little song.txt";
        String file8 = "08. Yiruma_Kiss the rain.txt";
        String file9 = "09. Beethoven_Pathetique 3rd movement.txt";
        String file10 = "10. Pachelbel _ Canon in D.txt";
        String file11 = "11. Kondo_Shiek.txt";
        String file12 = "12. Schubert_Ave Maria.txt";
        String file13 = "13. Chopin_Nocturne in E flat major.txt";
        String file14 = "14. Schindler's list theme song.txt";
        String file15 = "15. Beethoven_Pathetique 2nd movement.txt";
        String file16 = "16. Into the West.txt";
        String file17 = "17. Mad World.txt";
        String file18 = "18. My heart will go on.txt";
        String file19 = "19. All I ask of you.txt";
        String file20 = "20. Grieg_In the hall of the mountain king.txt";
        String file21 = "21. Bach_Toccata in D minor.txt";
        String file22 = "22. Chopin_Funeral march Op.35.txt";
        String file23 = "23. Lalaland.txt";
        String file24 = "24. Clayderman_Dream wedding.txt";
        String file25 = "25. Chopin_Etude Op.10 No.3.txt";
        String file26 = "26. Mozart_Twinkle twinkle little star.txt";
        String file27 = "27. Mozart_Serenade No.13.txt";
        String file28 = "28. Beethoven_For Alice.txt";
        String file29 = "29. A Maiden's Prayer.txt";
        String file30 = "30. Brahms_Lullaby.txt";
        String file31 = "31. Clayderman_A letter to mom.txt";
        String file32 = "32. Music of the night.txt";
        String file33 = "33. A whole new world.txt";
        String file34 = "34. Yiruma_River flows in you.txt";
        String file35 = "35. Haydn_Surprise Symphony.txt";
        String file36 = "36. Mozart_Rondo alla Turca.txt";
        String file37 = "37. Liszt_Hungarian Rhapsody No.2.txt";
        String file38 = "38. Lennon_Imagine.txt";
        String file39 = "39. Burgmuller_Arabesque.txt";
        String file40 = "40. Adele_Someone like you.txt";
        String file41 = "41. Mozart_Rondo No.3 in a minor.txt";
        String file42 = "42. Schubert_Impromptu Op.90 No.2.txt";
        String file43 = "43. Tchaikovsky_Song of the lark.txt";
        String file44 = "44. Mozart_Fantasia in d minor.txt";
        String file45 = "45. Lost in the woods.txt";
        String file46 = "46. A little hapiness.txt";
        String file47 = "47. Brahms_Intermezzo.txt";
        String file48 = "48. Jingle Bells.txt";
        String file49 = "49. Everytime we touch.txt";
        String file50 = "50. Last Christmas.txt";
        HashMap <Integer, int[][]> database = new HashMap<>();
		HashMap<Integer, int[]> simpleDatabase = new HashMap<>();
        ReadingFiles readfile = new ReadingFiles();
        ArrayList<ArrayList<Integer>> arrfile1 = readfile.transpose(file1, "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile2 = readfile.transpose(file2, "F", scale);
        ArrayList <ArrayList<Integer>>arrfile3 = readfile.transpose(file3, "A", scale);
        ArrayList<ArrayList<Integer>> arrfile4 = readfile.transpose(file4, "F", scale);
        ArrayList<ArrayList<Integer>> arrfile5 = readfile.transpose(file5, "A flat", scale);
        ArrayList <ArrayList<Integer>> arrfile6 = readfile.transpose(file6, "F", scale);
        ArrayList <ArrayList<Integer>> arrfile7 = readfile.transpose(file7, "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile8 = readfile.transpose(file8,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile9 = readfile.transpose(file9,  "c", scale);
        ArrayList<ArrayList<Integer>> arrfile10 = readfile.transpose(file10,  "D", scale);
        ArrayList<ArrayList<Integer>> arrfile11 = readfile.transpose(file11,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile12 = readfile.transpose(file12,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile13 = readfile.transpose(file13,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile14 = readfile.transpose(file14,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile15 = readfile.transpose(file15,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile16 = readfile.transpose(file16,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile17 = readfile.transpose(file17,  "f", scale);
        ArrayList<ArrayList<Integer>> arrfile18 = readfile.transpose(file18,  "E", scale);
        ArrayList<ArrayList<Integer>> arrfile19 = readfile.transpose(file19,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile20 = readfile.transpose(file20,  "b", scale);
        ArrayList<ArrayList<Integer>> arrfile21 = readfile.transpose(file21,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile22 = readfile.transpose(file22,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile23 = readfile.transpose(file23,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile24 = readfile.transpose(file24,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile25 = readfile.transpose(file25,  "E", scale);
        ArrayList<ArrayList<Integer>> arrfile26 = readfile.transpose(file26,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile27 = readfile.transpose(file27,  "G", scale);
        ArrayList<ArrayList<Integer>> arrfile28 = readfile.transpose(file28,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile29 = readfile.transpose(file29,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile30 = readfile.transpose(file30,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile31 = readfile.transpose(file31,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile32 = readfile.transpose(file32,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile33 = readfile.transpose(file33,  "D", scale);
        ArrayList<ArrayList<Integer>> arrfile34 = readfile.transpose(file34,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile35 = readfile.transpose(file35,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile36 = readfile.transpose(file36,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile37 = readfile.transpose(file37,  "c sharp", scale);
        ArrayList<ArrayList<Integer>> arrfile38 = readfile.transpose(file38,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile39 = readfile.transpose(file39,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile40 = readfile.transpose(file40,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile41 = readfile.transpose(file41,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile42 = readfile.transpose(file42,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile43 = readfile.transpose(file43,  "G", scale);
        ArrayList<ArrayList<Integer>> arrfile44 = readfile.transpose(file44,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile45 = readfile.transpose(file45,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile46 = readfile.transpose(file46,  "F", scale);
        ArrayList<ArrayList<Integer>> arrfile47 = readfile.transpose(file47,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile48 = readfile.transpose(file48,  "F", scale);
        ArrayList<ArrayList<Integer>> arrfile49 = readfile.transpose(file49,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile50 = readfile.transpose(file50,  "D", scale);
        readfile.buildSimpleDatabase(arrfile1, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile2, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile3, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile4, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile5, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile6, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile7, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile8,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile9,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile10,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile11,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile12,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile13,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile14,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile15,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile16,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile17,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile18,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile19,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile20,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile21,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile22,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile23,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile24,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile25,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile26,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile27,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile28,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile29,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile30,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile31,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile32,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile33,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile34,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile35,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile36,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile37,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile38,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile39,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile40,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile41,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile42,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile43,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile44,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile45,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile46,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile47,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile48,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile49,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile50,  simpleDatabase);
        BuildMarkov buildingmarkov = new BuildMarkov();
        HashMap<String, int[]> markovDatabase = new HashMap<>();
        buildingmarkov.simpleBuildMarkov(mkov, arrfile1, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile2, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile3, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile4, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile5, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile6, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile7, markovDatabase, simpleDatabase); 
        buildingmarkov.simpleBuildMarkov(mkov, arrfile8, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile9, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile10, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile11, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile12, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile13, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile14, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile15, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile16, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile17, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile18, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile19, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile20, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile21, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile22, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile23, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile24, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile25, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile26, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile27, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile28, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile29, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile30, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile31, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile32, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile33, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile34, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile35, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile36, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile37, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile38, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile39, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile40, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile41, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile42, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile43, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile44, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile45, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile46, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile47, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile48, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile49, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile50, markovDatabase, simpleDatabase);
        //updated
        melodyGenerator generate = new melodyGenerator();
        ArrayList<ArrayList<Integer>> chosenBeats = generate.calmingBeats();
        int [] chosenChords = generate.calmingChordProgression();
        //int [] chosenChords = {170, 2700, 37, 5};
        ArrayList<Integer> generatedMelody = generator(simpleDatabase,  markovDatabase, chosenBeats,  mkov,  scale, chosenChords[0], chosenChords[1], chosenChords[2], chosenChords[3]);  
        SoundPlayer player = new SoundPlayer();
        return player.calmingPlay(generatedMelody, isFlatKey(scale));
	}
	
	public String[] sadGenerator(String scale, int mkov) throws FileNotFoundException{
		//updated
		String file1 = "01. Thompson_Stopping by woods on a snowy evening.txt"; //A flat
		String file2 = "02. Debussy_The snow is dancing.txt"; //F
        String file3 = "03. Korngold_Der Schneemann.txt"; //A
        String file4 = "04. Schubert_Serenade.txt";
        String file5 = "05. Schubert_Fantasia.txt";
        String file6 = "06. Sibelius_Violin Concerto in d minor.txt";
        String file7 = "07. Khachaturian_A little song.txt";
        String file8 = "08. Yiruma_Kiss the rain.txt";
        String file9 = "09. Beethoven_Pathetique 3rd movement.txt";
        String file10 = "10. Pachelbel _ Canon in D.txt";
        String file11 = "11. Kondo_Shiek.txt";
        String file12 = "12. Schubert_Ave Maria.txt";
        String file13 = "13. Chopin_Nocturne in E flat major.txt";
        String file14 = "14. Schindler's list theme song.txt";
        String file15 = "15. Beethoven_Pathetique 2nd movement.txt";
        String file16 = "16. Into the West.txt";
        String file17 = "17. Mad World.txt";
        String file18 = "18. My heart will go on.txt";
        String file19 = "19. All I ask of you.txt";
        String file20 = "20. Grieg_In the hall of the mountain king.txt";
        String file21 = "21. Bach_Toccata in D minor.txt";
        String file22 = "22. Chopin_Funeral march Op.35.txt";
        String file23 = "23. Lalaland.txt";
        String file24 = "24. Clayderman_Dream wedding.txt";
        String file25 = "25. Chopin_Etude Op.10 No.3.txt";
        String file26 = "26. Mozart_Twinkle twinkle little star.txt";
        String file27 = "27. Mozart_Serenade No.13.txt";
        String file28 = "28. Beethoven_For Alice.txt";
        String file29 = "29. A Maiden's Prayer.txt";
        String file30 = "30. Brahms_Lullaby.txt";
        String file31 = "31. Clayderman_A letter to mom.txt";
        String file32 = "32. Music of the night.txt";
        String file33 = "33. A whole new world.txt";
        String file34 = "34. Yiruma_River flows in you.txt";
        String file35 = "35. Haydn_Surprise Symphony.txt";
        String file36 = "36. Mozart_Rondo alla Turca.txt";
        String file37 = "37. Liszt_Hungarian Rhapsody No.2.txt";
        String file38 = "38. Lennon_Imagine.txt";
        String file39 = "39. Burgmuller_Arabesque.txt";
        String file40 = "40. Adele_Someone like you.txt";
        String file41 = "41. Mozart_Rondo No.3 in a minor.txt";
        String file42 = "42. Schubert_Impromptu Op.90 No.2.txt";
        String file43 = "43. Tchaikovsky_Song of the lark.txt";
        String file44 = "44. Mozart_Fantasia in d minor.txt";
        String file45 = "45. Lost in the woods.txt";
        String file46 = "46. A little hapiness.txt";
        String file47 = "47. Brahms_Intermezzo.txt";
        String file48 = "48. Jingle Bells.txt";
        String file49 = "49. Everytime we touch.txt";
        String file50 = "50. Last Christmas.txt";
        HashMap <Integer, int[][]> database = new HashMap<>();
		HashMap<Integer, int[]> simpleDatabase = new HashMap<>();
        ReadingFiles readfile = new ReadingFiles();
        ArrayList<ArrayList<Integer>> arrfile1 = readfile.transpose(file1, "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile2 = readfile.transpose(file2, "F", scale);
        ArrayList <ArrayList<Integer>>arrfile3 = readfile.transpose(file3, "A", scale);
        ArrayList<ArrayList<Integer>> arrfile4 = readfile.transpose(file4, "F", scale);
        ArrayList<ArrayList<Integer>> arrfile5 = readfile.transpose(file5, "A flat", scale);
        ArrayList <ArrayList<Integer>> arrfile6 = readfile.transpose(file6, "F", scale);
        ArrayList <ArrayList<Integer>> arrfile7 = readfile.transpose(file7, "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile8 = readfile.transpose(file8,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile9 = readfile.transpose(file9,  "c", scale);
        ArrayList<ArrayList<Integer>> arrfile10 = readfile.transpose(file10,  "D", scale);
        ArrayList<ArrayList<Integer>> arrfile11 = readfile.transpose(file11,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile12 = readfile.transpose(file12,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile13 = readfile.transpose(file13,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile14 = readfile.transpose(file14,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile15 = readfile.transpose(file15,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile16 = readfile.transpose(file16,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile17 = readfile.transpose(file17,  "f", scale);
        ArrayList<ArrayList<Integer>> arrfile18 = readfile.transpose(file18,  "E", scale);
        ArrayList<ArrayList<Integer>> arrfile19 = readfile.transpose(file19,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile20 = readfile.transpose(file20,  "b", scale);
        ArrayList<ArrayList<Integer>> arrfile21 = readfile.transpose(file21,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile22 = readfile.transpose(file22,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile23 = readfile.transpose(file23,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile24 = readfile.transpose(file24,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile25 = readfile.transpose(file25,  "E", scale);
        ArrayList<ArrayList<Integer>> arrfile26 = readfile.transpose(file26,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile27 = readfile.transpose(file27,  "G", scale);
        ArrayList<ArrayList<Integer>> arrfile28 = readfile.transpose(file28,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile29 = readfile.transpose(file29,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile30 = readfile.transpose(file30,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile31 = readfile.transpose(file31,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile32 = readfile.transpose(file32,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile33 = readfile.transpose(file33,  "D", scale);
        ArrayList<ArrayList<Integer>> arrfile34 = readfile.transpose(file34,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile35 = readfile.transpose(file35,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile36 = readfile.transpose(file36,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile37 = readfile.transpose(file37,  "c sharp", scale);
        ArrayList<ArrayList<Integer>> arrfile38 = readfile.transpose(file38,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile39 = readfile.transpose(file39,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile40 = readfile.transpose(file40,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile41 = readfile.transpose(file41,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile42 = readfile.transpose(file42,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile43 = readfile.transpose(file43,  "G", scale);
        ArrayList<ArrayList<Integer>> arrfile44 = readfile.transpose(file44,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile45 = readfile.transpose(file45,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile46 = readfile.transpose(file46,  "F", scale);
        ArrayList<ArrayList<Integer>> arrfile47 = readfile.transpose(file47,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile48 = readfile.transpose(file48,  "F", scale);
        ArrayList<ArrayList<Integer>> arrfile49 = readfile.transpose(file49,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile50 = readfile.transpose(file50,  "D", scale);
        readfile.buildSimpleDatabase(arrfile1, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile2, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile3, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile4, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile5, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile6, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile7, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile8,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile9,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile10,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile11,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile12,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile13,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile14,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile15,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile16,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile17,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile18,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile19,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile20,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile21,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile22,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile23,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile24,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile25,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile26,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile27,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile28,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile29,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile30,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile31,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile32,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile33,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile34,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile35,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile36,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile37,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile38,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile39,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile40,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile41,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile42,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile43,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile44,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile45,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile46,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile47,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile48,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile49,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile50,  simpleDatabase);
        BuildMarkov buildingmarkov = new BuildMarkov();
        HashMap<String, int[]> markovDatabase = new HashMap<>();
        buildingmarkov.simpleBuildMarkov(mkov, arrfile1, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile2, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile3, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile4, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile5, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile6, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile7, markovDatabase, simpleDatabase); 
        buildingmarkov.simpleBuildMarkov(mkov, arrfile8, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile9, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile10, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile11, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile12, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile13, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile14, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile15, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile16, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile17, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile18, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile19, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile20, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile21, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile22, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile23, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile24, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile25, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile26, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile27, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile28, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile29, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile30, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile31, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile32, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile33, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile34, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile35, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile36, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile37, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile38, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile39, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile40, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile41, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile42, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile43, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile44, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile45, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile46, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile47, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile48, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile49, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile50, markovDatabase, simpleDatabase);
        //updated
        melodyGenerator generate = new melodyGenerator();
        ArrayList<ArrayList<Integer>> chosenBeats = generate.sadBeats();
        int count = 0;
        //int [] chosenChords = {170, 370, 5700, 4};
        //int [] chosenChords = {1, 2, 3, 4};
        int [] chosenChords = generate.sadChordProgression();
        ArrayList<Integer> generatedMelody = generator(simpleDatabase,  markovDatabase, chosenBeats,  2,  scale, chosenChords[0], chosenChords[1], chosenChords[2], chosenChords[3]);  
        SoundPlayer player = new SoundPlayer();      
        return player.sadPlay(generatedMelody, isFlatKey(scale));
	}
	public String[] joyfulGenerator(String scale, int mkov) throws FileNotFoundException{
		//updated
		String file1 = "01. Thompson_Stopping by woods on a snowy evening.txt"; //A flat
		String file2 = "02. Debussy_The snow is dancing.txt"; //F
        String file3 = "03. Korngold_Der Schneemann.txt"; //A
        String file4 = "04. Schubert_Serenade.txt";
        String file5 = "05. Schubert_Fantasia.txt";
        String file6 = "06. Sibelius_Violin Concerto in d minor.txt";
        String file7 = "07. Khachaturian_A little song.txt";
        String file8 = "08. Yiruma_Kiss the rain.txt";
        String file9 = "09. Beethoven_Pathetique 3rd movement.txt";
        String file10 = "10. Pachelbel _ Canon in D.txt";
        String file11 = "11. Kondo_Shiek.txt";
        String file12 = "12. Schubert_Ave Maria.txt";
        String file13 = "13. Chopin_Nocturne in E flat major.txt";
        String file14 = "14. Schindler's list theme song.txt";
        String file15 = "15. Beethoven_Pathetique 2nd movement.txt";
        String file16 = "16. Into the West.txt";
        String file17 = "17. Mad World.txt";
        String file18 = "18. My heart will go on.txt";
        String file19 = "19. All I ask of you.txt";
        String file20 = "20. Grieg_In the hall of the mountain king.txt";
        String file21 = "21. Bach_Toccata in D minor.txt";
        String file22 = "22. Chopin_Funeral march Op.35.txt";
        String file23 = "23. Lalaland.txt";
        String file24 = "24. Clayderman_Dream wedding.txt";
        String file25 = "25. Chopin_Etude Op.10 No.3.txt";
        String file26 = "26. Mozart_Twinkle twinkle little star.txt";
        String file27 = "27. Mozart_Serenade No.13.txt";
        String file28 = "28. Beethoven_For Alice.txt";
        String file29 = "29. A Maiden's Prayer.txt";
        String file30 = "30. Brahms_Lullaby.txt";
        String file31 = "31. Clayderman_A letter to mom.txt";
        String file32 = "32. Music of the night.txt";
        String file33 = "33. A whole new world.txt";
        String file34 = "34. Yiruma_River flows in you.txt";
        String file35 = "35. Haydn_Surprise Symphony.txt";
        String file36 = "36. Mozart_Rondo alla Turca.txt";
        String file37 = "37. Liszt_Hungarian Rhapsody No.2.txt";
        String file38 = "38. Lennon_Imagine.txt";
        String file39 = "39. Burgmuller_Arabesque.txt";
        String file40 = "40. Adele_Someone like you.txt";
        String file41 = "41. Mozart_Rondo No.3 in a minor.txt";
        String file42 = "42. Schubert_Impromptu Op.90 No.2.txt";
        String file43 = "43. Tchaikovsky_Song of the lark.txt";
        String file44 = "44. Mozart_Fantasia in d minor.txt";
        String file45 = "45. Lost in the woods.txt";
        String file46 = "46. A little hapiness.txt";
        String file47 = "47. Brahms_Intermezzo.txt";
        String file48 = "48. Jingle Bells.txt";
        String file49 = "49. Everytime we touch.txt";
        String file50 = "50. Last Christmas.txt";
        HashMap <Integer, int[][]> database = new HashMap<>();
		HashMap<Integer, int[]> simpleDatabase = new HashMap<>();
        ReadingFiles readfile = new ReadingFiles();
        ArrayList<ArrayList<Integer>> arrfile1 = readfile.transpose(file1, "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile2 = readfile.transpose(file2, "F", scale);
        ArrayList <ArrayList<Integer>>arrfile3 = readfile.transpose(file3, "A", scale);
        ArrayList<ArrayList<Integer>> arrfile4 = readfile.transpose(file4, "F", scale);
        ArrayList<ArrayList<Integer>> arrfile5 = readfile.transpose(file5, "A flat", scale);
        ArrayList <ArrayList<Integer>> arrfile6 = readfile.transpose(file6, "F", scale);
        ArrayList <ArrayList<Integer>> arrfile7 = readfile.transpose(file7, "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile8 = readfile.transpose(file8,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile9 = readfile.transpose(file9,  "c", scale);
        ArrayList<ArrayList<Integer>> arrfile10 = readfile.transpose(file10,  "D", scale);
        ArrayList<ArrayList<Integer>> arrfile11 = readfile.transpose(file11,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile12 = readfile.transpose(file12,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile13 = readfile.transpose(file13,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile14 = readfile.transpose(file14,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile15 = readfile.transpose(file15,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile16 = readfile.transpose(file16,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile17 = readfile.transpose(file17,  "f", scale);
        ArrayList<ArrayList<Integer>> arrfile18 = readfile.transpose(file18,  "E", scale);
        ArrayList<ArrayList<Integer>> arrfile19 = readfile.transpose(file19,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile20 = readfile.transpose(file20,  "b", scale);
        ArrayList<ArrayList<Integer>> arrfile21 = readfile.transpose(file21,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile22 = readfile.transpose(file22,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile23 = readfile.transpose(file23,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile24 = readfile.transpose(file24,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile25 = readfile.transpose(file25,  "E", scale);
        ArrayList<ArrayList<Integer>> arrfile26 = readfile.transpose(file26,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile27 = readfile.transpose(file27,  "G", scale);
        ArrayList<ArrayList<Integer>> arrfile28 = readfile.transpose(file28,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile29 = readfile.transpose(file29,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile30 = readfile.transpose(file30,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile31 = readfile.transpose(file31,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile32 = readfile.transpose(file32,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile33 = readfile.transpose(file33,  "D", scale);
        ArrayList<ArrayList<Integer>> arrfile34 = readfile.transpose(file34,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile35 = readfile.transpose(file35,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile36 = readfile.transpose(file36,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile37 = readfile.transpose(file37,  "c sharp", scale);
        ArrayList<ArrayList<Integer>> arrfile38 = readfile.transpose(file38,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile39 = readfile.transpose(file39,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile40 = readfile.transpose(file40,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile41 = readfile.transpose(file41,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile42 = readfile.transpose(file42,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile43 = readfile.transpose(file43,  "G", scale);
        ArrayList<ArrayList<Integer>> arrfile44 = readfile.transpose(file44,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile45 = readfile.transpose(file45,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile46 = readfile.transpose(file46,  "F", scale);
        ArrayList<ArrayList<Integer>> arrfile47 = readfile.transpose(file47,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile48 = readfile.transpose(file48,  "F", scale);
        ArrayList<ArrayList<Integer>> arrfile49 = readfile.transpose(file49,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile50 = readfile.transpose(file50,  "D", scale);
        readfile.buildSimpleDatabase(arrfile1, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile2, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile3, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile4, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile5, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile6, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile7, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile8,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile9,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile10,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile11,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile12,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile13,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile14,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile15,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile16,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile17,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile18,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile19,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile20,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile21,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile22,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile23,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile24,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile25,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile26,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile27,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile28,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile29,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile30,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile31,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile32,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile33,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile34,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile35,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile36,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile37,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile38,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile39,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile40,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile41,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile42,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile43,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile44,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile45,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile46,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile47,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile48,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile49,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile50,  simpleDatabase);
        BuildMarkov buildingmarkov = new BuildMarkov();
        HashMap<String, int[]> markovDatabase = new HashMap<>();
        buildingmarkov.simpleBuildMarkov(mkov, arrfile1, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile2, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile3, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile4, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile5, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile6, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile7, markovDatabase, simpleDatabase); 
        buildingmarkov.simpleBuildMarkov(mkov, arrfile8, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile9, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile10, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile11, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile12, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile13, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile14, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile15, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile16, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile17, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile18, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile19, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile20, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile21, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile22, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile23, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile24, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile25, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile26, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile27, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile28, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile29, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile30, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile31, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile32, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile33, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile34, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile35, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile36, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile37, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile38, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile39, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile40, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile41, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile42, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile43, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile44, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile45, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile46, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile47, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile48, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile49, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile50, markovDatabase, simpleDatabase);
        //updated
        melodyGenerator generate = new melodyGenerator();
        ArrayList<ArrayList<Integer>> chosenBeats = generate.joyfulBeats();
        int [] chosenChords = generate.joyfulChordProgression();
        //int [] chosenChords = {1, 27, 4, 5};
        ArrayList<Integer> generatedMelody = generator(simpleDatabase,  markovDatabase, chosenBeats,  mkov,  scale, chosenChords[0], chosenChords[1], chosenChords[2], chosenChords[3]);  
        SoundPlayer player = new SoundPlayer();
        return player.joyfulPlay(generatedMelody, isFlatKey(scale));
	}
	
	public String[] fearfulGenerator(String scale, int mkov) throws FileNotFoundException{
		//updated
		String file1 = "01. Thompson_Stopping by woods on a snowy evening.txt"; //A flat
		String file2 = "02. Debussy_The snow is dancing.txt"; //F
        String file3 = "03. Korngold_Der Schneemann.txt"; //A
        String file4 = "04. Schubert_Serenade.txt";
        String file5 = "05. Schubert_Fantasia.txt";
        String file6 = "06. Sibelius_Violin Concerto in d minor.txt";
        String file7 = "07. Khachaturian_A little song.txt";
        String file8 = "08. Yiruma_Kiss the rain.txt";
        String file9 = "09. Beethoven_Pathetique 3rd movement.txt";
        String file10 = "10. Pachelbel _ Canon in D.txt";
        String file11 = "11. Kondo_Shiek.txt";
        String file12 = "12. Schubert_Ave Maria.txt";
        String file13 = "13. Chopin_Nocturne in E flat major.txt";
        String file14 = "14. Schindler's list theme song.txt";
        String file15 = "15. Beethoven_Pathetique 2nd movement.txt";
        String file16 = "16. Into the West.txt";
        String file17 = "17. Mad World.txt";
        String file18 = "18. My heart will go on.txt";
        String file19 = "19. All I ask of you.txt";
        String file20 = "20. Grieg_In the hall of the mountain king.txt";
        String file21 = "21. Bach_Toccata in D minor.txt";
        String file22 = "22. Chopin_Funeral march Op.35.txt";
        String file23 = "23. Lalaland.txt";
        String file24 = "24. Clayderman_Dream wedding.txt";
        String file25 = "25. Chopin_Etude Op.10 No.3.txt";
        String file26 = "26. Mozart_Twinkle twinkle little star.txt";
        String file27 = "27. Mozart_Serenade No.13.txt";
        String file28 = "28. Beethoven_For Alice.txt";
        String file29 = "29. A Maiden's Prayer.txt";
        String file30 = "30. Brahms_Lullaby.txt";
        String file31 = "31. Clayderman_A letter to mom.txt";
        String file32 = "32. Music of the night.txt";
        String file33 = "33. A whole new world.txt";
        String file34 = "34. Yiruma_River flows in you.txt";
        String file35 = "35. Haydn_Surprise Symphony.txt";
        String file36 = "36. Mozart_Rondo alla Turca.txt";
        String file37 = "37. Liszt_Hungarian Rhapsody No.2.txt";
        String file38 = "38. Lennon_Imagine.txt";
        String file39 = "39. Burgmuller_Arabesque.txt";
        String file40 = "40. Adele_Someone like you.txt";
        String file41 = "41. Mozart_Rondo No.3 in a minor.txt";
        String file42 = "42. Schubert_Impromptu Op.90 No.2.txt";
        String file43 = "43. Tchaikovsky_Song of the lark.txt";
        String file44 = "44. Mozart_Fantasia in d minor.txt";
        String file45 = "45. Lost in the woods.txt";
        String file46 = "46. A little hapiness.txt";
        String file47 = "47. Brahms_Intermezzo.txt";
        String file48 = "48. Jingle Bells.txt";
        String file49 = "49. Everytime we touch.txt";
        String file50 = "50. Last Christmas.txt";
        HashMap <Integer, int[][]> database = new HashMap<>();
		HashMap<Integer, int[]> simpleDatabase = new HashMap<>();
        ReadingFiles readfile = new ReadingFiles();
        ArrayList<ArrayList<Integer>> arrfile1 = readfile.transpose(file1, "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile2 = readfile.transpose(file2, "F", scale);
        ArrayList <ArrayList<Integer>>arrfile3 = readfile.transpose(file3, "A", scale);
        ArrayList<ArrayList<Integer>> arrfile4 = readfile.transpose(file4, "F", scale);
        ArrayList<ArrayList<Integer>> arrfile5 = readfile.transpose(file5, "A flat", scale);
        ArrayList <ArrayList<Integer>> arrfile6 = readfile.transpose(file6, "F", scale);
        ArrayList <ArrayList<Integer>> arrfile7 = readfile.transpose(file7, "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile8 = readfile.transpose(file8,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile9 = readfile.transpose(file9,  "c", scale);
        ArrayList<ArrayList<Integer>> arrfile10 = readfile.transpose(file10,  "D", scale);
        ArrayList<ArrayList<Integer>> arrfile11 = readfile.transpose(file11,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile12 = readfile.transpose(file12,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile13 = readfile.transpose(file13,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile14 = readfile.transpose(file14,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile15 = readfile.transpose(file15,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile16 = readfile.transpose(file16,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile17 = readfile.transpose(file17,  "f", scale);
        ArrayList<ArrayList<Integer>> arrfile18 = readfile.transpose(file18,  "E", scale);
        ArrayList<ArrayList<Integer>> arrfile19 = readfile.transpose(file19,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile20 = readfile.transpose(file20,  "b", scale);
        ArrayList<ArrayList<Integer>> arrfile21 = readfile.transpose(file21,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile22 = readfile.transpose(file22,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile23 = readfile.transpose(file23,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile24 = readfile.transpose(file24,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile25 = readfile.transpose(file25,  "E", scale);
        ArrayList<ArrayList<Integer>> arrfile26 = readfile.transpose(file26,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile27 = readfile.transpose(file27,  "G", scale);
        ArrayList<ArrayList<Integer>> arrfile28 = readfile.transpose(file28,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile29 = readfile.transpose(file29,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile30 = readfile.transpose(file30,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile31 = readfile.transpose(file31,  "B flat", scale);
        ArrayList<ArrayList<Integer>> arrfile32 = readfile.transpose(file32,  "D flat", scale);
        ArrayList<ArrayList<Integer>> arrfile33 = readfile.transpose(file33,  "D", scale);
        ArrayList<ArrayList<Integer>> arrfile34 = readfile.transpose(file34,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile35 = readfile.transpose(file35,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile36 = readfile.transpose(file36,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile37 = readfile.transpose(file37,  "c sharp", scale);
        ArrayList<ArrayList<Integer>> arrfile38 = readfile.transpose(file38,  "C", scale);
        ArrayList<ArrayList<Integer>> arrfile39 = readfile.transpose(file39,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile40 = readfile.transpose(file40,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile41 = readfile.transpose(file41,  "a", scale);
        ArrayList<ArrayList<Integer>> arrfile42 = readfile.transpose(file42,  "E flat", scale);
        ArrayList<ArrayList<Integer>> arrfile43 = readfile.transpose(file43,  "G", scale);
        ArrayList<ArrayList<Integer>> arrfile44 = readfile.transpose(file44,  "d", scale);
        ArrayList<ArrayList<Integer>> arrfile45 = readfile.transpose(file45,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile46 = readfile.transpose(file46,  "F", scale);
        ArrayList<ArrayList<Integer>> arrfile47 = readfile.transpose(file47,  "A", scale);
        ArrayList<ArrayList<Integer>> arrfile48 = readfile.transpose(file48,  "F", scale);
        ArrayList<ArrayList<Integer>> arrfile49 = readfile.transpose(file49,  "A flat", scale);
        ArrayList<ArrayList<Integer>> arrfile50 = readfile.transpose(file50,  "D", scale);
        readfile.buildSimpleDatabase(arrfile1, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile2, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile3, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile4, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile5, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile6, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile7, simpleDatabase); 
        readfile.buildSimpleDatabase(arrfile8,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile9,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile10,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile11,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile12,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile13,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile14,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile15,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile16,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile17,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile18,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile19,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile20,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile21,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile22,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile23,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile24,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile25,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile26,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile27,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile28,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile29,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile30,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile31,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile32,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile33,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile34,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile35,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile36,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile37,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile38,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile39,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile40,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile41,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile42,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile43,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile44,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile45,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile46,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile47,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile48,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile49,  simpleDatabase);
        readfile.buildSimpleDatabase(arrfile50,  simpleDatabase);
        BuildMarkov buildingmarkov = new BuildMarkov();
        HashMap<String, int[]> markovDatabase = new HashMap<>();
        buildingmarkov.simpleBuildMarkov(mkov, arrfile1, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile2, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile3, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile4, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile5, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile6, markovDatabase, simpleDatabase);  
        buildingmarkov.simpleBuildMarkov(mkov, arrfile7, markovDatabase, simpleDatabase); 
        buildingmarkov.simpleBuildMarkov(mkov, arrfile8, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile9, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile10, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile11, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile12, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile13, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile14, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile15, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile16, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile17, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile18, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile19, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile20, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile21, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile22, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile23, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile24, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile25, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile26, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile27, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile28, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile29, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile30, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile31, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile32, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile33, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile34, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile35, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile36, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile37, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile38, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile39, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile40, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile41, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile42, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile43, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile44, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile45, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile46, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile47, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile48, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile49, markovDatabase, simpleDatabase);
        buildingmarkov.simpleBuildMarkov(mkov, arrfile50, markovDatabase, simpleDatabase);
        //updated
        melodyGenerator generate = new melodyGenerator();
        ArrayList<ArrayList<Integer>> chosenBeats = generate.fearfulBeats();
        int [] chosenChords = generate.fearfulChordProgression();
        ArrayList<Integer> generatedMelody = generator(simpleDatabase,  markovDatabase, chosenBeats,  mkov,  scale, chosenChords[0], chosenChords[1], chosenChords[2], chosenChords[3]);  
        SoundPlayer player = new SoundPlayer();
        return player.fearfulPlay(generatedMelody, isFlatKey(scale));
	}
	
	public static String[] webplay (String emotion, String scale) throws FileNotFoundException {
		melodyGenerator generator = new melodyGenerator();
		if (emotion.equals("sad")) {
			return generator.sadGenerator(scale, 2);
		} else if (emotion.equals("calm")) {
			return generator.calmingGenerator(scale, 2);
		} else if (emotion.equals("joyful")) {
			return generator.joyfulGenerator(scale, 2);
		} else if (emotion.equals("fearful")) {
			return generator.fearfulGenerator(scale, 2);
		}
		return null;
	}
	
	public static void main (String [] args) throws FileNotFoundException {
		melodyGenerator generator = new melodyGenerator();
		//System.out.println(generator.calmingGenerator("C", 2));
		//System.out.println(generator.sadGenerator("a", 2));
		//System.out.println(generator.joyfulGenerator("C", 2));
		System.out.println(Arrays.toString(generator.fearfulGenerator("c", 2)));
		
	}
}