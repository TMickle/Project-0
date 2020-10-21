package MadLibs.util

import MadLibs.Model.{AnswersModel, MadlibModel, textBlanksModel}
import org.mongodb.scala.{MongoClient, MongoCollection, Observable}

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Projections._

class MongoDb {
  val mongoClient = MongoClient()
  private val codecRegistry = fromRegistries(fromProviders(classOf[MadlibModel], classOf[textBlanksModel], classOf[AnswersModel]), MongoClient.DEFAULT_CODEC_REGISTRY)

  private val db = mongoClient.getDatabase("Madlibs").withCodecRegistry(codecRegistry)

  private val collection : MongoCollection[MadlibModel] = db.getCollection("Madlibs")

  private def getResults[T](obs: Observable[T]): Seq[T] = {
    Await.result(obs.toFuture(), Duration(10, SECONDS))
  }

  /** print results from Mongo for debugging */
  def printResults[T](obs: Observable[T]): Unit = {
    getResults(obs).foreach(println(_))
  }


  /** Retrieve all Madlibs */
  private def getAll() = collection.find()


  def getAllTitles() = (getResults(collection.find()).foreach( x => println(x.title) ))

  def getByTitle(title: String) = getResults( collection.find(equal("title", title)))


  def getByUser (user: String) = getResults( collection.find(equal("user", user)))

  def updateAnswers(title: String, answers: List[AnswersModel]) = {getResults(collection.updateOne(equal("title", title), set("solutions", answers ) ) )}

  def createMadlib (madlib: MadlibModel) : Unit = { getResults( collection.insertOne(madlib) )}

  def deleteByTitle(title: String) = getResults( collection.deleteOne(equal("title", title)))




}



