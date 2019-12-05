package dk.alfabetacain.advent_of_code

object IntCode {

  type Program = Array[Int]

  type InstructionOutput = (Option[Int], Boolean)

  val instructions: Map[String, Instruction] = Map(
    "01" -> Code01,
    "02" -> Code02,
    "03" -> Code03,
    "04" -> Code04,
    "05" -> Code05,
    "06" -> Code06,
    "07" -> Code07,
    "08" -> Code08,
    "99" -> Code99
  )

  sealed trait Instruction {
    def run(parameters: List[Parameter], program: Program): InstructionOutput
    def numberOfParameters: Int
  }
  case object Code01 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter], program: Program): InstructionOutput = parameters match {
      case param1 :: param2 :: param3 :: Nil =>
        program(param3.position()) = param1.value() + param2.value()
        (None, false)
    }
  }

  case object Code02 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter], program: Program): InstructionOutput = parameters match {
      case param1 :: param2 :: param3 :: Nil =>
        program(param3.position()) = param1.value() * param2.value()
        (None, false)
    }
  }
  case object Code03 extends Instruction {
    val numberOfParameters = 1
    override def run(parameters: List[Parameter], program: Program): InstructionOutput = parameters match {
      case param1 :: Nil =>
        println("Input number...")
        val input = scala.io.StdIn.readLine().trim().toInt
        program(param1.position()) = input
        (None, false)
    }
  }
  case object Code04 extends Instruction {
    val numberOfParameters = 1
    override def run(parameters: List[Parameter], program: Program): InstructionOutput = parameters match {
      case p :: Nil =>
        println(p.value())
        (None, false)
    }
  }

  case object Code05 extends Instruction {
    val numberOfParameters = 2
    override def run(parameters: List[Parameter], program: Program): InstructionOutput = parameters match {
      case param1 :: param2 :: Nil =>
        val newIp = if (param1.value() > 0) Some(param2.value()) else None
        (newIp, false)
    }
  }
  case object Code06 extends Instruction {
    val numberOfParameters = 2
    override def run(parameters: List[Parameter], program: Program): InstructionOutput = parameters match {
      case param1 :: param2 :: Nil =>
        val newIp = if (param1.value() == 0) Some(param2.value()) else None
        (newIp, false)
    }
  }
  case object Code07 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter], program: Program): InstructionOutput = parameters match {
      case param1 :: param2 :: param3 :: Nil =>
        program(param3.position()) = if (param1.value() < param2.value()) 1 else 0
        (None, false)
    }
  }
  case object Code08 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter], program: Program): InstructionOutput = parameters match {
      case param1 :: param2 :: param3 :: Nil =>
        program(param3.position()) = if (param1.value() == param2.value()) 1 else 0
        (None, false)
    }
  }
  case object Code99 extends Instruction {
    val numberOfParameters = 0
    override def run(parameters: List[Parameter], program: Program): InstructionOutput = 
      (None, true)
  }
  sealed trait Parameter {
    def resolve(): Int = value()
    def value(): Int
    def position(): Int
  }
  final case class Position(program: Program, index: Int) extends Parameter {
    override def value(): Int = program(program(index))
    override def position(): Int = program(index)
  }
  final case class Immediate(program: Program, index: Int) extends Parameter {
    override def value(): Int = program(index)
    override def position(): Int = index
  }

  def run(program: Program): Unit = {
    var i = 0
    var halt = false
    while (!halt) {
      val commandString = program(i).toString
      val opcode = 
        if (commandString.length == 1) 
          commandString.takeRight(1).padTo(2, '0').reverse 
        else 
          commandString.takeRight(2)
      val parameterModes = 
        if (commandString.length == 1) 
          commandString.dropRight(1) 
        else 
          commandString.dropRight(2)
      val instruction = instructions(opcode)
      val parameters = getParameters(parameterModes, i, instruction.numberOfParameters, program)
      val step = instruction.numberOfParameters+1 // for the opcode
      val (ip, shouldHalt) = instruction.run(parameters, program)
      halt = shouldHalt
      ip match {
        case None => 
          i += step
        case Some(v) =>
          i = v
      }
    }
  }

  def getParameters(parameterModes: String, base: Int, length: Int, program: Program): List[Parameter] = {
    val padded = parameterModes.reverse.padTo(length, '0')
    padded.toCharArray().map(_.toString.toInt).toList.zip(List.range(base+1, base+1+length)).map{
      case (0, x) => Position(program, x)
      case (1, x) => Immediate(program, x)
      case x => 
        println(s"Unknown mode: $x")
        ???
    }
  }
}
