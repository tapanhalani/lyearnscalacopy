package company

import java.util.UUID
import user.AccountId
import java.net.URL

case class Company(id: CompanyId, name: String, adminAccounts: List[AccountId],
    description: String, logo: URL, background: URL,
    loginTypes: List[LoginType], headerImage: URL, subdomain: String)

case class CompanyId private (id: UUID)

sealed trait LoginType
case object GoogleLogin extends LoginType
case object EmailLogin extends LoginType