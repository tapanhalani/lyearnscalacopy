package com.lyearn.backend.eventstore

import doobie.imports._
import org.apache.commons.lang3.SerializationUtils
import scalaz._
import Scalaz._
import com.twitter.util.Future
import java.time.LocalDateTime
import java.sql.Timestamp
import java.io.Serializable

trait DoobieUtils {
  implicit val SerializableMeta: Meta[Serializable] = Meta[Array[Byte]].nxmap(
    SerializationUtils.deserialize[Serializable],
    x => SerializationUtils.serialize((x.asInstanceOf[Serializable])))
  implicit val LocalDateTimeMeta: Meta[LocalDateTime] = Meta[Timestamp].nxmap(
    _.toLocalDateTime(), Timestamp.valueOf(_))
  implicit val classMeta: Meta[Class[_]] = Meta[Array[Byte]].nxmap(
    SerializationUtils.deserialize[Class[_]],
    x => SerializationUtils.serialize((x.asInstanceOf[Serializable])))
  //implicit val idMeta: Meta[Id[_]] = Meta[Long].nxmap(Id(_), _.id)
  implicit val mF = new Object with Monad[Future] {
    def point[A](a: => A): Future[A] = Future.value(a)
    def bind[A, B](fa: Future[A])(f: A => Future[B]): Future[B] =
      fa.flatMap(f)
  }
  implicit val cF = new Object with Catchable[Future] {
    def attempt[A](f: Future[A]): Future[Throwable \/ A] =
      f.flatMap { x => Future.value(x.right[Throwable]) }.handle({
        case e => e.left[A]
      })
    def fail[A](err: Throwable): Future[A] = throw err
  }
  implicit val cF2: Capture[Future] = new Capture[Future] {
    def apply[A](a: => A): Future[A] = Future.value(a)
  }
}