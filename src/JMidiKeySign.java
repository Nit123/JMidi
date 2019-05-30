import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/** Represents a MIDI Key Signature Event.
 * @author Nitesh Kartha
 * @version 0.1 (Larghissimo)
 */
public class JMidiKeySign {

    public static HashSet<JKeySignature> KEY_SIGNATURES;

    private static String PATH_NAME_FOR_KEY_SIGNATURES = ".\\req\\MIDI_Key_Signature.txt";

    public static void initList() throws FileNotFoundException {
        if(KEY_SIGNATURES == null){
            Scanner fileScan = new Scanner(new File(PATH_NAME_FOR_KEY_SIGNATURES));
            KEY_SIGNATURES = new HashSet<>();

            while (fileScan.hasNextLine()) {
                String key = fileScan.nextLine();
                Scanner keyScan = new Scanner(key);

                while (keyScan.hasNextLine()) {
                    int sharps = keyScan.nextInt();
                    int flats = keyScan.nextInt();
                    String major = keyScan.next();
                    keyScan.next();
                    String minor = keyScan.next();

                    JKeySignature keySign = new JKeySignature(sharps, flats, major, minor);
                    KEY_SIGNATURES.add(keySign);
                }
            }
        }
    }
    public static JKeySignature findKey(byte[] keyData){
        JKeySignature key = null;
        for (JKeySignature keySign : KEY_SIGNATURES) {
            if (keySign.numberOfSharps == keyData[0] && keySign.numberOfFlats == keyData[1])
                key = keySign;
        }

        return key;
    }


    public static class JKeySignature {
        private int numberOfSharps;
        private int numberOfFlats;
        private String majorName;
        private String minorName;

        public JKeySignature(int sharps, int flats, String major, String minor){
            numberOfFlats = flats;
            numberOfSharps = sharps;
            majorName = major;
            minorName = minor;
        }

        @Override
        public String toString(){
            StringBuilder keyStringBuilder = new StringBuilder();
            keyStringBuilder.append("KEY SIGNATURE INFORMATION: \n");
            keyStringBuilder.append("Major: " + majorName + "\n");
            keyStringBuilder.append("Minor: " + minorName + "\n");
            keyStringBuilder.append("(" + numberOfSharps + "#, " + numberOfFlats + "b)\n");

            return keyStringBuilder.toString();
        }
    }



}
