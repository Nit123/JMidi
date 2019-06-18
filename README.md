![JMidi logo](logo/JMIDI_Logo_Small.png)

# JMidi
 
JMidi is an open-source Java library used to analyze MIDI files for data analysis.


### Why MIDI?

According to [Wikipedia](https://en.wikipedia.org/wiki/MIDI), MIDI is "a technical standard that describes a communications protocol, digital interface, and electrical connectors that connect a wide variety of electronic musical instruments, computers, and related audio devices for playing, editing and recording music."



However, more importantly, MIDI *does not store actual audio of a song/music*. Rather, it stores messages to send to MIDI channels and programs to produce notes at a certain audio level (velocity) and pitch. This means in order to analyze the music stored in the MIDI file, **we do not have to deal with the audio waves themselves**, rather just deal with the MIDI messages stored in the file.



### Why Java?

Java has a very robust MIDI [library](https://docs.oracle.com/javase/tutorial/sound/overview-MIDI.html) but since its main purpose is to allow developers to create MIDI programs rather than analyze the MIDI files themselves, JMidi utilizes the Java's MIDI libararies and gears them towards data analysis.

### Version 0.1 (*JMIDI Larghessimo*) ###

This is the first release of JMidi that is currently released as a beta version. 
Major changes such as adopting a more efficient format for output in order to allow
for better data analysis as well as support for measuring counting and "declutter" mode
(which aims to reduce the amount of MIDI messages recorded to those you wish to keep track of)
is coming soon.

Similarly, a user guide that explains the library in more detail is in the works.

**Please do not mess with the files in the [req](https://github.com/Nit123/JMidi/tree/master/req) directory 
as they are in a specialized format that allows the library to read in the MIDI constants.**

## Getting Started ##
Simply clone or download this repo and run the MidiTester.java file. For more help on how to 
run Java files, please see this [website](https://www.tutorialspoint.com/How-to-run-a-java-program) or Google it!

## Special Thanks ##
Special thanks to Fronrich Puno for the logo and helping me use MuseScore to obtain the MIDI
tester files in the [MIDI_FILES](https://github.com/Nit123/JMidi/tree/master/MIDI_Files) directory,
Ansh Shah for envisioning the text file formatting for all the MIDI constants, and Mason
Eastman for all the music theory help!

Finally, thanks to [@ClassicMan](https://musescore.com/classicman) for transcribing
the music on MuseScore which generated the MIDI files used as samples.


### In-depth Documentation coming soon! ###
