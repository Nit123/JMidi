import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeSet;

/* Class that represents a MIDI Control Change message.
   Allows client to no longer require original MIDI data to
   determine what the MIDI Control Change message was once the
   object is created.
 */
public class JMidiControl {

    // TreeSet containing all the MIDI Control Change messages. Obtained
    // using the file generated at PATH_NAME_FOR_CONTROL_MESSAGE
    public static TreeSet<MidiControlMessage> MESSAGE_SET = new TreeSet<>();
    private final static String UNDEFINED = "UNDEFINED"; // String assigned to undefined values (MIDI has a lot)

    // DO NOT CHANGE unless you are certain that the pathname has the standardized file, otherwise
    // this program WILL BREAK.
    private final static String PATH_NAME_FOR_CONTROL_MESSAGE = ".\\req\\MIDI_Control_Change_Codes.txt";

    // Special number to indicate the end of track message.
    private final static int END_OF_TRACK = -1;

    // Instance variables representing the code number and status of the Control Message.
    private int codeNumber;
    private int currentStatus;

    // Method that initializes MESSAGE_SET using the specialized file at PATH_NAME_FOR_CONTROL_MESSAGE.
    // This method must be run before any JMidiControl objects are initialized.
    public static void initMessageSet() throws FileNotFoundException {
        // Scanner to go through the file and a String that contains all the
        // undefined values.
        Scanner codeScan = new Scanner(new File(PATH_NAME_FOR_CONTROL_MESSAGE));
        String undefinedLine = codeScan.nextLine();

        // Scanner to go through all the undefined values
        Scanner undefinedScan = new Scanner(undefinedLine);
        undefinedScan.next();

        // Goes through the undefined values and adds them to the MESSAGE_SET
        while(undefinedScan.hasNext()){
            int codeNum = undefinedScan.nextInt();
            MidiControlMessage message = new MidiControlMessage(codeNum, 0, 0, UNDEFINED);
            MESSAGE_SET.add(message);
        }

        undefinedScan.close(); // closes it since we don't need it anymore

        // While the rest of the file has stuff
        while(codeScan.hasNext()){
            // Go through each line
            String codeString = codeScan.nextLine();
            Scanner stringScan = new Scanner(codeString);

            // While the line has stuff, record the data and add it to MESSAGE_SET
            while(stringScan.hasNext()){
                int codeNum = stringScan.nextInt();
                int offLim = stringScan.nextInt();
                int onLim = stringScan.nextInt();
                String codeName = stringScan.next();
                MidiControlMessage message = new MidiControlMessage(codeNum, offLim, onLim, codeName);
                MESSAGE_SET.add(message);
            }
        }
    }

    // Constructor for JMidiControl, assigns the code and status
    public JMidiControl(int codeNum, int dataNum) throws FileNotFoundException{
        if(MESSAGE_SET == null)
            initMessageSet();

        codeNumber = codeNum;
        currentStatus = dataNum;
    }

    // Default Constructor for JMidiControl: an end-of-track message.
    public JMidiControl(){
        codeNumber = END_OF_TRACK;
        currentStatus = END_OF_TRACK;
    }

    // Prints it out in a human readable (though inefficient) manner,
    // will be changed to toDescriptiveString in version 0.2
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        MidiControlMessage message = null;

        if(codeNumber == END_OF_TRACK){
            return "MIDI END OF TRACK";
        }

        for(MidiControlMessage mes: MESSAGE_SET){
            if(mes.codeNum == codeNumber){
                message = mes;
            }
        }

        if(message == null)
            return "MIDI Control Change NOT FOUND";

        stringBuilder.append("MIDI Control Change: ");
        stringBuilder.append(message.codeName);

        if(message.offLimit == 0){
            // off limit doesn't really exist
            return stringBuilder.toString();
        }

        if(currentStatus <= message.offLimit)
            stringBuilder.append(" OFF");
        else
            stringBuilder.append(" ON");

        return stringBuilder.toString();
    }



    private static class MidiControlMessage implements Comparable<MidiControlMessage> {
        private String codeName;
        private int codeNum;
        private int offLimit;
        private int onLimit;


        public MidiControlMessage(int codeNumber, int offLim, int onLim, String codeName) {
            codeNum = codeNumber;
            offLimit = offLim;
            onLimit = onLim;
            this.codeName = codeName;
        }

        @Override
        public int compareTo(MidiControlMessage o) {
            return codeNum - o.codeNum;
        }
    }
}