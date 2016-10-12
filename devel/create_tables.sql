drop table if exists aggregatesnapshots;
drop table if exists events;
drop table if exists aggregateinfos;

drop table if exists companyaccounts;
drop type if exists accountstatus;
drop table if exists users;
drop table if exists companies;


create table if not exists AggregateInfos(
  id serial primary key,
  atype bytea,
  ts timestamp);
create table if not exists Events(
  id serial primary key,
  etype bytea,
  aggregateId serial references aggregateinfos(id),
  ts timestamp,
  data bytea,
  unique(aggregateId, id));
create index on Events(aggregateId);
create table if not exists AggregateSnapshots(
  id serial primary key,
  lastEventId serial,
  data bytea,
  ts timestamp,
  foreign key(id, lastEventId) references events(aggregateId, id));

create table if not exists Companies(
  id serial primary key,
  name text,
  loginTypes bytea,
  subdomain text unique,
  description text,
  logo text,
  background text,
  headerImage text);
create index on Companies(subdomain);
create table if not exists Users(
  id serial primary key,
  email text unique,
  name text,
  hashedPassword text,
  resetPassword bool,
  emailVerified bool);
create index on Users(email);
create type AccountStatus as enum('Active', 'Invited', 'Terminated');
create table if not exists CompanyAccounts(
  userId serial references Users(id),
  companyId serial references Companies(id),
  photo text,
  joinTime timestamp,
  lastActive timestamp,
  status AccountStatus,
  isAdmin bool,
  isTeacher bool,
  isContentCreator bool,
  primary key(userId, companyId));
create index on CompanyAccounts(userId);
create index on CompanyAccounts(companyId);
