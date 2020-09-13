package com.generate.jsp;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.player.Player;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.sound.midi.Sequence;
public class SoundPlayer {
	public String[] calmingPlay (ArrayList <Integer>  melody, boolean isFlat){
		String[] ret = new String[2];
		ArrayList<String> notesWithBeats = new ArrayList<>();
		ArrayList<String> notesString = new ArrayList<>();
		for (int i = 0; i < melody.size() - 1; i+=2) {
			String note = getNote(melody.get(i)) + getBeat(melody.get(i+1));
			String stringNote = "";
			if (isFlat) stringNote = getStringNoteFlat(melody.get(i)) + " " + getStringBeat(melody.get(i+1));
			else stringNote = getStringNoteSharp(melody.get(i)) + " " + getStringBeat(melody.get(i+1));
            notesWithBeats.add(note);
            notesString.add(stringNote);
		}
		
		Player player = new Player();
		Sequence seq = player.getSequence(String.join(" ", notesWithBeats));
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// Try to save music to ByteArrayOutputStream
		try {
			MidiFileManager.save(seq, out);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
        	System.out.println("Fail to save to OutputStream");
			e1.printStackTrace();
		}
		
		// Try to convert ByteArrayOutputStream to byte[]
		byte[] bytes = out.toByteArray();
		
		String encoded = Base64.getEncoder().encodeToString(bytes);
        ret[0] = String.join(" ", notesString);
        ret[1] = encoded;
        return ret;
    }
	
	public String[] sadPlay(ArrayList<Integer> melody, boolean isFlat) {
		String[] ret = new String[2];
		ArrayList<String> notesWithBeats = new ArrayList<>();
		ArrayList<String> notesString = new ArrayList<>();
		for (int i = 0; i < melody.size() - 1; i+=2) {
			String note = getNote(melody.get(i)) + getBeat(melody.get(i+1));
			String stringNote = "";
			if (isFlat) stringNote = getStringNoteFlat(melody.get(i)) + " " + getStringBeat(melody.get(i+1));
			else stringNote = getStringNoteSharp(melody.get(i)) + " " + getStringBeat(melody.get(i+1));
            notesWithBeats.add(note);
            notesString.add(stringNote);
		}
		
		Player player = new Player();
		Sequence seq = player.getSequence(String.join(" ", notesWithBeats));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		// Try to save music to ByteArrayOutputStream
		try {
			MidiFileManager.save(seq, out);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
        	System.out.println("Fail to save to OutputStream");
			e1.printStackTrace();
		}
		
		// Try to convert ByteArrayOutputStream to byte[]
		byte[] bytes = out.toByteArray();
		
		String encoded = Base64.getEncoder().encodeToString(bytes);
        ret[0] = String.join(" ", notesString);
        ret[1] = encoded;
        return ret;
	}
	
	public String[] joyfulPlay(ArrayList<Integer> melody, boolean isFlat) {
		String[] ret = new String[2];
		ArrayList<String> notesWithBeats = new ArrayList<>();
		ArrayList<String> notesString = new ArrayList<>();
		for (int i = 0; i < melody.size() - 1; i+=2) {
			String note = getNote(melody.get(i)) + getBeat(melody.get(i+1));
			String stringNote = "";
			if (isFlat) stringNote = getStringNoteFlat(melody.get(i)) + " " + getStringBeat(melody.get(i+1));
			else stringNote = getStringNoteSharp(melody.get(i)) + " " + getStringBeat(melody.get(i+1));
            notesWithBeats.add(note);
            notesString.add(stringNote);
		}

		Player player = new Player();
		Sequence seq = player.getSequence(String.join(" ", notesWithBeats));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		// Try to save music to ByteArrayOutputStream
		try {
			MidiFileManager.save(seq, out);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
        	System.out.println("Fail to save to OutputStream");
			e1.printStackTrace();
		}
		
		// Try to convert ByteArrayOutputStream to byte[]
		byte[] bytes = out.toByteArray();
		
		String encoded = Base64.getEncoder().encodeToString(bytes);
        ret[0] = String.join(" ", notesString);
        ret[1] = encoded;
        return ret;
	}
	
	public String[] fearfulPlay(ArrayList<Integer> melody, boolean isFlat) {
		String[] ret = new String[2];
		ArrayList<String> notesWithBeats = new ArrayList<>();
		ArrayList<String> notesString = new ArrayList<>();
		for (int i = 0; i < melody.size() - 1; i+=2) {
			String note = getFearfulNote(melody.get(i)) + getBeat(melody.get(i+1));
			String stringNote = "";
			if (isFlat) stringNote = getStringNoteFlat(melody.get(i)) + " " + getStringBeat(melody.get(i+1));
			else stringNote = getStringNoteSharp(melody.get(i)) + " " + getStringBeat(melody.get(i+1));
            notesWithBeats.add(note);
            notesString.add(stringNote);
		}

		Player player = new Player();
		Sequence seq = player.getSequence(String.join(" ", notesWithBeats));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		// Try to save music to ByteArrayOutputStream
		try {
			MidiFileManager.save(seq, out);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
        	System.out.println("Fail to save to OutputStream");
			e1.printStackTrace();
		}
		
		// Try to convert ByteArrayOutputStream to byte[]
		byte[] bytes = out.toByteArray();
		
		String encoded = Base64.getEncoder().encodeToString(bytes);
        ret[0] = String.join(" ", notesString);
        ret[1] = encoded;
        return ret;
	}
	
	public String getFearfulNote(Integer note){
        if (note < 12){
            if (note < 6){
                if (note == 0) return "C4";
                else if (note == 1) return "C#4";
                else if (note == 2) return "D4";
                else if (note ==3) return "D45";
                else if (note == 4) return "E4";
                else return "F4";
            }
            else{
                if (note == 6) return "F#4";
                if (note == 7) return "G4";
                else if (note == 8) return "G#4";
                else if (note == 9) return "A4";
                else if (note ==10) return "A#4";
                else return "B4";
            }
        }
        else{
            if (note < 18){
                if (note == 12) return "C5";
                else if (note == 13) return "C#5";
                else if (note == 14) return "D5";
                else if (note ==15) return "D#5";
                else if (note == 16) return "E5";
                else return "F5";
            }
            else{
                if (note == 18) return "F#5";
                if (note == 19) return "G5";
                else if (note == 20) return "G#5";
                else if (note == 21) return "A5";
                else if (note ==22) return "A#5";
                else if (note ==23) return "B5";
                else if (note == 24) return "C6";
                else return "R";
            }
        }
    }
	
	public String getNote(Integer note){
		if (note < 12){
            if (note < 6){
                if (note == 0) return "C5";
                else if (note == 1) return "C#5";
                else if (note == 2) return "D5";
                else if (note ==3) return "D#5";
                else if (note == 4) return "E5";
                else return "F5";
            }
            else{
                if (note == 6) return "F#5";
                if (note == 7) return "G5";
                else if (note == 8) return "G#5";
                else if (note == 9) return "A5";
                else if (note ==10) return "A#5";
                else return "B5";
            }
        }
        else{
            if (note < 18){
                if (note == 12) return "C6";
                else if (note == 13) return "C#6";
                else if (note == 14) return "D6";
                else if (note ==15) return "D#6";
                else if (note == 16) return "E6";
                else return "F6";
            }
            else{
                if (note == 18) return "F#6";
                if (note == 19) return "G6";
                else if (note == 20) return "G#6";
                else if (note == 21) return "A6";
                else if (note ==22) return "A#6";
                else if (note ==23) return "B5";
                else if (note == 24) return "C7";
                else return "R";
            }
        }
    }
    public String getStringNoteFlat(Integer note) {
    	if (note < 12){
            if (note < 6){
                if (note == 0) return "Mid C";
                else if (note == 1) return "Mid D flat";
                else if (note == 2) return "Mid D";
                else if (note ==3) return "Mid E flat";
                else if (note == 4) return "Mid E";
                else return "Mid F";
            }
            else{
                if (note == 6) return "Mid G flat";
                if (note == 7) return "Mid G";
                else if (note == 8) return "Mid A flat";
                else if (note == 9) return "Mid A";
                else if (note ==10) return "Mid B flat";
                else return "Mid B";
            }
        }
        else{
            if (note < 18){
                if (note == 12) return "High C";
                else if (note == 13) return "High D flat";
                else if (note == 14) return "High D";
                else if (note ==15) return "High E flat";
                else if (note == 16) return "High E";
                else return "High F";
            }
            else{
                if (note == 18) return "High G flat";
                if (note == 19) return "High G";
                else if (note == 20) return "High A flat";
                else if (note == 21) return "High A";
                else if (note ==22) return "High B flat";
                else if (note ==23) return "High B";
                else if (note == 24) return "High C";
                else return "Rest";
            }
        }

    }
    public String getStringNoteSharp(Integer note) {
    	if (note < 12){
            if (note < 6){
                if (note == 0) return "Mid C";
                else if (note == 1) return "Mid C sharp";
                else if (note == 2) return "Mid D";
                else if (note ==3) return "Mid C sharp";
                else if (note == 4) return "Mid E";
                else return "Mid F";
            }
            else{
                if (note == 6) return "Mid F sharp";
                if (note == 7) return "Mid G";
                else if (note == 8) return "Mid G sharp";
                else if (note == 9) return "Mid A";
                else if (note ==10) return "Mid A sharp";
                else return "Mid B";
            }
        }
        else{
            if (note < 18){
                if (note == 12) return "High C";
                else if (note == 13) return "High C sharp";
                else if (note == 14) return "High D";
                else if (note ==15) return "High D sharp";
                else if (note == 16) return "High E";
                else return "High F";
            }
            else{
                if (note == 18) return "High F sharp";
                if (note == 19) return "High G";
                else if (note == 20) return "High G sharp";
                else if (note == 21) return "High A";
                else if (note ==22) return "High A sharp";
                else if (note ==23) return "High B";
                else if (note == 24) return "High C";
                else return "Rest";
            }
        }

    }
    public String getBeat(Integer beat){
        if (beat <5){
            if (beat ==0) return "s";
            else if (beat == 1) return "i";
            else if (beat == 2) return "is";
            else if (beat ==3) return "q";
            else return "qi";
        }
        else{
            if (beat == 5) return "h";
            else if (beat ==6) return "hq";
            else if (beat ==7) return "w";
            else return "i*";
        }
    }
    public String getStringBeat(Integer beat){
        if (beat <5){
            if (beat ==0) return "(sixteenth)";
            else if (beat == 1) return "(eighth)";
            else if (beat == 2) return "(dotted eighth)";
            else if (beat ==3) return "(quarter)";
            else return "(dotted quarter)";
        }
        else{
            if (beat == 5) return "(half)";
            else if (beat ==6) return "(dotted half)";
            else if (beat ==7) return "(whole)";
            else return "(triplet)";
        }
    }
}
