package com.fpmeetup.talk.prop_based.candy

import com.fpmeetup.talk.prop_based.candy.CandyMachine._
import org.scalacheck.Gen
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{FreeSpec, Matchers}

class CandyMachineTest_PropertyBased extends FreeSpec with GeneratorDrivenPropertyChecks with Matchers {

  val lockedMachine: Gen[Machine] = for {
    coins <- Gen.choose(0, 100)
    candies <- Gen.choose(0, 100)
  } yield Machine(locked = true, candies, coins)

  val unlockedMachine: Gen[Machine] = lockedMachine.map(_.copy(locked = false))
  val machine: Gen[Machine] = Gen.oneOf(lockedMachine, unlockedMachine)
  val outOfCandyMachine: Gen[Machine] = lockedMachine.map(_.copy(candies = 0))

  val operations: Gen[List[Input]] = Gen.listOf(Gen.oneOf(Coin, Turn))

  "On a machine starting locked for every coin and turn we should be missing a com.fpmeetup.talk.prop_based.candy" in {
    forAll(lockedMachine, operations) {
      case (startMachine, inputs) =>

        (startMachine + inputs).candies shouldBe startMachine.candies - countCoinTurnPairs(inputs)

    }
  }

  "An unlocked machine can be turned to a locked machine missing a com.fpmeetup.talk.prop_based.candy using a Turn" in {
    forAll(unlockedMachine, operations) {
      case (startMachine, inputs) =>
        val startMachine_but_LockedAndMissingACandy = startMachine.copy(locked = true, candies = Math.max(startMachine.candies - 1, 0))

        (startMachine + List(Turn)) shouldBe startMachine_but_LockedAndMissingACandy
        (startMachine + (Turn +: inputs)) shouldBe (startMachine_but_LockedAndMissingACandy + inputs)
    }
  }

  "Coins in succession count as only one coin, Turns in succession count as only one turn" in {
    forAll(machine, operations) {
      case (startMachine, inputs) =>
        val reducedInputs = reduceSameItemConsecutiveSequences(inputs)

        (startMachine + inputs) shouldBe (startMachine + reducedInputs)
    }
  }

  "We should have a coin in the machine for every sequence of coins inserted" in {
    forAll(machine, operations) {
      case (startMachine, inputs) =>
        val reducedInputs = reduceSameItemConsecutiveSequences(inputs)
        val expectedIncome = reducedInputs.count(_ == Coin)

        (startMachine + inputs).coins == startMachine.coins + expectedIncome
    }
  }

  "An out of com.fpmeetup.talk.prop_based.candy machine does not change, regardless of input" in {
    forAll(outOfCandyMachine, operations) {
      case (startMachine, inputs) =>

        startMachine + inputs shouldBe startMachine

    }
  }

  "We should never have less than 0 candies OR less coins that we started with" in {
    forAll(machine, operations) {
      case (startMachine, inputs) =>
        val endMachine = startMachine + inputs

        endMachine.candies should be >= 0
        endMachine.coins should be >= startMachine.coins
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
