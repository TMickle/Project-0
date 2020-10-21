package MadLibs.View

case class NoCommandFound(val c: String) {

}

object NoCommandFound {

  def message(c: String) : String = {
    s"""I'm sorry, Computer no understand '${c}'
       |Human, please try again or type 'opt' to view your Options
       |""".stripMargin
  }


}