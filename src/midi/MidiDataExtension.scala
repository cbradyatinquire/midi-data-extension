package midi

import org.nlogo.api._

/**
 * Created by IntelliJ IDEA.
 * User: cbrady
 * Date: 5/26/13
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
class MidiDataExtension extends DefaultClassManager {

  val dataInterface = new MidiDataCollector

  override def load(primManager: PrimitiveManager) {
    primManager.addPrimitive("read", Read)
    primManager.addPrimitive("open", Open)
    primManager.addPrimitive("close", Close)
  }

  object Read extends DefaultReporter {
    override def getSyntax: Syntax = Syntax.reporterSyntax( Array(Syntax.NumberType), Syntax.NumberType )
    def report(args: Array[Argument], context: Context): AnyRef = {
      val register = args(0).getIntValue
      Double.box(dataInterface.getRegister( register ))
    }
  }

  object Open extends DefaultCommand {
    def perform(args: Array[Argument], context: Context) {
      dataInterface.init()
    }
  }

  object Close extends DefaultCommand {
    def perform(args: Array[Argument], context: Context) {
      dataInterface.closeCommunications()
    }
  }

  override def unload(em: ExtensionManager) {
    super.unload(em)
    dataInterface.closeCommunications()
  }

}
