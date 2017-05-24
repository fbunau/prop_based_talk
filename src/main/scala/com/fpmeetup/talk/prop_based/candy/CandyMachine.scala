package com.fpmeetup.talk.prop_based.candy

import cats.data.State

sealed trait Input

case object Coin extends Input
case object Turn extends Input

case class Machine(locked: Boolean, candies: Int, coins: Int)

object CandyMachine {

  def update: (Input) => (Machine) => Machine =
    (i: Input) => (s: Machine) => {

      (i, s) match {
        case (_, Machine(_, 0, _)) => s

        case (Coin, Machine(false, _, _)) => s

        case (Turn, Machine(true, _, _)) => s

        case (Coin, Machine(true, candy, coin)) =>
          Machine(locked = false, candy, coin + 1)

        case (Turn, Machine(false, candy, coin)) =>
          Machine(locked = true, candy - 1, coin)
    }

  }


  def sequence[S,A](l: List[State[S, A]]): State[S, List[A]] =
    l.reverse.foldLeft(State[S, List[A]](s => (s, List())))((acc, s) => s.map2(acc)(_ :: _ ))

  def simulateInputs(inputs: List[Input]): State[Machine, (Int, Int)] = {
    val machines: List[State[Machine, Unit]] =
      inputs.map(i => State.modify(update(i)))

    val sequenced = sequence(machines)

    sequenced.flatMap(x => State.get.map(s => (s.coins, s.candies)))
  }

  implicit class MachineWithOps(machine: Machine) {

    def +(inputs: List[Input]): Machine = {
      simulateInputs(inputs).run(machine).value._1
    }

  }

}
