package MadLibs.Controller

import MadLibs.View._
import MadLibs.Model._
import MadLibs.util.FileUtil.{readCSV, writeCSV}
import MadLibs.util.MongoDb
import org.mongodb.scala.MongoWriteException

import scala.io.StdIn

class Controller() {


  val dbClient = new MongoDb()

  def welcome (): Unit = View.messageOut( Welcome.message() )

  def exit(): Unit = {
    View.messageOut(Exit.message)
    dbClient.mongoClient.close()
    Cli.continueMenuLoop = false
  }

  def option (): Unit = View.messageOut(Options.message)

  def notRecognized (command: String): Unit =  View.messageOut(NoCommandFound.message(command))

  /** Create a Madlib **/
  def createMadlib( inp: (String, String) ): Unit ={

    val (title, text) = inp
    try {
      dbClient.createMadlib(MadlibModel(title, text, Cli.user))
      println(s"""Sucessfully created Madlib
        |Title: $title
        |Text: "$text"
        |""".stripMargin)

    }
    catch {
      case e: MongoWriteException => { println(
        """*** Title already exists ***
          |Please try again with a different title.
          |""".stripMargin )
        createMadlib((Cli.requestTitle(), text))
      }

    }
  }


  def allTitles(): Unit = dbClient.getAllTitles()

  def getByUser(user: String = Cli.user) = {
    println("These are the titles for all your created Madlibs.")
    dbClient.getByUser(user).foreach(x=> println(x.title))
  }

  def playMadlib (title:String) = {
    val madLib = dbClient.getByTitle(title).head
    val answers = madLib.generateAnswers()
    println( madLib.interpolateMadlib(answers.answeredBlanks) )
    val solutionsWithoutPreviousSave = madLib.solutions match {
      case Some(i) => i.filter(_.savedAs != answers.savedAs)
      case None => List()
    }
    dbClient.updateAnswers(title, solutionsWithoutPreviousSave:+answers)
  }

  def playMadlib (title:String, savedAs: String) = {
    val madLib = dbClient.getByTitle(title).head
     val savedMadlib = (madLib.solutions match {
      case Some(i) => i.filter(x => x.savedAs == savedAs && x.userName == Cli.user)
    })

    println( madLib.interpolateMadlib(savedMadlib.head.answeredBlanks) )
  }

  def createConfig(text: List[String]): Unit = {
    writeCSV("config.csv", text)
  }

  def createMadlibFromCsv(name:String) ={
    readCSV(name).foreach(x => createMadlib(x(0), x(1)))
  }

  def deleteMadlib(name: String) = {
    println("Warning this will also delete all child records of this document. Are you sure youd like to continue? y/n")
    if (StdIn.readLine() == "y") dbClient.deleteByTitle(name) else println("Deletion Aborted")

  }

}



