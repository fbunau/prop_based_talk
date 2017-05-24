package com.fpmeetup.talk.prop_based.db

import org.scalatest.{FreeSpec, Matchers}

class DatabaseStoreTest_ExampleBased extends FreeSpec with Matchers {

  "Test that storage works" in {
    val db = new DatabaseStore()

    db.put("foobar")

    db.get() shouldBe "foobar"
  }

}
