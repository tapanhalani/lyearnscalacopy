package com.lyearn.backend.groups

case class GroupId(id: Long)
case class GroupEventId(id: Long)
case class GroupData[T](elems: Set[T])

sealed trait GroupEventData[T] extends Product with Serializable
case class CreateGroup[T](objectType: Class[T]) extends GroupEventData[T]
case class DestroyGroup[T]() extends GroupEventData[T]
case class AddObjectToGroup[T](objectId: T) extends GroupEventData[T]
case class RemoveObjectFromGroup[T](objectId: T) extends GroupEventData[T]