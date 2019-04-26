import javax.sound.midi.*;
import java.io.File;

public class MidiTester {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final int MIDI_CONTROL_CHANGE = 0xB0;
    public static final int PROGRAM_CHANGE = 0xC0;


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
                    if (sm.getCommand() == NOTE_ON || sm.getCommand() == NOTE_OFF) {
                        JMidiNote midiNote = new JMidiNote(event.getTick(), sm.getChannel(), sm.getData2(), sm.getData1(), sequence.getResolution());
                        System.out.println(midiNote);
                    }
                    else if (sm.getCommand() == PROGRAM_CHANGE) {
                        System.out.print("Select Channel Mode: ");
                        JMidiNote.setUpChannelLookup();
                        System.out.print(JMidiNote.CHANNEL_LOOKUP.get(sm.getData1()));
                        System.out.println();
                    }
//                        }
                    else if (sm.getCommand() == MIDI_CONTROL_CHANGE) {
                        JMidiControl.initMessageSet();
                        JMidiControl control = new JMidiControl(sm.getData1(), sm.getData2());
                        System.out.print(control);
                        System.out.println();
                    }
                }
                    else {
                    if (message instanceof MetaMessage) {
                            //ShortMessage sm = (ShortMessage) message;
                            MetaMessage metaMessage = (MetaMessage) message;
                            int messageType = metaMessage.getType();
                            if (messageType == TIME_SIGNATURE) {
                                byte[] timeInfo = metaMessage.getData();
                                JMidiTimeSign time = new JMidiTimeSign(timeInfo);
                                System.out.println(time);
                            }
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
                            } else if (messageType == END_OF_TRACK) {
                                JMidiControl mes = new JMidiControl();
                                System.out.println(mes);
                            }
//                        else
//                            System.out.print("Type of MetaMessage: " + metaMessage.getType());
//
//                        System.out.println();
                        }
                    }
//                        else
//                            System.out.println("Command:" + sm.getCommand());
//                    }
                    }
//                    //System.out.println();

//                }
//            }

                            System.out.println();
                        }


                    }
                }







