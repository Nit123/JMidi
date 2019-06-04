import javax.sound.midi.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

/* Class as a tester for JMidi objects. This will eventually become
   a MIDI parser which will create a file of a certain format for
   data analysis. Currently, this will transcribe the information
   in a given MIDI file into a human readable output (and not necessarily
   the best output for data analysis...yet).
 */
public class MidiTester {

    // Variables that represent the MIDI value for various types of MIDI messages.
    // Depending on the type of MIDI file, some choose to format a NOTE_OFF message
    // using the NOTE_OFF tag or simply a NOTE_ON message with 0 velocity.
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final int MIDI_CONTROL_CHANGE = 0xB0; // Header for control change messages
    public static final int PROGRAM_CHANGE = 0xC0; // Header for program (instrument/channel) change messages

    // Variables that represent MIDI values for various meta-messages.
    public static final int TIME_SIGNATURE = 0x58;
    public static final int KEY_SIGNATURE = 0x59;
    public static final int TRACK_NAME = 0x03;
    public static final int RANDOM_TEXT = 0x01;
    public static final int SET_TEMPO = 0x51;
    public static final int END_OF_TRACK = 0x2F;
    public static final int MIDI_PORT_MESSAGE = 0x21;

    // Relative file path of the MIDI file that you want to analyze.
    public static final String FILE_NAME = ".\\MIDI_Files\\La_Campanella.mid";

    public static void main(String[] args) throws Exception {

//        PrintStream out = new PrintStream(new FileOutputStream("output.txt"), true);
//        System.setOut(out);

        // Creates a Java Sequence for us to manipulate the MIDI file and
        // a LinkedList to store the notes when determining when they are turned off.
        Sequence sequence = MidiSystem.getSequence(new File(FILE_NAME));
        LinkedList<JMidiNote> notes = new LinkedList<>();

        // Goes through each track
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();

            // Goes through each MIDI message in the track
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    // Is it a note (on) message?
                    if (sm.getCommand() == NOTE_ON && sm.getData2() != 0) {
                        // Creates the JMidi object and adds it to the LinkedList.
                        JMidiNote midiNote = new JMidiNote(event.getTick(), sm.getChannel(), sm.getData2(),
                                sm.getData1(), sequence.getResolution());
                        notes.add(midiNote);
                    }
                    // Is it a note OFF message?
                    else if(sm.getData2() == 0){
                        // Creates specialized note-off JMidiNote
                        JMidiNote noteOff = new JMidiNote(event.getTick(),sm.getChannel(), sm.getData1(), sequence.getResolution());
                        // Goes through all the notes already played
                        for(JMidiNote possibleNote : notes){
                            if(possibleNote.isOn && noteOff.isEndNoteOfThisNote(possibleNote)){
                                // found it! Note hasn't been turned off and is the same as the note-off message
                                // Sets tickStop and note length.
                                possibleNote.setTickStop(noteOff.getTickStart());
                                possibleNote.setUpNoteLength();
                                possibleNote.isOn = false; // turns off the note
                                System.out.println(possibleNote); // prints it in place (might be changed later)
                                break; // stop the search
                            }
                        }

                    }
                    // Is it a MIDI Program Change Message?
                    else if (sm.getCommand() == PROGRAM_CHANGE) {
                        System.out.print("Select Channel Mode: ");
                        JMidiNote.setUpChannelLookup(); // Sets up the channel lookup map if needed
                        System.out.print(JMidiNote.CHANNEL_LOOKUP.get(sm.getData1()));
                        System.out.println();
                    }
                    // Is it a MIDI Control Change Message?
                    else if (sm.getCommand() == MIDI_CONTROL_CHANGE) {
                        JMidiControl.initMessageSet(); // Sets up the control message map if needed
                        JMidiControl control = new JMidiControl(sm.getData1(), sm.getData2());
                        System.out.print(control);
                        System.out.println();
                        System.out.println();
                    }
                } else {
                    // Most likely, the message is a MIDI MetaMessage
                    if (message instanceof MetaMessage) {
                        MetaMessage metaMessage = (MetaMessage) message;
                        int messageType = metaMessage.getType();
                        // Is it a time signature message?
                        if (messageType == TIME_SIGNATURE) {
                            byte[] timeInfo = metaMessage.getData();
                            JMidiTimeSign time = new JMidiTimeSign(timeInfo);
                            System.out.println(time);
                        }
                        // Is it a key signature message?
                        else if (messageType == KEY_SIGNATURE) {
                            JMidiKeySign.initList(); // Initialize key signature list if needed
                            byte[] keyInfo = metaMessage.getData();
                            JMidiKeySign.JKeySignature keySign = JMidiKeySign.findKey(keyInfo);
                            System.out.println(keySign);
                        }
                        // Is it a tempo message?
                        else if (messageType == SET_TEMPO) {
                            byte[] tempo = metaMessage.getData();
                            JMidiTempo midiTempo = new JMidiTempo(event.getTick(), sequence.getResolution(), tempo);
                            System.out.println(midiTempo);
                        }
                        // End of Track message?
                        else if (messageType == END_OF_TRACK) {
                            JMidiControl mes = new JMidiControl();
                            System.out.println(mes);
                            System.out.println();
                        }
                        // MIDI port message?
                        else if (messageType == MIDI_PORT_MESSAGE) {
                            System.out.println("MIDI Port: " + (metaMessage.getData()[0] + 1));
                        }
                        // Random text/track name?
                        else if (messageType == TRACK_NAME || messageType == RANDOM_TEXT) {
                                // NEVER USED IN SAMPLE BUT THIS IS WHAT WE WOULD DO
                                System.out.println("TRACK NAME:");
                                byte[] trackInfo = metaMessage.getData();
                                for (byte info : trackInfo)
                                    System.out.print((char) info);
                                System.out.println();
                        }
                        // No idea...
                         else
                            System.out.print("Type of MetaMessage: " + metaMessage.getType());

                        System.out.println();
                    }


                }
            }
        }

    }
}







