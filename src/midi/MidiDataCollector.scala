package midi

import javax.sound.midi._
import org.nlogo.api.ExtensionException

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
  private var tdevice:MidiDevice = null

  def init() {
    val transOption = MidiSystem.getMidiDeviceInfo.toList.find( deviceInfo => MidiSystem.getMidiDevice( deviceInfo ).getTransmitter != null)
    try {
      tdevice         = MidiSystem.getMidiDevice( transOption.getOrElse( throw new ExtensionException("No Midi Device connected") ) )
      transmitter     = tdevice.getTransmitter

      if (! tdevice.isOpen ) {
        tdevice.open()
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
    tdevice.close()
  }
}
