import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeSet;

public class JMidiControl {

    //public static TreeMap<int[], String> CONTROL_MESSAGE;
    public static TreeSet<MidiControlMessage> MESSAGE_SET = new TreeSet<>();
    private final static String UNDEFINED = "UNDEFINED";

    private final static int END_OF_TRACK = -1;

    private int codeNumber;
    private int currentStatus;

    public static void initMessageSet() throws FileNotFoundException {
        Scanner codeScan = new Scanner(new File(".\\req\\MIDI_Control_Change_Codes.txt"));
        String undefinedLine = codeScan.nextLine();

        Scanner undefinedScan = new Scanner(undefinedLine);
        undefinedScan.next();

        while(undefinedScan.hasNext()){
            int codeNum = undefinedScan.nextInt();
            MidiControlMessage message = new MidiControlMessage(codeNum, 0, 0, UNDEFINED);
            MESSAGE_SET.add(message);
        }

        undefinedScan.close();

        while(codeScan.hasNext()){
            String codeString = codeScan.nextLine();
            Scanner stringScan = new Scanner(codeString);

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


    public JMidiControl(int codeNum, int dataNum) throws FileNotFoundException{
        if(MESSAGE_SET == null)
            initMessageSet();

        codeNumber = codeNum;
        currentStatus = dataNum;
    }

    public JMidiControl(){
        codeNumber = END_OF_TRACK;
        currentStatus = END_OF_TRACK;
    }

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