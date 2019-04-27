import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

public class JMidiKeySign {

    public static HashSet<KeySignature> KEY_SIGNATURES;

    public static void initList() throws FileNotFoundException {
        Scanner fileScan = new Scanner(new File("MIDI_Key_Signature.txt"));

        while(fileScan.hasNextLine()){
            String key = fileScan.nextLine();
            Scanner keyScan = new Scanner(key);

            while(keyScan.hasNextLine()){
                int sharps = keyScan.nextInt();
                int flats = keyScan.nextInt();
                String major = keyScan.next();
                keyScan.next();
                String minor = keyScan.next();

                KeySignature keySign = new KeySignature(sharps, flats, major, minor);
                KEY_SIGNATURES.add(keySign);
            }
        }
    }

    private static class KeySignature{
        private int numberOfSharps;
        private int numberOfFlats;
        private String majorName;
        private String minorName;

        public KeySignature(int sharps, int flats, String major, String minor){
            numberOfFlats = flats;
            numberOfSharps = sharps;
            majorName = major;
            minorName = minor;
        }
    }

}
