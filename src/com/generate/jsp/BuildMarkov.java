package com.generate.jsp;
import java.util.*;
public class BuildMarkov {
	public void buildMarkov (int order, ArrayList <ArrayList<Integer>> data, HashMap <String, int[]> markovDatabase, HashMap<Integer, int[][]> database){
		for (ArrayList<Integer> melody: data){
            for (int i = 0; i < melody.size() - (order*2); i +=2){
                String notegram = "";
                int k = i;
                while (notegram.length() < (order*2)){
                    if (melody.get(k) <10) notegram += ("0" + String.valueOf(melody.get(k)));
                    else notegram += String.valueOf(melody.get(k));
                    k += 2;
                }
                //System.out.println(notegram.toString());                
                
                if (k!= melody.size()-2) {
                	markovDatabase.putIfAbsent(notegram, new int[26]);
                	markovDatabase.get(notegram)[melody.get(k)] +=1;
                }
                else {
                	if (database.get(melody.get(k - 2))[9][melody.get(k)]!=0) {
                		markovDatabase.putIfAbsent(notegram, new int[26]);
                    	markovDatabase.get(notegram)[melody.get(k)] +=1;
                	}
                }
            }
		}
    }
	public void simpleBuildMarkov  (int order, ArrayList<ArrayList<Integer>> data, HashMap<String, int[]> markovDatabase, HashMap<Integer, int[]> simpleDatabase){
		for (ArrayList<Integer> melody: data){
            for (int i = 0; i < melody.size() - (order*2); i +=2){
                String notegram = "";
                int k = i;
                while (notegram.length() < (order*2)){
                    if (melody.get(k) <10) notegram += ("0" + String.valueOf(melody.get(k)));
                    else notegram += String.valueOf(melody.get(k));
                    k += 2;
                }
                //System.out.println(notegram.toString());                
                
                if (k!= melody.size()-2) {
                	markovDatabase.putIfAbsent(notegram, new int[26]);
                	markovDatabase.get(notegram)[melody.get(k)] +=1;
                }
                else {
                	if (simpleDatabase.get(melody.get(k - 2))[melody.get(k)]!=0) {
                		markovDatabase.putIfAbsent(notegram, new int[26]);
                    	markovDatabase.get(notegram)[melody.get(k)] +=1;
                	}
                }
            }
		}
	}
    public static void main (String[] args){
    	System.out.println("I'm running BuildMarkov");
        int [] array1 = {1, 2, 2, 2, 3, 2, 4, 2, 5, 2, 6, 2, 7, 2};
        ArrayList <Integer> listtest = new ArrayList (Arrays.asList(array1));
        ArrayList <ArrayList<Integer>> testdata = new ArrayList<>();
        testdata.add(listtest);
        testdata.add(listtest);
        BuildMarkov test = new BuildMarkov();
        HashMap<String, int[]> markovDatabase = new HashMap <>();
        //test.buildMarkov(2, testdata, markovDatabase);
    }

}