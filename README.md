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

### In-depth Documentation coming soon! ###
