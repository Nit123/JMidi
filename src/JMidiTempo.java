public class JMidiTempo {
    private final int PPQ;
    private final int BEATS_PER_MINUTE;
    private final double TICKS_PER_SECOND;
    private final double SECONDS_PER_TICK;
    private final long START_TICK;

    public JMidiTempo(long startTick, int ppq, byte[] tempoInfo){
        PPQ = ppq;
        int tempoInt = (tempoInfo[0] & 0xff) << 16 | (tempoInfo[1] & 0xff) << 8 | (tempoInfo[2] & 0xff);
        BEATS_PER_MINUTE = 60_000_000 / tempoInt;
        TICKS_PER_SECOND = PPQ * (BEATS_PER_MINUTE / 60.0);
        SECONDS_PER_TICK = 1 / TICKS_PER_SECOND;
        START_TICK = startTick;
    }

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
