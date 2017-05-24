package com.fpmeetup.talk.prop_based.candy

import com.fpmeetup.talk.prop_based.candy.CandyMachine._
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FreeSpec, Matchers}

class CandyMachineTest_PropertyBased_Wrong extends FreeSpec with GeneratorDrivenPropertyChecks with Matchers {

  val lockedMachine: Gen[Machine] = for {
    coins <- Gen.choose(0, 100)
    candies <- Gen.choose(0, 100)
  } yield Machine(locked = true, candies, coins)

  val unlockedMachine: Gen[Machine] = lockedMachine.map(_.copy(locked = false))
  val machine: Gen[Machine] = Gen.oneOf(lockedMachine, unlockedMachine)
  val outOfCandyMachine: Gen[Machine] = lockedMachine.map(_.copy(candies = 0))

  val operations: Gen[List[Input]] = Gen.listOf(Gen.oneOf(Coin, Turn))

  "An unlocked machine is equivalent with a locked machine if a coin is inserted before processing inputs" in {
    forAll(unlockedMachine, operations) {
      case (startMachineUnlocked, inputs) =>

        println(startMachineUnlocked)
        println(inputs)

        val startMachineLocked = startMachineUnlocked.copy(locked = true)

        startMachineLocked + (Coin +: inputs) shouldBe startMachineUnlocked + inputs
    }
  }

  "On a machine starting locked for every coin and turn we should have a coin in the machine" in {
    forAll(lockedMachine, operations) {
      case (startMachine, inputs) =>

        println(startMachine)
        println(inputs)

        (startMachine + inputs).coins shouldBe startMachine.coins - countCoinTurnPairs(inputs)

    }
  }

  "An unlocked machine can be turned to a locked machine missing a com.fpmeetup.talk.prop_based.candy using a Turn" in {
    forAll(unlockedMachine, operations) {
      case (startMachine, inputs) =>
        println(startMachine)
        println(inputs)

        val startMachineMissingACandy = startMachine.copy(candies = Math.max(startMachine.candies - 1, 0))

        (startMachine + (Turn +: inputs)) shouldBe startMachineMissingACandy
    }
  }

  private def countCoinTurnPairs(input: List[Input]): Int = {
    input.sliding(2).foldRight(0) {
      case (List(Coin, Turn), acc) => acc + 1
      case (_, acc) => acc
    }
  }

  private def reduceSameItemConsecutiveSequences(input: List[Input]): List[Input] = {
    input.foldLeft(List[Input]()) {
      case (acc@List(_, x), y) if x == y => acc
      case (acc, x) => acc :+ x
    }
  }

}
