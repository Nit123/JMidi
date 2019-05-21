import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;

public class JMidiNote implements Comparable<JMidiNote>{

    private long tickStart;
    private long tickStop;
    private String channelName;
    private String pitchNotation;
    private String clef;
    private int dynamic;
    private boolean isOn;
    private long noteInTermsOfQuarter;

    private final int PPQ;

    // VELOCITY TO DYNAMIC (upper bounds)
    // Somewhat arbitrary values found from this: https://en.wikipedia.org/wiki/File:Dynamic%27s_Note_Velocity.svg
    private static final int PPP = 16;
    private static final int PP = 33;
    private static final int P = 49;
    private static final int MP = 64;
    private static final int MF = 80;
    private static final int F = 96;
    private static final int FF = 112;
    private static final int FFF = 127;

    // Note names
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    // MIDI CHANNEL map
    public static TreeMap<Integer, String> CHANNEL_LOOKUP;

    // clef delineator
    private final String TREBLE_SWITCH = "C4";

    // Constuctor for a note before tickStop is known
    public JMidiNote (long TickStart, int channelNum, int velocity, int key, int ppq) throws IOException {
        PPQ = ppq;

        if (velocity == 0)
            isOn = false;
        else {
            isOn = true;
        }

        setDynamic(velocity);

        this.tickStart = TickStart;
        // default value of tickStop when unknown will be 0 since we can
        // only start a Note at tick 0.
        this.tickStop = 0;

        if(CHANNEL_LOOKUP == null)
            setUpChannelLookup();

        // Assumes we have a valid key for the channel
        if(CHANNEL_LOOKUP.containsKey(channelNum))
         channelName = CHANNEL_LOOKUP.get(channelNum);
        else
            channelName = "" + channelNum;

        // Set up pitch notation
        int octave = (key / 12) - 1;
        int note = key % 12;
        if(octave >= 0)
            pitchNotation = NOTE_NAMES[note] + octave;
        else
            pitchNotation = NOTE_NAMES[note] + octave + " (theoretical)";

        // set up clef
        if(octave < 4)
            clef = "BASS";
        else if(octave > 4)
            clef = "TREBLE";
        else{
            if(NOTE_NAMES[note].compareTo("C") < 0)
                clef = "BASS";
            else
                clef = "TREBLE";
        }



    }

    // Used for temporary JMidiNotes that are actually note-off
    // messages.
    public JMidiNote(int channelNum, int key, int ppq) throws IOException{
        // Assumes we have a valid key for the channel
        PPQ = ppq;

        isOn = false;

        if(CHANNEL_LOOKUP == null)
            setUpChannelLookup();

        if(CHANNEL_LOOKUP.containsKey(channelNum))
            channelName = CHANNEL_LOOKUP.get(channelNum);
        else
            channelName = "" + channelNum;

        // Set up pitch notation
        int octave = (key / 12) - 1;
        int note = key % 12;
        if(octave >= 0)
            pitchNotation = NOTE_NAMES[note] + octave;
        else
            pitchNotation = NOTE_NAMES[note] + octave + " (theoretical)";
    }

    public void setUpNoteLength(){
        if(getLengthOfNoteInTicks() > 0){
            // actually has a note length
            long deltaTicks = getLengthOfNoteInTicks();
            noteInTermsOfQuarter = deltaTicks / PPQ;
        }
    }

    public boolean isEndNoteOfThisNote(JMidiNote startNote){
        if(isOn)
            throw new IllegalStateException("isEndNoteOfThisNote can only be called by a note-off JMidiNote.");

        return (startNote.pitchNotation.equals(pitchNotation) && channelName.equals(startNote.channelName) &&
                startNote.PPQ == PPQ);
    }

    private long getLengthOfNoteInTicks(){
        return tickStop - tickStart;
    }

    private void setDynamic(int velocity){
        if(velocity == 0)
            dynamic = -1;
        else if(velocity <= PPP){
            dynamic = 0;
        }
        else if(velocity <= PP){
            dynamic = 1;
        }
        else if(velocity <= P){
            dynamic = 2;
        }
        else if(velocity <= MP)
            dynamic = 3;
        else if(velocity <= MF)
           dynamic = 4;
        else if(velocity <= F)
            dynamic = 5;
        else if(velocity <= FF)
            dynamic = 6;
        else if(velocity <= FFF)
           dynamic = 7;
    }

    // Credit to Ansh Shah for coming up with a text file to store MIDI channel information.
    public static void setUpChannelLookup() throws FileNotFoundException {
        if(CHANNEL_LOOKUP == null) {
            CHANNEL_LOOKUP = new TreeMap<>();
            Scanner channelScan = new Scanner(new File("MIDI_Channels.txt"));

            while (channelScan.hasNextLine()) {
                String channelInfo = channelScan.nextLine();
                Scanner infoScanner = new Scanner(channelInfo);

                while (infoScanner.hasNext()) {
                    int channelNumber = infoScanner.nextInt() - 1;
                    String channelName = infoScanner.next();
                    CHANNEL_LOOKUP.put(channelNumber, channelName);
                }

            }
        }

        //System.out.println(CHANNEL_LOOKUP);
    }

    @Override
    public String toString() {
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

    public void setTickStop(long tickStop) {
        this.tickStop = tickStop;
    }

    public long getTickStart() {
        return tickStart;
    }

    @Override
    public int compareTo(JMidiNote o) {
        if(tickStart > o.tickStart)
            return 1;
        else {
            return -1;
        }
    }
}

