package user;

import scala.concurrent.Future
import java.util.UUID
import net.liftweb.json.DefaultFormats
import com.websudos.phantom.dsl._

abstract class UserModel extends CassandraTable[UserModel, User] with RootConnector {
  implicit val formats = DefaultFormats;
  override def tableName: String = "users"

  object email extends StringColumn(this) with PartitionKey[String] // how to solve this? 
  object name extends StringColumn(this)
  object hashedPassword extends StringColumn(this)
  object resetPassword extends BooleanColumn(this)
  object emailVerified extends BooleanColumn(this)
  object accountIds extends ListColumn[UUID](this)

  override def fromRow(row: Row): User = {
    User(
      Email(email(row)),
      name(row),
      hashedPassword(row),
      accountIds(row) map AccountId,
      resetPassword(row),
      emailVerified(row)
      )
  }
  
  def getByEmail(e: String): Future[Option[User]] = {
    select
      .where(_.email eqs e)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .one()
  }

  def store(user: User): Future[ResultSet] = {
    insert
      .value(_.email, user.email.email)
      .value(_.name, user.name)
      .value(_.accountIds, user.accountIds.map(_.id))
      .value(_.resetPassword, user.resetPassword)
      .value(_.emailVerified, user.emailVerified)
      .value(_.hashedPassword, user.hashedPassword)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()
  }

  def deleteByEmail(e: String): Future[ResultSet] = {
    delete
      .where(_.email eqs e)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()
  }  
}

//  object email extends JsonColumn[Email](this) with PartitionKey[Email]{
//    override def fromJson(obj: String): Email = {
//      JsonParser.parse(obj).extract[Email]
//    } 
//    override def toJson(obj: Email): String = {
//      compactRender(Extraction.decompose(obj)) // Extract.decompose, decomposes a case class into Json(JValue
//  }