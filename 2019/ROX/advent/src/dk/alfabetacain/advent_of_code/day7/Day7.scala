package dk.alfabetacain.advent_of_code.day7

import dk.alfabetacain.advent_of_code.IntCode

object Day7 {

  def puzzle1(): Unit = {
    val program = io.Source
      .fromFile("advent/resources/day7/puzzle_1.txt")
      .getLines
      .filter(!_.isEmpty)
      .toList
      .head
      .split(",")
      .map(_.toInt)

    val test =
      "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0"
        .split(",")
        .map(_.toInt)

    val highestSignal = combinations(Set.empty).map { combs =>
      val settings = combs.toArray
      var i = 0
      var previous = 0
      while (i < 5) {
        println(s"Running program $i")
        previous = IntCode.run(program.clone, List(settings(i), previous)).head
        println(s"Program $i output = $previous")
        i += 1
      }
      previous
    }.max
    println(s"Highest signal = $highestSignal")
  }

  def combinations(previous: Set[Int]): Set[List[Int]] = {
    val diff = Set(0, 1, 2, 3, 4) -- previous
    if (diff.isEmpty) {
      Set(List.empty)
    } else {
      diff.flatMap { next =>
        combinations(previous + next).map(next :: _)
      }
    }
  }

  def puzzle2(): Unit = {}

}
