package com.fpmeetup.talk.prop_based.candy

import com.fpmeetup.talk.prop_based.candy.CandyMachine._
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FreeSpec, Matchers}

class CandyMachineTest_Param_Commented extends FreeSpec with TableDrivenPropertyChecks with Matchers {

  val cases = Table(
    ("start", "inputs", "expectedOutput"),
    (Machine(locked=true, 1, 0), List(Coin), Machine(locked=false, 1, 1)),
    (Machine(locked=true, 1, 0), List(Coin, Coin), Machine(locked=false, 1, 1)),
    (Machine(locked=true, 1, 0), List(Coin, Turn), Machine(locked=true, 0, 1)),
    (Machine(locked=true, 1, 0), List(Turn), Machine(locked=true, 1, 0)),
    (Machine(locked=false, 1, 0), List(Coin), Machine(locked=false, 1, 0)),
    (Machine(locked=true, 0, 42), List(Turn), Machine(locked=true, 0, 42)),
    (Machine(locked=false, 0, 42), List(Turn), Machine(locked=false, 0, 42)),
    (Machine(locked=true, 0, 42), List(Coin), Machine(locked=true, 0, 42)),
    (Machine(locked=false, 0, 42), List(Coin), Machine(locked=false, 0, 42))
  )

  /**
    * Combinations :
    *
    * true * 0 Coin -> +1 coin
    * true * 0 Coin Coin -> +1 coin
    * true * 0 Coin Turn -> -1 com.fpmeetup.talk.prop_based.candy +1 coin
    * true * 0 Turn -> same
    *
    * false * 0 Coin -> same
    *
    * true 0 * Turn -> same
    * true 0 * Coin -> same
    *
    * false 0 * Turn -> same
    * false 0 * Coin -> same
    *
    * !! Missing : !!
    *
    * true * 0 Turn Coin
    * true * 0 Turn Turn
    *
    * false * Turn !
    * false * Coin
    * false * Coin Turn
    * false * Turn Coin
    *
    * true 0 * Turn Coin
    * true 0 * Coin Turn
    *
    * ....
    */

  "Run cases" in {
    forAll(cases) {
      (startMachine: Machine, inputs: List[Input], expectedOutput) => {
        simulateInputs(inputs)
          .run(startMachine).value._1 shouldBe expectedOutput
      }
    }
  }

}
