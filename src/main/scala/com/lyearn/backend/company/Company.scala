package com.lyearn.backend.company

import java.util.UUID
import java.net.URL

case class Company(id: CompanyId, name: String, loginTypes: List[Enumeration],
    subdomain: String, description: Option[String], logo: Option[URL],
    background: Option[URL], headerImage: Option[URL])
case class CompanyId (id: UUID)

object LoginType extends Enumeration { val GoogleLogin, EmailLogin = Value}