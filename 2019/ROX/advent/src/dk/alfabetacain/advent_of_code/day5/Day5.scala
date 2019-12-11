package dk.alfabetacain.advent_of_code.day5

import dk.alfabetacain.advent_of_code.IntCode

object Day5 {

  def puzzle1(): Unit = {
    val input = io.Source.fromFile("advent/resources/day5/puzzle_1.txt").getLines.toList.head

    val program = input.split(",").map(_.toInt)

    println("Running...")
    println("Input 1")
    IntCode.run(program, List(1))

    println("Done")
  }

  def puzzle2(): Unit = {

    val input = io.Source.fromFile("advent/resources/day5/puzzle_1.txt").getLines.toList.head

    val program = input.split(",").map(_.toInt)

    println("Running...")
    println("Input 5")
    IntCode.run(program)

    println("Done")
  }

}
