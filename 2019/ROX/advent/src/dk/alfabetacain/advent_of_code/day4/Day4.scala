package dk.alfabetacain.advent_of_code.day4

object Day4 {

  val regex = """([0-9])\1""".r

  def puzzle1(): Unit = {
    val res = Stream
      .from(372037)
      .take(905157 - 372037)
      .map(_.toString)
      .filter(increases)
      .filter(hasPair)
      .size
    println("res = " + res)
  }

  def noTriple(input: List[Char]): Boolean = {
    input match {
      case h1 :: h2 :: h3 :: _ if h1 == h2 && h2 != h3                  => true
      case _ :: _ :: _ :: h1 :: h2 :: h3 :: Nil if h1 != h2 && h2 == h3 => true
      case _ =>
        input.sliding(4).exists {
          case h1 :: h2 :: h3 :: h4 :: Nil =>
            h2 == h3 && h1 != h2 && h3 != h4
        }
    }
  }

  def increases(input: String): Boolean = {
    //println(s"Input = $input")
    val res = input.toCharArray
      .foldLeft(('0', true)) {
        case ((prev, cond), current) =>
          (current, cond && current >= prev)
      }
      ._2
    res
  }

  def hasPair(input: String): Boolean = {
    regex.findFirstIn(input).isDefined
  }

  def puzzle2(): Unit = {
    val res = Stream
      .from(372037)
      .take(905157 - 372037)
      .map(_.toString)
      .filter(increases)
      .filter(hasPair)
      .filter(x => noTriple(x.toCharArray.toList))
      .size
    println("res = " + res)
  }

}
