package MadLibs.util

import java.io.{BufferedWriter, File, FileNotFoundException, FileWriter}

import MadLibs.Controller.Cli

import scala.collection.mutable.ArrayBuffer
import scala.collection.immutable.Map
import scala.io.{BufferedSource, Source}
import scala.util.Try
import com.github.tototoshi.csv._



object FileUtil {

  def readCSVConfig(name: String): Map[String,String] = {
    try {
      val bufferedSource = Source.fromFile(name)
      val arr = ArrayBuffer[String]()
      for (line <- bufferedSource.getLines.drop(1)) {
        val Array(user, color) = line.split(",").map(_.trim)
        arr += (user, color)
      }
      bufferedSource.close

      Map("user" -> arr(0), "color" -> arr(1))
    } catch {
      case e: FileNotFoundException if name == "config.csv" => {
        println("It appears you do not have a config.csv file")
        writeCSV("config.csv", Cli.requestConfigInfo())
        readCSVConfig("config.csv")
      }

    }
  }

  def writeCSV(name: String, text: List[String]) ={
    Try {
      val fileWriter = CSVWriter.open(new File(name))
      fileWriter.writeAll( List( List("User","Color"), text))
      fileWriter.close()
    }.toEither match {
      case Left(ex) =>
        println(ex)
      case Right(_) =>
        println(s"${name} was succefully written.")

    }
  }

  def readCSV(fileName: String): List[List[String]] = {

      val reader = CSVReader.open(new File(fileName))
      val results = reader.all()
      reader.close
      results


  }
}


