package MadLibs.Model

import org.bson.types.ObjectId

case class AnswersModel(_id: ObjectId, userName: String, savedAs: String, answeredBlanks: List[textBlanksModel] )

object AnswersModel {
  def apply (userName: String, savedAs: String, answeredBlanks: List[textBlanksModel] ) = { new AnswersModel(new ObjectId(), userName, savedAs, answeredBlanks ) }
}



