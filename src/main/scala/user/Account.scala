package user

import company.CompanyId
import java.util.UUID
import org.joda.time.DateTime
import java.net.URL

case class Account (id: AccountId, companyId: CompanyId, photo: URL,
    status: AccountStatus, joinDate: DateTime, lastUpdated: DateTime)
case class AccountId (id: UUID)
sealed trait AccountStatus
case object Active extends AccountStatus
case object Invited extends AccountStatus
case object Terminated extends AccountStatus