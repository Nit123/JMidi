import javax.sound.midi.*;
import java.io.File;

public class MidiTester {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public static final int CHANNEL_ONE_CONTROL = 0xB0;
    public static final int CHANNEL_ONE_CHANGE = 0xC0;


    public static final int TIME_SIGNATURE = 0x58;
    public static final int KEY_SIGNATURE = 0x59;
    public static final int TRACK_NAME = 0x03;
    public static final int RANDOM_TEXT = 0x01;
    public static final int SET_TEMPO = 0x51;
    public static final int END_OF_TRACK = 0x2F;


    public static void main(String[] args) throws Exception {
        Sequence sequence = MidiSystem.getSequence(new File("Fur_Elise.mid"));

//        System.out.println("PPQ (ticks per quarter note): " + sequence.getResolution());
//
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                //System.out.print("Tick #: " + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    //if((sm.getData1() / 12) - 1 >= 0) {
                        JMidiNote midiNote = new JMidiNote(event.getTick(), sm.getChannel(), sm.getData2(), sm.getData1());
                        System.out.println(midiNote);
                    //}
//                    determineChannel(sm);
//                    if(sm.getCommand() == NOTE_ON || sm.getCommand() == NOTE_OFF)
//                        commandNote(sm);
//                    else {
//                        if(sm.getCommand() == CHANNEL_ONE_CHANGE){
//                            System.out.println("Channel 1 Program Change ");
//                        }
//                        else if(sm.getCommand() == CHANNEL_ONE_CONTROL)
//                            System.out.println("Channel 1 Control/Mode Change ");
//                        else
//                            System.out.println("Command:" + sm.getCommand());
//                    }
                } else {
//                    //System.out.println();
                    if (message instanceof MetaMessage) {
                        MetaMessage metaMessage = (MetaMessage) message;
                        int messageType = metaMessage.getType();
//                        if(messageType == TIME_SIGNATURE) {
//                            System.out.println("Time Signature Information: ");
//                            byte[] timeInfo = metaMessage.getData();
//                            System.out.println("Time Signature: " + timeInfo[0] + "/" + (int) Math.pow(2, timeInfo[1]));
//                            System.out.println("MIDI ticks per metronome click: " + timeInfo[2]);
//                            System.out.println("32nd notes per MIDI quarter note: " + timeInfo[3]);
//                        }
//                        else if(messageType == KEY_SIGNATURE)
//                            System.out.print("Key Signature Information.");
//                        else if(messageType == TRACK_NAME)
//                            System.out.print("Track Name Information.");
//                        else if(messageType == RANDOM_TEXT)
//                            System.out.print("Random Text...we don't know..");
                        if (messageType == SET_TEMPO) {
                            byte[] tempo = metaMessage.getData();
                            JMidiTempo midiTempo = new JMidiTempo(event.getTick(), sequence.getResolution(), tempo);
                            System.out.println(midiTempo);
                        }
//                        else if(messageType == END_OF_TRACK)
//                            System.out.print("End of Track.");
//                        else
//                            System.out.print("Type of MetaMessage: " + metaMessage.getType());
//
//                        System.out.println();
//                    }
//                    // System.out.println("Other message: " + message.getClass());
//                }
//            }

                        System.out.println();
                    }


                }
            }
        }
    }
}

//    private static void determineChannel(ShortMessage ms){
//        final int RIGHT_HAND = 0;
//        final int LEFT_HAND = 1;
//        if(ms.getChannel() == RIGHT_HAND)
//            System.out.print("ACOUSTIC GRAND PIANO: ");
//        else if(ms.getChannel() == LEFT_HAND)
//            System.out.print("LEFT HAND: ");
//        else
//            System.out.print("Channel: " + ms.getChannel() + " ");
//
//    }
//    private static void commandNote(ShortMessage sm){
//        int key = sm.getData1();
//        int octave = (key / 12)-1;
//        int note = key % 12;
//        String noteName = NOTE_NAMES[note];
//        int velocity = sm.getData2();
//
//        // VELOCITY TO DYNAMIC (upper bounds)
//        final int PPP = 16;
//        final int PP = 33;
//        final int P = 49;
//        final int MP = 64;
//        final int MF = 80;
//        final int F = 96;
//        final int FF = 112;
//        final int FFF = 127;
//
//
//        if(sm.getCommand() == NOTE_ON) {
//            if(velocity == 0) {
//                System.out.println("***NOTE OFF***, " + noteName + octave + " key=" + key + " velocity: " + velocity);
//            }
//            else {
//                System.out.print("Note on, " + noteName + octave + " key=" + key + " ");
//                if(velocity <= PPP){
//                    System.out.println("dynamic: PPP");
//                }
//                else if(velocity <= PP){
//                    System.out.println("dynamic: PP");
//                }
//                else if(velocity <= P){
//                    System.out.println("dynamic: P");
//                }
//                else if(velocity <= MP)
//                    System.out.println("dynamic: MP");
//                else if(velocity <= MF)
//                    System.out.println("dynamic: MF");
//                else if(velocity <= F)
//                    System.out.println("dynamic: F");
//                else if(velocity <= FF)
//                    System.out.println("dynamic: FF");
//                else if(velocity <= FFF)
//                    System.out.println("dynamic: FFF");
//            }
//        }
////        else
////            System.out.println("***NOTE OFF***, " + noteName + octave + " key=" + key + " velocity: " + velocity);
//    }
//



