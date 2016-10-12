package com.lyearn.backend.main

import doobie.imports._
import scalaz._, Scalaz._
import scalaz.concurrent.Task
import org.mindrot.jbcrypt.BCrypt
import com.typesafe.config.ConfigFactory
import java.util.UUID
import java.nio.ByteBuffer
import java.util.Date
import java.sql.Timestamp
import java.util.TimeZone
import java.util.Arrays
import com.lyearn.backend.company.CompanyServiceImpl
import com.twitter.util.Future
import com.lyearn.backend.company.CompanyServiceImpl
import thrift.LyearnBackendException
import com.twitter.finagle.Thrift
import com.twitter.util.Await
import thrift.CreateCompanyInput
import thrift._
import com.zaxxer.hikari.HikariDataSource
import org.postgresql.ds.PGSimpleDataSource
import org.postgresql.ds.PGConnectionPoolDataSource
import org.postgresql.ds.PGPoolingDataSource
import java.util.Properties
import com.zaxxer.hikari.HikariConfig
import com.lyearn.backend.eventstore.EventStoreImpl
import com.lyearn.backend.groups.GroupEventData
import com.lyearn.backend.groups.GroupData
import com.lyearn.backend.groups.CreateGroup

object db {
  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()
    val isProd: Boolean = BCrypt.checkpw(config.getString("lyearn-prod.pass"),
        config.getString("lyearn-prod.hashPass"));
    val isDev: Boolean = !isProd
    if (isProd) {
      
    } else if (isDev) {
      val prop = new Properties()
      prop.setProperty("dataSourceClassName", "org.postgresql.ds.PGPoolingDataSource")
      prop.setProperty("dataSource.serverName", "159.203.86.69")
      prop.setProperty("dataSource.databaseName", "lyearn")
      prop.setProperty("dataSource.user", "lyearnbackend")
      prop.setProperty("dataSource.password", "test12345")
      val config = new HikariConfig(prop)
      val hds = new HikariDataSource(config)
      val esi = new EventStoreImpl[GroupEventData[Int], GroupData[Int]](hds)
      val aif = esi.putInfo(GroupData[Int](Set.empty))
      val ai = Await.result(aif)
      val ef = esi.putEvent(ai.id, CreateGroup[Int](3.getClass()))
      val e = Await.result(ef)
      val sf = esi.putSnapshot(ai.id, e.id, GroupData[Int](Set(1,2,3)))
      val s = Await.result(sf)
      println(Await.result(esi.getInfo(ai.id)))
      println(Await.result(esi.getEvents(ai.id)))
      println(Await.result(esi.getSnapshot(ai.id)))
      //val gai = es
      //val csi = new CompanyServiceImpl(hds)
      //val service = Thrift.server.serveIface("localhost:8428", csi)
      //Await.ready(service)
    }   
  } 
}