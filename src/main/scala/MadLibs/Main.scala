package MadLibs

import MadLibs.Controller.Cli
import MadLibs.util.FileUtil.{readCSV, readCSVConfig, writeCSV}


object Main extends App {
  val config = readCSVConfig("config.csv")
  Cli.run(config)
}
