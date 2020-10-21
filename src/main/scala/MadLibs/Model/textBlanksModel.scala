package MadLibs.Model


case class textBlanksModel ( key: String, answers: Option[String]) {
}

object textBlanksModel {
  def apply(key: String, answers: Option[String]) = new textBlanksModel(key, answers)
}