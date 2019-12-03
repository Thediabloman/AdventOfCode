package dk.alfabetacain.advent_of_code.day1

import org.scalatest.{FlatSpec, Matchers}

class Day1Test extends FlatSpec with Matchers {
  "Day1" should "work" in {
    Day1.run()
    println("hello world")
    1 should be (1)
  }

  it should "do magic" in {
    Day1.run()
  }
}
