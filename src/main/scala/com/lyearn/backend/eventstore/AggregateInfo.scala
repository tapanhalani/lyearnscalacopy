package com.lyearn.backend.eventstore

import java.time.LocalDateTime

/** Information about the Aggregate with @id.
 *  @ts is the creation timestamp of the aggregate*/
case class AggregateInfo[AggregateData](
    id: Id[AggregateData], aType: Class[AggregateData], ts: LocalDateTime)