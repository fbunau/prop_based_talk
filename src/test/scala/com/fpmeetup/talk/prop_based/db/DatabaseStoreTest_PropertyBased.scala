package com.fpmeetup.talk.prop_based.db

import org.scalacheck.Gen
import org.scalatest.{FreeSpec, Matchers}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class DatabaseStoreTest_PropertyBased extends FreeSpec with GeneratorDrivenPropertyChecks with Matchers {

  val unicodeChar: Gen[Char] = Gen.oneOf((Char.MinValue to Char.MaxValue).filter(Character.isDefined))
  val stringInput: Gen[String] = Gen.listOf(unicodeChar).map(_.mkString)

  "Storing is symmetrical with retrieval" in {
    forAll(stringInput) { input =>

      val db = new DatabaseStore()
      db.put(input)

      input shouldBe db.get()

    }

  }

}
