package MadLibs.Model



case class MadlibEntries( blanks: List[textBlanksModel]) {

}

object MadlibEntries{
  def apply(entries: List[textBlanksModel]) = { new MadlibEntries( entries )}
}
