package com.lyearn.backend.company

import thrift._
import scalaz._
import Scalaz._
import doobie.imports._
import doobie.contrib.postgresql.pgtypes._
import com.twitter.util.Future
import javax.sql.DataSource
import com.lyearn.backend.eventstore.DoobieUtils
import java.io.Serializable

class CompanyServiceImpl(ds: DataSource) extends CompanyService[Future] with DoobieUtils {
  implicit val AccountStatusAtom = pgEnum(Status, "AccountStatus")
  def createCompanyC(input: CreateCompanyInput): ConnectionIO[CreateCompanyOutput] = {
    val desc = input.description.getOrElse("")
    val logo = input.logoUrl.getOrElse("")
    val back = input.backgroundUrl.getOrElse("")
    val head = input.headerImageUrl.getOrElse("")
    for {
      _ <- sql"select id from companies where subdomain = ${input.subdomain}".
        query[Long].option.map(x => x match {
          case None => true
          case Some(v) => throw LyearnBackendException(
            LyearnBackendError.ExistingCompanyWithSubdomain,
            None)
        })
      companyId <- sql"""insert into companies(name, loginTypes, subdomain,
             description, logo, background, headerImage) values
             (${input.companyName},
             ${input.loginTypes.asInstanceOf[Serializable]},
             ${input.subdomain}, $desc, $logo, $back, $head)""".
        update.withUniqueGeneratedKeys[Long]("id")
      adminId <- createCompanyAccountC(CreateCompanyAccountInput(companyId,
        input.adminEmail, input.adminName, true, false, false))
    } yield (CreateCompanyOutput(companyId, adminId))
  }

  def createUserIfNecessaryC(email: String, name: Option[String]): ConnectionIO[Long] = for {
    uId1 <- getUserIdIfExists(email)
    uId <- uId1 match {
      case None => sql"""insert into Users(email, name, resetPassword,
         emailVerified) values ($email, ${name.getOrElse("")}, true, false)"""
        .update.withUniqueGeneratedKeys[Long]("id")
      case Some(v) => v.point[ConnectionIO]
    }
  } yield (uId)
  
  def getUserIdIfExists(email: String): ConnectionIO[Option[Long]] =
    sql"select id from Users where email=$email".query[Long].option
    
  def existsUserC(userId:Long): ConnectionIO[Boolean] =
    sql"select count(*) from Users where id=$userId".query[Int].
    unique.map(_.==(1))

  def existsCompanyC(companyId:Long): ConnectionIO[Boolean] =
    sql"select count(*) from Companies where id=$companyId".query[Long].
    unique.map(_.==(1))
  def existsCompanyAccountC(companyId: Long, userId: Long): ConnectionIO[Boolean] = 
    sql"""select count(*) from CompanyAccounts where
       userId=$userId and companyId=$companyId""".query[Int].unique.map(_.==(1))

  def createCompanyAccountC(input: CreateCompanyAccountInput): ConnectionIO[Long] = for {
    userId <- createUserIfNecessaryC(input.email, input.name)
    eCA <- existsCompanyAccountC(input.companyId, userId)
    _ <- if (eCA) {
      throw LyearnBackendException(
        LyearnBackendError.UserAlreadyExistsInCompany, None)
      Unit.point[ConnectionIO]
    } else sql"""insert into CompanyAccounts(userId, companyId, joinTime,
         status, isAdmin, isTeacher, isContentCreator)
         values ($userId, ${input.companyId}, CURRENT_TIMESTAMP, 'Invited',
         ${input.isAdmin}, ${input.isTeacher}, ${input.isContentCreator})""".
      update.run
  } yield (userId)
  
  
  def listCompaniesC(userId: Long): ConnectionIO[Seq[Long]] = for {
    doesExist <- existsUserC(userId)
    allAccounts <- if(!doesExist){
       throw LyearnBackendException(
        LyearnBackendError.NoUserFound, None)
        List[Long]().point[ConnectionIO]
    } else sql""" select companyId from CompanyAccounts where userId=$userId""".query[Long].list
  } yield (allAccounts)
  
  def listActiveUsersC(companyId: Long): ConnectionIO[Seq[Long]] = for {
    doesExist <- existsCompanyC(companyId)
    allAccounts <- if(!doesExist){
       throw LyearnBackendException(
        LyearnBackendError.NoCompanyFound, None)
        List[Long]().point[ConnectionIO]
    } else sql""" select userId from CompanyAccounts where companyId=$companyId AND status !='Terminated'""".query[Long].list  
    
    } yield (allAccounts)
    
  def terminateCompanyAccountC(userId: Long, companyId: Long): ConnectionIO[Unit] = for {
    doesExist <- existsUserC(userId)
    _ <- if(!doesExist){
       throw LyearnBackendException(
        LyearnBackendError.NoUserFound, None)
        List[Long]().point[ConnectionIO]
    } else sql"""update CompanyAccounts SET status = 'Terminated' where 
      userId=$userId AND companyId=$companyId""".update.run
    } yield ()

  def createCompany(input: CreateCompanyInput): Future[CreateCompanyOutput] =
    DataSourceTransactor[Future](ds).trans(createCompanyC(input))
  def createCompanyAccount(input: CreateCompanyAccountInput): Future[Long] =
    DataSourceTransactor[Future](ds).trans(createCompanyAccountC(input))
  def existsCompanyAccount(companyId: Long, userId: Long): Future[Boolean] =
    DataSourceTransactor[Future](ds)
      .trans(existsCompanyAccountC(companyId, userId))
  def listCompanies(userId: Long): com.twitter.util.Future[Seq[Long]] = 
    DataSourceTransactor[Future](ds)
      .trans(listCompaniesC(userId))
  def listActiveUsers(companyId: Long): com.twitter.util.Future[Seq[Long]] = 
      DataSourceTransactor[Future](ds)
      .trans(listActiveUsersC(companyId))
  def terminateCompanyAccount(userId: Long, companyId: Long): com.twitter.util.Future[Unit] = 
        DataSourceTransactor[Future](ds)
      .trans(terminateCompanyAccountC(userId,companyId))
}
