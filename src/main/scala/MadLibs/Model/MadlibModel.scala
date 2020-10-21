package MadLibs.Model

import MadLibs.Controller.Cli
import org.bson.types.ObjectId

import scala.io.{AnsiColor, StdIn}

case class MadlibModel(_id: ObjectId, title: String, user: String, text: String, blanks: List[textBlanksModel], solutions: Option[List[AnswersModel]] ) {





  /** fill in madlib with provided answers **/
  def interpolateMadlib (answers: List[textBlanksModel]): String = {
    val parsedtext = MadlibModel.parseMadlibInputText(text)
    val e = answers.map(x=> x.answers match {
      case Some(i) => s"${i}"
      case None => s"<<${x.key}>>"
    })
//    println(ptext)
//    println(s"this is e: ${e}")
//    println(ptext.format(e: _*) )
    parsedtext.format(e: _*)
  }

  def generateAnswers(): AnswersModel = {
    val answerList = blanks.map( x=> {
      println(s"Pick a ${x.key}")
      val answer = Some(StdIn.readLine())
      textBlanksModel(x.key,answer)
    })
    println("What would you like to save this solution as?")
    val saveAs = StdIn.readLine()
    AnswersModel(user, saveAs, answerList)
  }
}

object MadlibModel {

  def apply( title:String , text: String, user: String): MadlibModel = new MadlibModel( new ObjectId(), title, user ,text, parseMadlibBlanksIntoList(text), None)

  /** Parses given text into MadlibEntries */
  def parseMadlibBlanksIntoList(text: String): List[textBlanksModel] = {
    val reg  = "<<(.*?)>>".r
    val t = reg.findAllMatchIn(text).toList
//    println( (for (ms <- t) yield ms.group(1) ).map(textBlanksModel(_, None)))
    ((for (ms <- t) yield ms.group(1) ).map(textBlanksModel(_, None)))

  }

  /** Format Madlib Text from <<key>> to s% for .format interpolation */
  def parseMadlibInputText (text: String): String = {
    val reg  = "<<.*?>>".r
    val t = reg.replaceAllIn(text, "%s")
    t
  }
}

