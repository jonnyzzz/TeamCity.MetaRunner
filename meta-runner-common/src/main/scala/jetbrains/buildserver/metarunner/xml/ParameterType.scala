package jetbrains.buildserver.metarunner.xml

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:09 
 */

abstract class ParameterType(name: String)

case class TextType(useTextArea : Boolean) extends ParameterType("text")
case object HiddenType extends ParameterType("hidden")
case class ChooserType(items : List[ChooserItem]) extends ParameterType("list")

class ChooserItem(value : String, description : String, isDefault : Boolean)

























