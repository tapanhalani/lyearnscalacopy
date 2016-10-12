package com.lyearn.backend.eventstore

import doobie.imports._
import scalaz._
import Scalaz._

case class Id[Data](id: Long)