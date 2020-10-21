package MadLibs.Controller

import scala.io.{AnsiColor, StdIn}
import scala.util.matching.Regex

/** CLI that interacts with the user and directs inputs to the Controller*/
object Cli {
  /** commandArgPattern is regex that we get us a command and arguments to that
   * command from user input */
  var user : String = ""
  var color : String = ""
  var continueMenuLoop = true
  val commandArgPattern : Regex = "(\\w+)\\s*(.*)".r

  val controller = new Controller()

  /** Runs the menu prompting && listening for user input */
  def run(config: Map[String,String]):Unit = {

    user = config("user")
    color = ansiColorMap(config("color"))

    controller.welcome()
    // This loop here will repeatedly prompt, listen, run code, and repeat
    while(continueMenuLoop) {

      // get user input with StdIn.readLine, read directly from StdIn
      StdIn.readLine() match {
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("Madlib") && arg.trim.equalsIgnoreCase("-p") => controller.playMadlib(requestMadlibByTitle())
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("Madlib") && arg.trim.equalsIgnoreCase("-find") => controller.playMadlib(requestMadlibByTitle(), requestSavedAs())
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("Madlib") && arg.trim.equalsIgnoreCase("-t") => controller.allTitles()
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("Madlib") && arg.trim.equalsIgnoreCase("-my") => controller.getByUser()
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("Madlib") && arg.trim.equalsIgnoreCase("-by") => controller.getByUser(requestUser())
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("create")  && arg.trim.equalsIgnoreCase("-c") => controller.createConfig(requestConfigInfo())
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("create") && arg.trim.equalsIgnoreCase("-m") => controller.createMadlib(createMadlibInput())
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("create") && arg.trim.equalsIgnoreCase("-mcsv") => controller.createMadlibFromCsv(requestFileName())
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("delete") && arg.trim.equalsIgnoreCase("-t") => controller.deleteMadlib(requestMadlibByTitle())
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("opt") => controller.option()
        case commandArgPattern(cmd, arg) if cmd.equalsIgnoreCase("exit") => controller.exit()
        case notRecognized => controller.notRecognized(notRecognized)
      }
    }
  }


  def createMadlibInput() ={
    (requestTitle(), requestText())
  }

  def requestTitle () : String = {
    println("What would you like to name the title?")
    StdIn.readLine()
  }

  def requestText () ={
    println(
      s"""Please type the text you'd like to use for your Madlib.
         |hint: please format your entries in the text as such <<Noun>>
         |""".stripMargin)
    StdIn.readLine()
  }

  def requestMadlibByTitle(): String = {
    println("What is the title of the madlib you are searching for?")
    StdIn.readLine()
  }

  def requestSavedAs(): String = {
    println("What is the madlib saved as?")
    StdIn.readLine()
  }

  def requestConfigInfo() ={
    println("What would you like to use for your user name?")
    val name = StdIn.readLine()
    val color = reqFavColor()
    List(name, color)
  }

  def reqFavColor(): String = {
    println("What is your favorite color of these?")
    println("Red, Green, Yellow, Blue, Magenta, Cyan")
    val colors= List("red", "green", "yellow", "blue", "magenta", "cyan")
    val pickedColor = StdIn.readLine().toLowerCase().trim
    if (colors.contains(pickedColor)) {
      pickedColor
    } else {
      println("Sorry that color is not available")
      reqFavColor()}
  }

  def requestFileName(): String = {
    println("What is the name of the csv file you'd like to add")
    StdIn.readLine()
  }

  def ansiColorMap (s: String): String = {
   var c = s match {
      case x if x == "red" => AnsiColor.RED
      case x if x == "green" => AnsiColor.GREEN
      case x if x == "yellow" => AnsiColor.YELLOW
      case x if x == "blue" => AnsiColor.BLUE
      case x if x == "magenta" => AnsiColor.MAGENTA
      case x if x == "cyan" => AnsiColor.CYAN
    }
    c
  }


  def requestUser() = {
    println("Who's madlib are you looking for?")
    StdIn.readLine()
  }

}
