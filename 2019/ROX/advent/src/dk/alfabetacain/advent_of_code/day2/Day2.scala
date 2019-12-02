package dk.alfabetacain.advent_of_code.day2

object Day2 {

  def test(): Unit = {
    val program = "1,9,10,3,2,3,11,0,99,30,40,50".split(",").map(_.toInt).toArray
    println("Before: " + program.mkString(","))
    runIntcodeProgram(program)
    println("After: " + program.mkString(","))
  }

  def readInput(): Array[Int] = {
    io.Source.fromFile("advent/resources/day2/puzzle_1.txt").getLines.flatMap(_.split(","))
      .map(_.toInt).toArray
  }
  def puzzle1(): Unit = {
    val program = readInput()

    println("Before = " + program.mkString(","))
    program(1) = 12
    program(2) = 2
    runIntcodeProgram(program)
    println("Position 0 = " + program(0))
  }
  def puzzle2(): Unit = {
    val originalProgram = readInput()

    val expected = 19690720
    val inputs = Stream.from(0).take(100).flatMap(s => Stream.from(0).take(100).map((s,_)))
    val res = inputs.map{ in =>
      val program = originalProgram.clone()
      program(1) = in._1
      program(2) = in._2
      runIntcodeProgram(program)
      (in, program(0))
    }.filter(_._2 == expected).head
    println("res = " + res)
    println("Transformed = " + (100 * res._1._1 + res._1._2))
  }

  def runIntcodeProgram(input: Array[Int]): Unit = {
    var i = 0
    var continue = true
    while (continue) {
      continue = input(i) match {
        case 99 =>
          false
        case 1 =>
          ops(i, input)(_ + _)
          true
        case 2 =>
          ops(i, input)(_ * _)
          true
      }
      i += 4
    }
  }

  def ops(base: Int, input: Array[Int])(operator: (Int, Int) => Int): Unit = {
    val firstArg = input(input(base+1))
    val secondArg = input(input(base+2))
    val destination = input(base+3)
    input(destination) = operator(firstArg, secondArg)
  }
}
