package MadLibs.View

class Options {

}

object Options {
  val message = """ This is a list of your options:
                   | create config : create a new config file for text color
                   | create -t : to add a new Madlib text
                   | delete [id/title] : delete Madlib by id or title
                   | update [id/title]: update a Madlib found by id or title
                   | read [id/title]: read a Madlib found by id or title
                   | random: Will return a random Madlib for you to complete
                   | help -f: How to format your madlib text
                   |
                   | exit : close the application""".stripMargin
}