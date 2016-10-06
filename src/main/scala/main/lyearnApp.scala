package main;

import java.util.UUID;
import user.{User, Email, AccountId}
import database.ProductionDb;

object Main {
  def main(args: Array[String]): Unit = {
    val Ids = List.fill(3)(UUID.randomUUID()).map(AccountId)
    val user1 = User(Email("nithinreddyt@gmail.com"), "nithin", "password", Ids, false, false)
    val db = ProductionDb
    //db.userModel store user1
  }
}