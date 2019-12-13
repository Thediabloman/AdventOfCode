package dk.alfabetacain.advent_of_code.day6

object Day6 {

  def puzzle1(): Unit = {
    val lines = io.Source
      .fromFile("advent/resources/day6/puzzle_1.txt")
      .getLines
      .filter(!_.isEmpty)
      .toList

    val graph = buildGraph(lines)

    val withDistances = bfs(graph.get("COM").get, graph)

    println("puzzle1 sum = " + withDistances.values.sum)
  }

  def puzzle2(): Unit = {
    val lines = io.Source
      .fromFile("advent/resources/day6/puzzle_1.txt")
      .getLines
      .filter(!_.isEmpty)
      .toList

    val graph = buildGraph(lines)

    val withDistances = bfs(graph.get("YOU").get, graph)
    // -2 because we move from our orbit to SAN's
    println(
      "puzzle 2 distance: " + (withDistances.get(graph.get("SAN").get).get - 2)
    )
  }

  final case class Vertex(name: String, neighbours: Set[String])

  def buildGraph(lines: List[String]): Map[String, Vertex] = {
    val map = Map.empty
    lines.foldLeft[Map[String, Vertex]](Map.empty) { (map, line) =>
      val parts = line.split(')')
      val first: Vertex =
        map.get(parts(0)).getOrElse(Vertex(parts(0), Set.empty))
      val second: Vertex =
        map.get(parts(1)).getOrElse(Vertex(parts(1), Set.empty))
      (map + (parts(0) -> first.copy(neighbours = first.neighbours + second.name
      ))) + (parts(1) -> second.copy(neighbours = second.neighbours + first.name
      ))
    }
  }

  def bfs(from: Vertex, all: Map[String, Vertex]): Map[Vertex, Int] = {
    var i = 0
    var toBeExplored = Set(from)
    var explored = Map.empty[Vertex, Int]
    while (!toBeExplored.isEmpty) {
      val res = toBeExplored
        .foldLeft[(Map[Vertex, Int], Set[Vertex])]((explored, Set.empty)) {
          case ((m, next), vertex) =>
            m.get(vertex) match {
              case None =>
                (
                  m + (vertex -> i),
                  next ++ vertex.neighbours.map(n => all.get(n).get)
                )
              case Some(_) =>
                (m, next)
            }
        }
      explored = res._1
      toBeExplored = res._2
      i += 1
    }
    explored
  }
}
