package jetbrains.buildserver.metarunner.xml

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 07.12.10 15:09 
 */

abstract class ParameterType

case class TextType(useTextArea : Boolean ) extends ParameterType
case object HiddenType extends ParameterType
case class CooserType(items : List[ChooserItem]) extends ParameterType

class ChooserItem(value : String, description : String, isDefault : Boolean)

























