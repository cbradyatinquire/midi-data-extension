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

  val reciever = new MidiDataCollector
  /**
   * Loads the primitives in the extension. This is called once per model compilation.
   *
   * @param primManager The manager to transport the primitives to NetLogo
   */
  override def load(primManager: PrimitiveManager) {
    primManager.addPrimitive("read", Read)
  }

  object Read extends DefaultReporter {
    override def getSyntax: Syntax = Syntax.reporterSyntax( Array(Syntax.NumberType), Syntax.NumberType )
    def report(args: Array[Argument], context: Context): AnyRef = {
      val register = args(0).getIntValue
      Double.box(reciever.getRegister( register ))
    }
  }
}
