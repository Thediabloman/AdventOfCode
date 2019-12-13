package dk.alfabetacain.advent_of_code.day3

object Day3 {

  val centralPoint = (0, 0)

  type Point = (Int, Int)

  type PointsToSteps = Map[Point, Int]

  def puzzle1(): Unit = {
    /*
    test1()
    test2()
    test3()
     */
    val (first, second) = loadInput()

    val firstPath = findPath(first)
    val secondPath = findPath(second)

    val res = secondPath.keySet
      .filter(firstPath.contains)
      .map(manhattanDistanceToCentralPoint)
      .min
    println("res = " + res)

  }

  def puzzle2(): Unit = {
    val (first, second) = loadInput()

    val firstPath = findPath(first)
    val secondPath = findPath(second)

    val res = secondPath
      .filter(entry => firstPath.contains(entry._1))
      .map {
        case (point, steps) =>
          (manhattanDistanceToCentralPoint(point), steps + firstPath(point))
      }
      .minBy(entry => entry._2)
      ._2
    println("res = " + res)
  }

  def loadInput(): (List[String], List[String]) = {
    val lines =
      io.Source.fromFile("advent/resources/day3/puzzle_1.txt").getLines.toList
    (lines.head.split(",").toList, lines.tail.head.split(",").toList)
  }

  def test1(): Unit = {
    val firstPath = findPath("R8,U5,L5,D3".split(",").toList)
    val secondPath = findPath("U7,R6,D4,L4".split(",").toList)
    val res = secondPath.keySet
      .filter(firstPath.contains)
      .map(manhattanDistanceToCentralPoint)
      .min
    println("res = " + res)
  }

  def test2(): Unit = {
    val firstPath = findPath(
      "R75,D30,R83,U83,L12,D49,R71,U7,L72".split(",").toList
    )
    println("first path = " + firstPath.mkString(", "))
    val secondPath = findPath(
      "U62,R66,U55,R34,D71,R55,D58,R83".split(",").toList
    )
    println("second path = " + secondPath.mkString(", "))
    val res = secondPath.keySet
      .filter(firstPath.contains)
      .map(manhattanDistanceToCentralPoint)
      .min
    println("res = " + res)
  }
  def test3(): Unit = {
    val firstPath = findPath(
      "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51".split(",").toList
    )
    val secondPath = findPath(
      "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7".split(",").toList
    )
    val res = secondPath.keySet
      .filter(firstPath.contains)
      .map(manhattanDistanceToCentralPoint)
      .min
    println("res = " + res)
  }

  def findPath(args: List[String]): PointsToSteps = {
    args
      .foldLeft[(Point, PointsToSteps)](((0, 0), Map.empty)) {
        case ((currentPos, path), command) =>
          val direction = command.charAt(0)
          val length = command.substring(1).toInt
          //val (newPoint, _, map) = step(direction, length, currentPos, path)
          val (newPoint, _, map) = calculateNewPoints(length, currentPos, path)(
            determineOperations(direction)
          )
          (newPoint, map)
      }
      ._2
  }

  def determineOperations(command: Char): (Point, Int) => Point = {
    command match {
      case 'U' => { case (point, delta) => (point._1, point._2 + delta) }
      case 'D' => { case (point, delta) => (point._1, point._2 - delta) }
      case 'R' => { case (point, delta) => (point._1 + delta, point._2) }
      case 'L' => { case (point, delta) => (point._1 - delta, point._2) }
    }
  }

  def calculateNewPoints(steps: Int, from: Point, pointMap: PointsToSteps)(
      op: (Point, Int) => Point
  ): (Point, Int, PointsToSteps) = {
    val stepsSoFar = if (from == (0, 0)) 0 else pointMap(from)
    val points = List
      .range(1, steps + 1)
      .map(x => (op(from, x), stepsSoFar + x))
    val newAdditions = points.filter(x => !pointMap.contains(x._1))
    val last = points.last
    (last._1, last._2, pointMap ++ newAdditions)
  }

  def manhattanDistanceToCentralPoint(a: Point): Int =
    manhattanDistance(centralPoint, a)

  def manhattanDistance(a: Point, b: Point): Int = {
    Math.abs(a._1 - b._1) + Math.abs(a._2 - b._2)
  }
}
