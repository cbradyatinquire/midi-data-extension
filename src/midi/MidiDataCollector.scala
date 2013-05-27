package midi

import javax.sound.midi._
import org.nlogo.api.ExtensionException
import com.sun.media.sound.MidiInDeviceProvider

/**
 * Created by IntelliJ IDEA.
 * User: cbrady
 * Date: 5/26/13
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */
class MidiDataCollector extends Receiver{
  //REGISTER NUMBERS     0   1   2   3   4   5   6   7   8   9  10  11  12  13  14  15
  val registers = Array(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1)
  def getRegister(register:Int):Double = {
    if ( 0 to 15 contains register ) registers(register)
    else -1
  }


  private var transmitter: Transmitter = null
  private var transDevice: MidiDevice = null

  def init() {
    if (transDevice != null ) closeCommunications()
    transmitter = null


    val availableDeviceInfos = MidiSystem.getMidiDeviceInfo.toList



   /* availableDevices.foreach{ deviceInfo: MidiDevice.Info =>
      val device = MidiSystem.getMidiDevice( deviceInfo )
      val trans = device.getTransmitter
      println( "device with info " + deviceInfo.getDescription  )
      if ( trans == null ) println("NO TRANSMITTER")
      else println( "A TRANSMITTER: " + trans.getClass.toString )
      if (trans.isInstanceOf[MidiInDeviceProvider]) println("VICTORY IS MINE")
    }
*/


    availableDeviceInfos.foreach(y => println(y.getDescription + ", " + y.getVendor + ", " + y.isInstanceOf[Sequencer]) + ", " + y.isInstanceOf[Synthesizer])
    val hardwareCandidateInfo = availableDeviceInfos.find( deviceInfo => !deviceInfo.getDescription.contains("Software") )
    val candidateDevice = MidiSystem.getMidiDevice( hardwareCandidateInfo.getOrElse( throw new ExtensionException("No Midi-Compliant Hardware Device connected") ) )
    println("name = " + candidateDevice.getDeviceInfo.getName)
    val candidateTransmitter =  candidateDevice.getTransmitter
    /*
    && deviceToTest.getTransmitter != null && deviceToTest.getTransmitter.isInstanceOf[MidiInDeviceProvider]  )
        }
     */
    if ( candidateTransmitter == null) throw new ExtensionException("No Available transmitter in hardware device " + candidateDevice.getDeviceInfo.getDescription )
    //if ( !candidateTransmitter.isInstanceOf[MidiInDeviceProvider] ) throw new ExtensionException("Hardware device " + candidateDevice.getDeviceInfo.getDescription + " is not a MIDI input device.")
    try {

      transDevice     = candidateDevice
      transmitter     = candidateTransmitter

      println("transdevice: " + transDevice.getDeviceInfo)
      println("transmitter: " + transmitter.toString)

      if (! transDevice.isOpen ) {
        transDevice.open()
        transmitter.setReceiver(this)
      }
    }
    catch {
      case e: MidiUnavailableException => throw new ExtensionException("Midi Device Unavailable"); e.printStackTrace()
    }

  }

  def send(message: MidiMessage, timeStamp: Long) {
    println( message )
    if ( message.isInstanceOf[ShortMessage] )
    {
      val sm       = message.asInstanceOf[ShortMessage]
      val channel  = sm.getChannel
      val data1    = sm.getData1
      val data2    = sm.getData2
      if (0 to 15 contains channel) registers(channel) = data1 + data2 * 128
      //val time   = timeStamp
    }
  }

  def close() {}

  def closeCommunications() {
    if (transDevice != null)
      transDevice.close()
  }
}
