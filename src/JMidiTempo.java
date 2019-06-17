/* Class that represents a Tempo MIDI message.
   Allows client to no longer require original MIDI data to
   determine what the tempo at a given time/tick is.
 */
public class JMidiTempo {
    // Instance Constants (an instance of JMidiTempo will not change
    // since a new tempo will simply mean the creation of a new JMidiTempo
    // object)
    private final int PPQ; // Pulses per quarter note (ticks per quarter note)
    private final int BEATS_PER_MINUTE; // BPM is a standard for tempo in Western Music
    private final double TICKS_PER_SECOND; // Ticks per Second
    private final double SECONDS_PER_TICK; // Alternatively, Seconds per Tick
    private final long START_TICK; // The starting tick of the current tempo (since tempos can change)

    // Constructor for the JMidiTempo object which takes in the starting tick,
    // the PPQ, and the rest of the MIDI information. Allows client to disregard
    // MIDI information after object is built.
    public JMidiTempo(long startTick, int ppq, byte[] tempoInfo){
        PPQ = ppq;
        // tempo is given in hexadecimal values representing microseconds per quarter note
        int tempoInt = (tempoInfo[0] & 0xff) << 16 | (tempoInfo[1] & 0xff) << 8 | (tempoInfo[2] & 0xff);

        // this turns it to beats per minute
        BEATS_PER_MINUTE = 60_000_000 / tempoInt;

        // which can then be turned into ticks per second / seconds per tick
        TICKS_PER_SECOND = PPQ * (BEATS_PER_MINUTE / 60.0);
        SECONDS_PER_TICK = 1 / TICKS_PER_SECOND;

        START_TICK = startTick;
    }

    // Prints it out in a human readable (though inefficient) manner,
    // will be changed to toDescriptiveString in version 0.2
    @Override
    public String toString() {
        StringBuilder tempoString = new StringBuilder();

        tempoString.append("PPQ (ticks per quarter note): ");
        tempoString.append(PPQ);
        tempoString.append("\n");

        tempoString.append("Starting tick: ");
        tempoString.append(START_TICK);
        tempoString.append("\n");

        tempoString.append("BBM (beats per minute): ");
        tempoString.append(BEATS_PER_MINUTE);
        tempoString.append("\n");

        tempoString.append("Ticks per second: ");
        tempoString.append(TICKS_PER_SECOND);
        tempoString.append("\n");

        tempoString.append("Seconds per tick: ");
        tempoString.append(SECONDS_PER_TICK);
        tempoString.append("\n");

        return tempoString.toString();
    }
}
