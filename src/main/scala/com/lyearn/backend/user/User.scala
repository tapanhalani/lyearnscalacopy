package com.lyearn.backend.user

import java.util.UUID

case class User (id: UserId, email: String, name: String,
    hashedPassword: String, resetPassword: Boolean, emailVerified: Boolean)
case class UserId(id: UUID)





