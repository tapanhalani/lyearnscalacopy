/*drop table if exists aggregateSnapshots;
drop table if exists events;
drop table if exists aggregates;*/

create table if not exists Aggregates(
  id serial primary key,
  type int,
  ts timestamp);
create table if not exists Events(
  id serial primary key,
  aggregateId serial references aggregates(id),
  ts timestamp,
  data bytes,
  unique index(aggregateId, id DESC));
create table if not exists AggregateSnapshots(
  id serial primary key,
  lastEventId serial,
  data bytes,
  ts timestamp,
  constraint valid_snapshot foreign key(id, lastEventId) references events(aggregateId, id),
  index(id, lastEventId));
