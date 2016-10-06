package user

import java.util.UUID
import java.net.URL
import org.joda.time.DateTime

case class User (email: Email, name: String, hashedPassword: String,
    accountIds: List[AccountId], resetPassword: Boolean, emailVerified: Boolean)
case class Email (email: String)





