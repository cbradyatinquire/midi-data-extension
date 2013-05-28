midi-data-extension
===================

Extension to communicate with arduino as a MIDI device

*PROBLEM:  Java seems to keep hold of the list of MIDI devices more tenaciously than it does for Serial devices.  This 
means that when a device is hot-swapped after the first read of available devices, the change of status is not recognized.
For REMOVAL, we can handle this by catching an exception.  For PLUG-IN, this is a problem.  Moreover, calling
com.sun.media.sound.JDK13Services.setCachingPeriod(0), as was suggested in several online forum 
entries, does not solve the problem. :(
