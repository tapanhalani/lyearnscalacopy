enum LoginType {
  EMAIL_LOGIN = 1,
  GOOGLE_LOGIN = 2
}

enum LyearnBackendError {
  USER_ALREADY_EXISTS_IN_COMPANY = 1,
  EXISTING_COMPANY_WITH_SUBDOMAIN = 2,
  NO_USER_FOUND = 3,
  NO_COMPANY_FOUND = 4
}

exception LyearnBackendException {
  1: LyearnBackendError code,
  2: optional string message
}

struct CreateCompanyInput {
  1: string companyName,
  2: list<LoginType> loginTypes,
  3: string subdomain,
  4: optional string description,
  5: optional string logoUrl,
  6: optional string backgroundUrl,
  7: optional string headerImageUrl,
  8: string adminEmail,
  9: optional string adminName
}

struct CreateCompanyOutput {
  1: i64 companyId,
  2: i64 adminId
}

struct CreateCompanyAccountInput {
  1: i64 companyId,
  2: string email,
  3: optional string name,
  4: bool isAdmin = false,
  5: bool isTeacher = false,
  6: bool isContentCreator = false
}

service CompanyService {
   CreateCompanyOutput createCompany(1:CreateCompanyInput input) throws (1: LyearnBackendException e),
   i64 createCompanyAccount(1:CreateCompanyAccountInput input) throws (1: LyearnBackendException e),
   void terminateCompanyAccount(1:i64 userId, 2:i64 companyId) throws (1: LyearnBackendException e),
   list<i64> listActiveUsers(1:i64 companyId) throws (1: LyearnBackendException e),
   list<i64> listCompanies(1:i64 userId) throws  (1: LyearnBackendException e),
   bool existsCompanyAccount(1:i64 companyId, 2:i64 userId) throws (1: LyearnBackendException e)
}
