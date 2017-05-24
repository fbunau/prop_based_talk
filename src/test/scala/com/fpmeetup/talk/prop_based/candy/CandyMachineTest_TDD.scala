package com.fpmeetup.talk.prop_based.candy

import com.fpmeetup.talk.prop_based.candy.CandyMachine._
import org.scalatest.{FreeSpec, Matchers}

class CandyMachineTest_TDD extends FreeSpec with Matchers {

  "Inserting a coin into a locked machine will cause it to unlock if there’s any com.fpmeetup.talk.prop_based.candy left" in {
    simulateInputs(List(Coin))
      .run(Machine(locked=true, 1, 0)).value._1 shouldBe Machine(locked=false, 1, 1)
  }

  "Inserting coins in an unlocked machine has the same result as a single coin" in {
    simulateInputs(List(Coin, Coin))
      .run(Machine(locked=true, 1, 0)).value._1 shouldBe Machine(locked=false, 1, 1)
  }

  "Turning the knob on an unlocked machine will cause it to dispense com.fpmeetup.talk.prop_based.candy and become locked" in {
    simulateInputs(List(Coin, Turn))
      .run(Machine(locked=true, 1, 0)).value._1 shouldBe Machine(locked=true, 0, 1)
  }

  "Turning the knob on a locked machine does nothing" in {
    simulateInputs(List(Turn))
      .run(Machine(locked=true, 1, 0)).value._1 shouldBe Machine(locked=true, 1, 0)
  }

  "Inserting a coin into an unlocked machine does nothing" in {
    simulateInputs(List(Coin))
      .run(Machine(locked=false, 1, 0)).value._1 shouldBe Machine(locked=false, 1, 0)
  }

  "A machine that’s out of com.fpmeetup.talk.prop_based.candy ignores all inputs" - {
    "com.fpmeetup.talk.prop_based.candy.Machine is locked, and we turn" in {
      simulateInputs(List(Turn))
        .run(Machine(locked=true, 0, 42)).value._1 shouldBe Machine(locked=true, 0, 42)
    }

    "com.fpmeetup.talk.prop_based.candy.Machine is unlocked and we turn" in {
      simulateInputs(List(Turn))
        .run(Machine(locked=false, 0, 42)).value._1 shouldBe Machine(locked=false, 0, 42)
    }

    "com.fpmeetup.talk.prop_based.candy.Machine is locked and we insert coin" in {
      simulateInputs(List(Coin))
        .run(Machine(locked=true, 0, 42)).value._1 shouldBe Machine(locked=true, 0, 42)
    }

    "com.fpmeetup.talk.prop_based.candy.Machine is unlocked and we insert coin" in {
      simulateInputs(List(Coin))
        .run(Machine(locked=false, 0, 42)).value._1 shouldBe Machine(locked=false, 0, 42)
    }

  }
}
