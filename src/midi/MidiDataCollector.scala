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

  private val RegisterCount = 16
  private val registers = Array.fill( RegisterCount )( -1.0 )

  def getRegister(register: Int): Double = {
    registers.lift( register ).getOrElse(-1.0)
  }

  private lazy val transDevice = getTransmittingDevice

  def init() {
    transDevice
  }

  private def getTransmittingDevice: MidiDevice = {
    val availableDeviceInfos = MidiSystem.getMidiDeviceInfo
    val hardwareCandidateInfo = availableDeviceInfos.find( deviceInfo => !deviceInfo.getDescription.contains("Software") )
    val candidateDevice = MidiSystem.getMidiDevice( hardwareCandidateInfo.getOrElse( throw new ExtensionException("No Midi-Compliant Hardware Device connected") ) )
    val candidateTransmitter =  candidateDevice.getTransmitter
    if ( candidateTransmitter == null) throw new ExtensionException("No Available transmitter in hardware device " + candidateDevice.getDeviceInfo.getDescription )
    try {
      if ( candidateDevice.isOpen ) {
        candidateDevice.close()
      }
      candidateDevice.open()
    }
    catch {
      case e: Exception => throw new ExtensionException("Midi Device cannot be opened.", e)
    }
    candidateTransmitter.setReceiver(this)
    candidateDevice
  }

  def closeCommunications() {
    if (transDevice.isOpen)
      transDevice.close()
  }

  //RECEIVER
  override def send(message: MidiMessage, timeStamp: Long) {
    message match {
      case sm: ShortMessage =>
        import sm._
        val channel = getChannel
        if (0 <= channel && channel < RegisterCount) registers(channel) = getData1 + getData2 * 128
      case _ => //ignore non ShortMessages
    }
  }

  override def close() {}

}


