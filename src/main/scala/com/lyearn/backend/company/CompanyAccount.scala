package com.lyearn.backend.company

import com.lyearn.backend.user.UserId
import java.util.UUID
import java.net.URL
import java.time.LocalDateTime

case class CompanyAccount(
    userId: UserId,
    companyId: CompanyId,
    status: Enumeration,
    photo: URL,
    joinTime: LocalDateTime,
    lastActive: LocalDateTime,
    isAdmin: Boolean,
    isTeacher: Boolean,
    isContentCreator: Boolean);
object Status extends Enumeration { val Active, Invited, Terminated = Value }