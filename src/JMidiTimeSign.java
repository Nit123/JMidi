public class JMidiTimeSign {
    private int numerator;
    private int denominator;
    private int midiTicksPerMetronomeClick;
    private int _32ndNotesPerMidiQuarterNote;

    public JMidiTimeSign(byte[] data){
        assert data.length == 4; // to follow MIDI guidelines

        numerator = data[0];
        denominator = ((int) Math.pow(2, data[1]));
        midiTicksPerMetronomeClick = data[2];
        _32ndNotesPerMidiQuarterNote = data[3];
    }

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

