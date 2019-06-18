/* Class that represents a MIDI Time Signature message.
   Allows client to no longer require original MIDI data to
   determine what the time signature at a given tick/note is.
 */
public class JMidiTimeSign {
    // Instance variables representing the numerator, denominator,
    // ticks per Metronome Click, and 32nd notes per MIDI Quarter Note (should be 8).
    private int numerator;
    private int denominator;
    private int midiTicksPerMetronomeClick;
    private int _32ndNotesPerMidiQuarterNote;

    // Constructor using MIDI data.
    public JMidiTimeSign(byte[] data){
        // Must have all four values for it to be a valid Time Signature Message
        if(data.length != 4)
            throw new IllegalArgumentException("MIDI data for Time Signature must be 4 bytes long");// to follow MIDI guidelines

        numerator = data[0];
        denominator = ((int) Math.pow(2, data[1])); // denom = 2^second data value
        midiTicksPerMetronomeClick = data[2];
        _32ndNotesPerMidiQuarterNote = data[3];
    }

    // Prints it out in a human readable (though inefficient) manner,
    // will be changed to toDescriptiveString in version 0.2
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Time Signature: ");
        stringBuilder.append(numerator);
        stringBuilder.append("/");
        stringBuilder.append(denominator);
        stringBuilder.append("\n");

        stringBuilder.append("MIDI Ticks per Metronome Click: ");
        stringBuilder.append(midiTicksPerMetronomeClick);
        stringBuilder.append("\n");

        stringBuilder.append("32nd Notes per MIDI Quarter Note: ");
        stringBuilder.append(_32ndNotesPerMidiQuarterNote);
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }
}

