import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

public class JMidiControl {

    public static TreeMap<String, int[]> CONTROL_MESSAGE;

    public static void initMap() throws FileNotFoundException {
        final String UNDEFINED = "UNDEFINED";
        Scanner codeScan = new Scanner(new File("MIDI_Control_Change_Codes.txt"));
        String undefinedLine = codeScan.nextLine();

        Scanner undefinedScan = new Scanner(undefinedLine);
        undefinedScan.next();

        while(undefinedScan.hasNext()){
            int codeNum = undefinedScan.nextInt();
            int[] codingInformation = new int[]{codeNum, 0, 0}
            CONTROL_MESSAGE.put(UNDEFINED, codingInformation);
        }

        undefinedScan.close();

        while(codeScan.hasNext()){
            String codeString = codeScan.next();
            Scanner stringScan = new Scanner(codeString);

            while(stringScan.hasNext()){
                int[] codeInfo = new int[]{stringScan.nextInt(), stringScan.nextInt(), stringScan.nextInt()};
                String codeName = stringScan.next();
                CONTROL_MESSAGE.put(codeName, codeInfo);
            }
        }
    }

}
