import javax.sound.midi.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

public class JMidiNote {

    private int TickStart;
    private int TickStop;
    private String channelName;
    private int dynamic;
    private boolean isOn;

    // VELOCITY TO DYNAMIC (upper bounds)
    // Somewhat arbitrary values found from this: https://en.wikipedia.org/wiki/File:Dynamic%27s_Note_Velocity.svg
    private static final int PPP = 16;
    private static final int PP = 33;
    private static final int P = 49;
    private static final int MP = 64;
    private static final int MF = 80;
    private static final int F = 96;
    private static final int FF = 112;
    private static final int FFF = 127;

    private static TreeMap<Integer, String> CHANNEL_LOOKUP;

    // Constuctor for a note before TickStop is known
    public JMidiNote (int TickStart, int channelNum, int velocity){
        if(velocity == 0)
            isOn = false;
        else{
            setDynamic(velocity);
            isOn = true;
        }

        this.TickStart = TickStart;
        this.TickStop = 0;

        if(CHANNEL_LOOKUP == null)
            try {
                setUpChannelLookup();
            } catch(FileNotFoundException e){
                System.out.println("Oh no.");
            }


    }

    private void setDynamic(int velocity){
        if(velocity <= PPP){
            dynamic = 0;
        }
        else if(velocity <= PP){
            dynamic = 1;
        }
        else if(velocity <= P){
            dynamic = 2;
        }
        else if(velocity <= MP)
            dynamic = 3;
        else if(velocity <= MF)
           dynamic = 4;
        else if(velocity <= F)
            dynamic = 5;
        else if(velocity <= FF)
            dynamic = 6;
        else if(velocity <= FFF)
           dynamic = 7;
    }

    // Credit to Ansh Shah for coming up with a text file to store MIDI channel information.
    private static void setUpChannelLookup() throws FileNotFoundException {
        CHANNEL_LOOKUP = new TreeMap<>();
        Scanner channelScan = new Scanner(new File("MIDI_Channels.txt"));

        while(channelScan.hasNextLine()){
            String channelInfo = channelScan.nextLine();
            Scanner infoScanner = new Scanner(channelInfo);

            while(infoScanner.hasNext()){
                int channelNumber = infoScanner.nextInt();
                String channelName = infoScanner.next();
                CHANNEL_LOOKUP.put(channelNumber, channelName);
            }

        }

        System.out.println(CHANNEL_LOOKUP);
    }
}

