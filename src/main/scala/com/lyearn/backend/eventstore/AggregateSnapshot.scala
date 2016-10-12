package com.lyearn.backend.eventstore

import java.time.LocalDateTime

case class AggregateSnapshot[EventData, AggregateData](
  id: Id[AggregateData],
  lastEventId: Id[EventData],
  ts: LocalDateTime,
  data: AggregateData
)