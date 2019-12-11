package dk.alfabetacain.advent_of_code

object IntCode {

  final case class IntCodeState(program: Program, instructionPointer: Int, inputs: List[Int], outputs: List[Int]) {
    def getInput: (Int, IntCodeState) = {
      inputs match {
        case h :: t => 
          (h, this.copy(inputs = t))
        case Nil =>
          println("Input number...")
          (scala.io.StdIn.readLine().trim().toInt, this)
      }
    }

    def addOutput(output: Int): IntCodeState = {
      this.copy(outputs = outputs ++ List(output))
    }
  }

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
    def run(parameters: List[Parameter], state: IntCodeState): IntCodeState
    def numberOfParameters: Int
  }
  case object Code01 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter], state: IntCodeState): IntCodeState = parameters match {
      case param1 :: param2 :: param3 :: Nil =>
        state.program(param3.position()) = param1.value() + param2.value()
        state
    }
  }

  case object Code02 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter], state: IntCodeState): IntCodeState = parameters match {
      case param1 :: param2 :: param3 :: Nil =>
        state.program(param3.position()) = param1.value() * param2.value()
        state
    }
  }
  case object Code03 extends Instruction {
    val numberOfParameters = 1
    override def run(parameters: List[Parameter], state: IntCodeState): IntCodeState = parameters match {
      case param1 :: Nil =>
        val (input, newState) = state.getInput
        newState.program(param1.position()) = input
        newState
    }
  }
  case object Code04 extends Instruction {
    val numberOfParameters = 1
    override def run(parameters: List[Parameter], state: IntCodeState): IntCodeState = parameters match {
      case p :: Nil =>
        println(p.value())
        state.addOutput(p.value())
    }
  }

  case object Code05 extends Instruction {
    val numberOfParameters = 2
    override def run(parameters: List[Parameter], state: IntCodeState): IntCodeState = parameters match {
      case param1 :: param2 :: Nil =>
        val newIp = if (param1.value() > 0) Some(param2.value()) else None
        newIp.map(ip => state.copy(instructionPointer = ip)).getOrElse(state)
    }
  }
  case object Code06 extends Instruction {
    val numberOfParameters = 2
    override def run(parameters: List[Parameter], state: IntCodeState): IntCodeState = parameters match {
      case param1 :: param2 :: Nil =>
        val newIp = if (param1.value() == 0) Some(param2.value()) else None
        newIp.map(ip => state.copy(instructionPointer = ip)).getOrElse(state)
    }
  }
  case object Code07 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter], state: IntCodeState): IntCodeState = parameters match {
      case param1 :: param2 :: param3 :: Nil =>
        state.program(param3.position()) = if (param1.value() < param2.value()) 1 else 0
        state
    }
  }
  case object Code08 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter], state: IntCodeState): IntCodeState = parameters match {
      case param1 :: param2 :: param3 :: Nil =>
        state.program(param3.position()) = if (param1.value() == param2.value()) 1 else 0
        state
    }
  }
  case object Code99 extends Instruction {
    val numberOfParameters = 0
    override def run(parameters: List[Parameter], state: IntCodeState): IntCodeState = 
      state.copy(instructionPointer = -1)
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
    override def position(): Int = ???
  }

  def run(program: Program, inputs: List[Int] = List.empty): List[Int] = {
    var state = IntCodeState(program, 0, inputs, List.empty)
    var i = 0
    var halt = false
    while (state.instructionPointer != -1) {
      val commandString = program(state.instructionPointer).toString
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
      val parameters = getParameters(parameterModes, state.instructionPointer, instruction.numberOfParameters, program)
      val step = instruction.numberOfParameters+1 // for the opcode
      val newState = instruction.run(parameters, state)
      if (newState.instructionPointer == state.instructionPointer) {
        state = newState.copy(instructionPointer = state.instructionPointer + step)
      } else {
        // ip changed
        state = newState
      }
    }
    state.outputs
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
