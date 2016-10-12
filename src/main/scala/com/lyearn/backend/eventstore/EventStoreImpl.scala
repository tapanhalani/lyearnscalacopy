package com.lyearn.backend.eventstore

import scalaz._
import Scalaz._
import com.twitter.util.Future
import javax.sql.DataSource
import doobie.imports._
import java.time.LocalDateTime
import doobie.contrib.postgresql.pgtypes._
import java.io.Serializable

class EventStoreImpl[EventData, AggregateData](
    ds: DataSource) extends DoobieUtils {
  type E = Event[EventData, AggregateData]
  type AI = AggregateInfo[AggregateData]
  type AS = AggregateSnapshot[EventData, AggregateData]
  
  implicit def toAI(t: (Long, Serializable, LocalDateTime)) = 
    AggregateInfo[AggregateData](Id[AggregateData](t._1),
             t._2.asInstanceOf[Class[AggregateData]], t._3)
  implicit def toE(t: (Long, Serializable, Long, LocalDateTime, 
      Serializable)) = Event[EventData, AggregateData](Id[EventData](t._1),
           t._2.asInstanceOf[Class[EventData]],
           Id[AggregateData](t._3),
           t._4,
           t._5.asInstanceOf[EventData])
  implicit def toAS(t: (Long, Long, LocalDateTime, Serializable)) =
    AggregateSnapshot[EventData, AggregateData](
              Id[AggregateData](t._1),
              Id[EventData](t._2),
              t._3,
              t._4.asInstanceOf[AggregateData])

  implicit def ciTf[A](prg: ConnectionIO[A]): Future[A] =
    DataSourceTransactor[Future](ds).trans(prg)
  
  def getInfo(id: Id[AggregateData]): Future[Option[AI]] = sql"""select id,
       atype, ts from aggregateinfos where id=${id.id}""".
       query[(Long, Serializable, LocalDateTime)].option.map(_.map(toAI))
  def getEvents(id: Id[AggregateData]): Future[List[E]] = sql"""select id,
     etype, aggregateid, ts, data from events where aggregateid=${id.id}""".
     query[(Long, Serializable, Long, LocalDateTime, Serializable)].list
     .map(_.map(toE))
  def getSnapshot(id: Id[AggregateData]): Future[Option[AS]] = sql"""select id,
    lastEventId, ts, data from aggregatesnapshots where id=${id.id}""".
    query[(Long, Long, LocalDateTime, Serializable)].option.map(_.map(toAS))

  def putInfo(arg: AggregateData): Future[AI] = sql"""insert into
     aggregateinfos(atype, ts) values
     (${arg.getClass().asInstanceOf[Serializable]}, CURRENT_TIMESTAMP)""".
     update.withUniqueGeneratedKeys[(Long, Serializable,
         LocalDateTime)]("id", "atype", "ts").map(toAI _)
  def putEvent(aggregateId: Id[AggregateData],
      eventData: EventData): Future[E] = sql"""insert into
        events(etype, aggregateid, ts, data) values
        (${eventData.getClass().asInstanceOf[Serializable]},
        ${aggregateId.id}, CURRENT_TIMESTAMP,
        ${eventData.asInstanceOf[Serializable]})""".update.
        withUniqueGeneratedKeys[(Long, Serializable, Long, LocalDateTime,
            Serializable)]("id", "etype", "aggregateid", "ts", "data").map(toE _)
  def putSnapshot(aggregateId: Id[AggregateData],
      lastEventId: Id[EventData],
      aggregateData: AggregateData): Future[AS] = sql"""insert into
        aggregatesnapshots(id, lasteventid, data, ts) values
        (${aggregateId.id}, ${lastEventId.id},
        ${aggregateData.asInstanceOf[Serializable]}, CURRENT_TIMESTAMP)
        ON CONFLICT(id) DO UPDATE set
        (lasteventid,data,ts)=(${lastEventId.id},
        ${aggregateData.asInstanceOf[Serializable]},
        CURRENT_TIMESTAMP) where aggregatesnapshots.id=${aggregateId.id}""".update.
        withUniqueGeneratedKeys[(Long, Long, LocalDateTime, Serializable)](
            "id", "lasteventid", "ts", "data").map(toAS _)
}