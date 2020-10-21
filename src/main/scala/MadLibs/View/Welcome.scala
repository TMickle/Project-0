package MadLibs.View

import io.AnsiColor._
import MadLibs.Controller.Cli

class Welcome {

}

object Welcome {
  val message = () => {
    s"""${BOLD}${Cli.color}Welcome to a MadLibs CLI app ${Cli.user}, I hope you have fun!
      |Type 'opt' for a list of common [Options]
      |""".stripMargin
  }
}
