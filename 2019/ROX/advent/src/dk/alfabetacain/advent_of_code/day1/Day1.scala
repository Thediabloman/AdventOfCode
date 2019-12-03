package dk.alfabetacain.advent_of_code.day1

object Day1 {

  def puzzle1(): Unit = {
    val lines = io.Source.fromFile("advent/resources/day1/puzzle_1.txt").getLines.toList
      .map(_.toInt)

    val result = lines.map(calculateFuel).sum
    println("Puzzle 1 = " + result)

  }

  def puzzle2(): Unit = {

    val lines = io.Source.fromFile("advent/resources/day1/puzzle_1.txt").getLines.toList
      .map(_.toInt)

    val result = lines.map(calculateAndAddForFuel).sum
    println("Puzzle 2 = " + result)
  }

  def calculateFuel(mass: Int): Int = {
    mass / 3 - 2
  }

  def calculateAndAddForFuel(mass: Int): Int = {
    val fuelNeeded = calculateFuel(mass)
    if (fuelNeeded <= 0) {
      0
    } else {
      fuelNeeded + calculateAndAddForFuel(fuelNeeded)
    }
  }


}
