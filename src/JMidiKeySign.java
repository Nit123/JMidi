import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/* Class that represents a MIDI Key Signature message.
   Allows client to no longer require original MIDI data to
   determine what the Key Signature is once the object is
   created.
 */
public class JMidiKeySign {

    // HashSet containing all the possible key signatures
    // in a standardized format. MUST BE INITIALIZED THROUGH
    // initList() before any object is created or findKey() is
    // called.
    public static HashSet<JKeySignature> KEY_SIGNATURES;

    // FILE NAME OF REQUIRED FILE NECESSARY TO GENERATE KEY_SIGNATURE
    // DO NOT CHANGE UNLESS FILE PATH CHANGES
    private static String PATH_NAME_FOR_KEY_SIGNATURES = ".\\req\\MIDI_Key_Signature.txt";

    // Method that initializes KEY_SIGNATURE if necessary.
    // Pre: PATH_NAME_FOR_KEY_SIGNATURE must be the correct file path for the
    // MIDI Key Signature text file.
    public static void initList() throws FileNotFoundException {
        if(KEY_SIGNATURES == null){
            // Accesses the MIDI Key Signature text file
            Scanner fileScan = new Scanner(new File(PATH_NAME_FOR_KEY_SIGNATURES));
            KEY_SIGNATURES = new HashSet<>();

            // Goes through the file
            while (fileScan.hasNextLine()) {
                String key = fileScan.nextLine();
                Scanner keyScan = new Scanner(key);

                // Goes through the given key signature
                while (keyScan.hasNextLine()) {
                    // Adds the information
                    int sharps = keyScan.nextInt();
                    int flats = keyScan.nextInt();
                    String major = keyScan.next();
                    keyScan.next();
                    String minor = keyScan.next();

                    // Creates the JKeySignature object for the given Key Signature and
                    // adds it to KEY_SIGNATURES
                    JKeySignature keySign = new JKeySignature(sharps, flats, major, minor);
                    KEY_SIGNATURES.add(keySign);
                }
            }
        }
    }

    // Finds a given JKeySignatue given the MIDI message data.
    public static JKeySignature findKey(byte[] keyData){
        assert KEY_SIGNATURES != null; // just in case something goes badly

        // Creates a default key, goes looking for the correct key, and makes
        // sure you have a valid key.
        JKeySignature key = null;
        for (JKeySignature keySign : KEY_SIGNATURES) {
            if (keySign.numberOfSharps == keyData[0] && keySign.numberOfFlats == keyData[1])
                key = keySign;
        }

        assert key != null; // only breaks if key is not valid

        return key;
    }

    // Static inner class that is used for the objects within KEY_SIGNATURES
    // (this is used since there are defined number of key signatures so
    // we only need information for those)
    public static class JKeySignature {
        // Instance variables for sharps, flats, key signature name as a
        // major scale, and key signature name as a minor scale.
        private int numberOfSharps;
        private int numberOfFlats;
        private String majorName;
        private String minorName;

        // Builds the JKeySignature object.
        public JKeySignature(int sharps, int flats, String major, String minor){
            numberOfFlats = flats;
            numberOfSharps = sharps;
            majorName = major;
            minorName = minor;
        }

        // Prints it out in a human readable (though inefficient) manner,
        // will be changed to toDescriptiveString in version 0.2
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
