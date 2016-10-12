package com.lyearn.backend.company

import java.util.UUID
import com.lyearn.backend.course.CourseId
import com.lyearn.backend.eventstore.Event
import com.lyearn.backend.user.UserId
import java.time.LocalDateTime

/*sealed trait CompanyStructureEvent extends
  Event[CompanyId, CompanyStructureEventId] {
}

//sealed trait CompanyStructureMetadata extends EventMetadata
case class CompanyStructureEventId(id: UUID)
case class TeamId(id: UUID)

sealed trait ChildId
case class ObjectTeamId(id: TeamId) extends ChildId
case class ObjectAutoAssignCourseId(id: CourseId) extends ChildId
case class ObjectEnrollableCousreId(id: CourseId) extends ChildId
case class ObjectUserId(id: UserId) extends ChildId

case class AddedRootTeam(id: CompanyStructureEventId,
    aggregateId: CompanyId, ts: LocalDateTime, rootId: TeamId)
    extends CompanyStructureEvent
case class AddObjectToTeam(id: CompanyStructureEventId,
    aggregateId: CompanyId, ts: LocalDateTime, parentId: TeamId, childId: ChildId)
    extends CompanyStructureEvent
case class RemoveObjectFromTeam(id: CompanyStructureEventId,
    aggregateId: CompanyId, ts: LocalDateTime, parentId: TeamId, childId: ChildId)
    extends CompanyStructureEvent
*/
/*case class AddedChildTeam(eventId: CompanyStructureEventId,
    companyId: CompanyId, parentId: TeamId, childId: TeamId)
    extends CompanyStructureEvents
case class RemovedChildTeam(eventId: CompanyStructureEventId,
    companyId: CompanyId, parentId: TeamId, childId: TeamId)
    extends CompanyStructureEvents
case class AddedAutoAssignCourseToTeam(eventId: CompanyStructureEventId,
    companyId: CompanyId, teamId: TeamId, courseId: CourseId)
    extends CompanyStructureEvents
case class RemovedAutoAssignCourseFromTeam(eventId: CompanyStructureEventId,
    companyId: CompanyId, teamId: TeamId, courseId: CourseId)
    extends CompanyStructureEvents
case class AddedEnrollableCourseToTeam(eventId: CompanyStructureEventId,
    companyId: CompanyId, teamId: TeamId, courseId: CourseId)
    extends CompanyStructureEvents
case class RemovedEnrollableCourseFromTeam(eventId: CompanyStructureEventId,
    companyId: CompanyId, teamId: TeamId, courseId: CourseId)
    extends CompanyStructureEvents
case class AddedAccountToTeam(eventId: CompanyStructureEventId,
    companyId: CompanyId, teamId: TeamId, accountId: AccountId)
    extends CompanyStructureEvents
case class RemovedAccountFromTeam(eventId: CompanyStructureEventId,
    companyId: CompanyId, teamId: TeamId, accountId: AccountId)
    extends CompanyStructureEvents*/