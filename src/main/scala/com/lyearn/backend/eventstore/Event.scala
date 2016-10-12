package com.lyearn.backend.eventstore

import java.util.UUID
import java.time.LocalDateTime

case class Event[EventData, AggregateData] (
  id: Id[EventData],
  eType: Class[EventData],
  aggregateId: Id[AggregateData],
  ts: LocalDateTime,
  data: EventData
)