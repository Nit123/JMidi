import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

// Arguable the most important class: this class represents
// a MIDI note message and can be used to determine when and how
// long a note is played. This allows a client to disregard MIDI
// data and see what messages/notes are contained in the MIDI file
public class JMidiNote implements Comparable<JMidiNote>{

    // A LOT OF INSTANCE VARIABLES:
    // tickStart: the tick the note starts.
    // tickStop: the tick the note stops (aka the tick where the NOTE OFF message occurs).
    // channelName: name of the channel the note is played on
    // pitchNotation: aka the "name of the note" (ex: C4, E3, etc.)
    // clef: this is the "suggested" clef as composers are weird and transpose music in different ways.
    //       MIDI does not have accesses to these transpositions so we use TREBLE_SWITCH to signify the
    //       the first note represented in treble clef. Currently, this only supports treble/bass clef.
    // dynamic: how loud the note is; based on MIDI velocity
    // isOn: used when determining tickStop; is the current note still "on"?
    // noteInTermsOfQuarter: the ratio of the note's length in terms of a quarter note.
    private long tickStart;
    private long tickStop;
    private String channelName;
    private String pitchNotation;
    private String clef;
    private int dynamic;
    public boolean isOn;
    private double noteInTermsOfQuarter;

    // Used for toString
    public int channelNumber;

    // Pulses per quarter-note or ticks per quarter note, used to determine length of the MIDI note.
    private final int PPQ;

    // VELOCITY TO DYNAMIC (upper) BOUNDS
    // Somewhat arbitrary values found from this: https://en.wikipedia.org/wiki/File:Dynamic%27s_Note_Velocity.svg
    private static final int PPP = 16;
    private static final int PP = 33;
    private static final int P = 49;
    private static final int MP = 64;
    private static final int MF = 80;
    private static final int F = 96;
    private static final int FF = 112;
    private static final int FFF = 127;

    private static final String[] DYNAMIC_NAMES = {"PPP", "PP", "P", "MP", "MF", "F", "FF", "FFF"};

    // Note names
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    // MIDI CHANNEL map
    public static TreeMap<Integer, String> CHANNEL_LOOKUP;

    // clef delineator
    private final String TREBLE_SWITCH = "C4";

    // DO NOT CHANGE UNLESS THE SPECIFIED FILE IS IN A NEW LOCATION.
    // This file contains the MIDI Channel Codes in a specific format. If altered, this library
    // will no longer work.
    private static final String PATH_NAME_FOR_CHANNELS = ".\\req\\MIDI_Channels.txt";

    // Constructor for a note before tickStop is known, aka the "NOTE ON" constructor
    public JMidiNote (long TickStart, int channelNum, int velocity, int key, int ppq) throws IOException {
        PPQ = ppq;
        channelNumber = channelNum;

        // if velocity is 0...then it's a note off
        if (velocity == 0)
            isOn = false;
        else {
            isOn = true;
        }

        // Otherwise, set the dynamic!
        setDynamic(velocity);

        // Makes sense
        this.tickStart = TickStart;

        // default value of tickStop when unknown will be 0 since we can
        // only start a Note at tick 0.
        this.tickStop = 0;

        // Initialize the channels if necessary
        if(CHANNEL_LOOKUP == null)
            setUpChannelLookup();

        // Assumes we have a valid key for the channel
        if(CHANNEL_LOOKUP.containsKey(channelNum))
         channelName = CHANNEL_LOOKUP.get(channelNum);
        else
            // No valid key...then tell me what it is.
            channelName = "" + channelNum;

        // Set up pitch notation
        int octave = (key / 12) - 1; // number attached to key
        int note = key % 12; // index of NOTE_NAMES
        if(octave >= 0)
            // got the pitch notation
            pitchNotation = NOTE_NAMES[note] + octave;
        else
            // Because MIDI is WEIRD...(negative octaves)
            pitchNotation = NOTE_NAMES[note] + octave + " (theoretical)";

        // set up clef (only BASS and TREBLE)
        if(octave < Integer.parseInt(TREBLE_SWITCH.charAt(1) + ""))
            clef = "BASS"; // lower than C4
        else if(octave > Integer.parseInt(TREBLE_SWITCH.charAt(1) + ""))
            clef = "TREBLE"; // higher than C4
        else{
            if(NOTE_NAMES[note].compareTo(TREBLE_SWITCH.substring(0,1)) < 0)
                clef = "BASS"; // in octave 4 but lower than C
            else
                clef = "TREBLE"; // in octave 4 but higher than C
        }



    }

    // Used for temporary JMidiNotes that are actually note-off
    // messages...channelNum must be in CHANNEL_LOOKUP or the program will fail.
    public JMidiNote(long tickStart, int channelNum, int key, int ppq) throws IOException{
        PPQ = ppq;
        this.tickStart = tickStart;

        // isOn is false since it represents a note-off message
        isOn = false;

        // Set it up if you have to.
        if(CHANNEL_LOOKUP == null)
            setUpChannelLookup();

        //
        if(CHANNEL_LOOKUP.containsKey(channelNum))
            channelName = CHANNEL_LOOKUP.get(channelNum);
        else
            throw new IllegalStateException("Cannot make note-off of a non-existent channel.");

        // Set up pitch notation... same as before
        int octave = (key / 12) - 1;
        int note = key % 12;
        if(octave >= 0)
            pitchNotation = NOTE_NAMES[note] + octave;
        else
            pitchNotation = NOTE_NAMES[note] + octave + " (theoretical)";
    }

    // Calculates the note length in terms of a quarter note
    public void setUpNoteLength(){
        long deltaTicks = getLengthOfNoteInTicks(); // grabs length in ticks

        // cannot be negative
        if(deltaTicks < 0)
            throw new IllegalStateException("Cannot calculate negative note length.");

        // actually has a note length
        noteInTermsOfQuarter = deltaTicks / (double) PPQ;
        noteInTermsOfQuarter = (double)Math.round(noteInTermsOfQuarter * 1000d) / 1000d; // rounds it to 3 decimal places
    }

    // Checks whether the current JMidiNote (which must be created using the note-off constructor) is the
    // end of a given JMidiNote (startNote)
    public boolean isEndNoteOfThisNote(JMidiNote startNote){
        // Can only be called by note-off JMidiNotes
        if(isOn)
            throw new IllegalStateException("isEndNoteOfThisNote can only be called by a note-off JMidiNote.");

        // must have same pitchNotation, same Channel, and same "Tempo" (represented by PPQ)
        return (startNote.pitchNotation.equals(pitchNotation) && channelName.equals(startNote.channelName) &&
                startNote.PPQ == PPQ);
    }

    // Self-explanatory method
    private long getLengthOfNoteInTicks(){
        return tickStop - tickStart;
    }

    // Sets up the dynamic of the JMidiNote using its velocity.
    private void setDynamic(int velocity){
        if(velocity == 0)
            dynamic = -1; // note-off has dynamic = -1 so we don't accidentally assign it to a dynamic value
        else if(velocity <= PPP){
            dynamic = 0; // PPP
        }
        else if(velocity <= PP){
            dynamic = 1; // PP
        }
        else if(velocity <= P){
            dynamic = 2; // P
        }
        else if(velocity <= MP)
            dynamic = 3; // MP
        else if(velocity <= MF)
           dynamic = 4; // MF
        else if(velocity <= F)
            dynamic = 5; // F
        else if(velocity <= FF)
            dynamic = 6; // FF
        else if(velocity <= FFF)
           dynamic = 7; // FFF
    }



    // Method that sets up CHANNEL_LOOKUP using the
    // Credit to Ansh Shah for coming up with a text file to store MIDI channel information.
    public static void setUpChannelLookup() throws FileNotFoundException {
        // Method only does stuff if we haven't set it up already.
        if(CHANNEL_LOOKUP == null) {
            // Creates the TreeMap and a Scanner so we can go through the file.
            CHANNEL_LOOKUP = new TreeMap<>();
            Scanner channelScan = new Scanner(new File(PATH_NAME_FOR_CHANNELS));

            // While the file has information..
            while (channelScan.hasNextLine()) {
                // Go through each line using Scanner
                String channelInfo = channelScan.nextLine();
                Scanner infoScanner = new Scanner(channelInfo);

                // While the current line has information..
                while (infoScanner.hasNext()) {
                    int channelNumber = infoScanner.nextInt() - 1; // got the channel number
                    String channelName = infoScanner.next(); // channelName
                    CHANNEL_LOOKUP.put(channelNumber, channelName); // add it to the TreeMap
                }

            }
        }
    }

    public String toString(){
        return pitchNotation;
    }

    public String getDynamic(){
        return DYNAMIC_NAMES[dynamic];
    }

    public String getNoteInTermsOfQuarter(){
        return "" + noteInTermsOfQuarter;
    }

    // Prints it out in a human readable (though inefficient) manner,
    // will be changed to toDescriptiveString in version 0.2
    public String toDescriptiveString() {
        StringBuilder stringBuilder = new StringBuilder();
        String[] DYNAMIC_NAMES = new String[]{"PPP", "PP", "P", "MP", "MF", "F", "FF", "FFF"};

        stringBuilder.append("Starting Tick #: ");
        stringBuilder.append(tickStart);
        stringBuilder.append("\n");

        stringBuilder.append("Channel Name: ");
        stringBuilder.append(channelName);
        stringBuilder.append("\n");

        stringBuilder.append("Note: ");
        stringBuilder.append(pitchNotation);
        stringBuilder.append(" (");
        stringBuilder.append(clef);
        stringBuilder.append(")");
        stringBuilder.append("\n");

        stringBuilder.append("Dynamic: ");
        if(dynamic >= 0)
            stringBuilder.append(DYNAMIC_NAMES[dynamic]);
        else
            stringBuilder.append(DYNAMIC_NAMES[0] + " (note off)");
        stringBuilder.append("\n");

        stringBuilder.append("Ratio to quarter-note: ");
        stringBuilder.append(noteInTermsOfQuarter);
        stringBuilder.append("\n");


        return stringBuilder.toString();
    }

    public String getChannelName(){
        return channelName;
    }

    // Setter for TickStop so we can update notes as we find out when they stop.
    public void setTickStop(long tickStop) {
        this.tickStop = tickStop;
    }

    // Getter for TickStart so we can check what their start times are.
    public long getTickStart() {
        return tickStart;
    }

    // compareTo is used so we can implement sorting in the near future.
    @Override
    public int compareTo(JMidiNote o) {
        if(tickStart > o.tickStart)
            return 1; // this one is later
        else if(tickStart == o.tickStart)
            return 0; // same timing
        else {
            return -1; // this one is earlier.
        }
    }
}

