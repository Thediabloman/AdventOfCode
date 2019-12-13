package dk.alfabetacain.advent_of_code

import cats._
import cats.data._
import cats.implicits._

object IntCode {

  final case class IntCodeState(
      program: Program,
      instructionPointer: Int,
      inputs: List[Int],
      outputs: List[Int]
  ) {}

  type IntCodeStateM[A] = State[IntCodeState, A]

  def getInput: IntCodeStateM[Int] = State { s =>
    s.inputs match {
      case h :: t =>
        (s.copy(inputs = t), h)
      case Nil =>
        println("Input number...")
        (s, scala.io.StdIn.readLine().trim().toInt)
    }
  }

  def addOutput(output: Int): IntCodeStateM[Unit] = State { s =>
    (s.copy(outputs = s.outputs ++ List(output)), ())
  }

  def updateInstructionPointer(
      instructionPointer: Option[Int]
  ): IntCodeStateM[Boolean] = instructionPointer match {
    case None => State.pure[IntCodeState, Boolean](false)
    case Some(ip) =>
      for {
        _ <- State.modify[IntCodeState](_.copy(instructionPointer = ip))
      } yield true
  }

  def updateIndex(index: Int, value: Int): IntCodeStateM[Unit] =
    State.modify[IntCodeState] { state =>
      state.program(index) = value
      state
    }

  def incrementInstructionPointer(step: Int): IntCodeStateM[Unit] = {
    for {
      s <- State.get[IntCodeState]
      _ <- State.set(s.copy(instructionPointer = s.instructionPointer + step))
    } yield ()
  }

  def halt: IntCodeStateM[Unit] = updateInstructionPointer(-1.some).map(_ => ())

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
    def run(parameters: List[Parameter]): IntCodeStateM[Unit]
    def numberOfParameters: Int
    def incrementInstructionPointer: IntCodeStateM[Unit] = {
      IntCode.incrementInstructionPointer(numberOfParameters + 1).map(_ => ())
    }
  }
  case object Code01 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter]): IntCodeStateM[Unit] = {
      parameters match {
        case param1 :: param2 :: param3 :: Nil =>
          for {
            _ <- updateIndex(param3.position(), param1.value() + param2.value())
            _ <- incrementInstructionPointer
          } yield ()
      }
    }
  }
  case object Code02 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter]): IntCodeStateM[Unit] =
      parameters match {
        case param1 :: param2 :: param3 :: Nil =>
          for {
            _ <- updateIndex(param3.position(), param1.value() * param2.value())
            _ <- incrementInstructionPointer
          } yield ()
      }
  }
  case object Code03 extends Instruction {
    val numberOfParameters = 1
    override def run(parameters: List[Parameter]): IntCodeStateM[Unit] =
      parameters match {
        case param1 :: Nil =>
          for {
            input <- getInput
            s <- State.get[IntCodeState]
            _ = s.program(param1.position()) = input
            _ <- incrementInstructionPointer
          } yield ()
      }
  }
  case object Code04 extends Instruction {
    val numberOfParameters = 1
    override def run(parameters: List[Parameter]): IntCodeStateM[Unit] =
      parameters match {
        case p :: Nil =>
          println(p.value())
          for {
            _ <- addOutput(p.value())
            _ <- incrementInstructionPointer
          } yield ()
      }
  }

  case object Code05 extends Instruction {
    val numberOfParameters = 2
    override def run(parameters: List[Parameter]): IntCodeStateM[Unit] =
      parameters match {
        case param1 :: param2 :: Nil =>
          val newIp = if (param1.value() > 0) Some(param2.value()) else None
          for {
            updated <- updateInstructionPointer(newIp)
            _ <- if (updated) State.pure[IntCodeState, Unit](())
            else incrementInstructionPointer
          } yield ()
      }
  }
  case object Code06 extends Instruction {
    val numberOfParameters = 2
    override def run(parameters: List[Parameter]): IntCodeStateM[Unit] =
      parameters match {
        case param1 :: param2 :: Nil =>
          val newIp = if (param1.value() == 0) Some(param2.value()) else None
          for {
            updated <- updateInstructionPointer(newIp)
            _ <- if (updated) State.pure[IntCodeState, Unit](())
            else incrementInstructionPointer
          } yield ()
      }
  }
  case object Code07 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter]): IntCodeStateM[Unit] =
      parameters match {
        case param1 :: param2 :: param3 :: Nil =>
          for {
            _ <- updateIndex(
              param3.position(),
              if (param1.value() < param2.value()) 1 else 0
            )
            _ <- incrementInstructionPointer
          } yield ()
      }
  }
  case object Code08 extends Instruction {
    val numberOfParameters = 3
    override def run(parameters: List[Parameter]): IntCodeStateM[Unit] =
      parameters match {
        case param1 :: param2 :: param3 :: Nil =>
          for {
            _ <- updateIndex(
              param3.position(),
              if (param1.value() == param2.value()) 1 else 0
            )
            _ <- incrementInstructionPointer
          } yield ()
      }
  }
  case object Code99 extends Instruction {
    val numberOfParameters = 0
    override def run(parameters: List[Parameter]): IntCodeStateM[Unit] =
      halt
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

  def run(
      program: Program,
      inputs: List[Int] = List.empty,
      instructionPointer: Int = 0
  ): List[Int] = {
    var state = IntCodeState(program, instructionPointer, inputs, List.empty)
    runProgram.run(state).value._1.outputs
  }
  def runProgram: IntCodeStateM[Unit] = {
    for {
      s <- State.get[IntCodeState]
      ip = s.instructionPointer
      _ <- if (ip == -1) {
        State.pure[IntCodeState, Unit](())
      } else {
        for {
          parsed <- parseCommand(ip)
          (opcode, parameterModes) = parsed
          instruction = instructions(opcode)
          parameters <- getParameters(
            parameterModes,
            ip,
            instruction.numberOfParameters
          )
          _ <- instruction.run(parameters)
          s <- State.get[IntCodeState]
          _ <- runProgram
        } yield ()
      }
    } yield ()
  }

  def parseCommand(ip: Int): IntCodeStateM[(String, String)] = {
    State { state =>
      val commandString = state.program(state.instructionPointer).toString
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
      (state, (opcode, parameterModes))
    }
  }

  def getParameters(
      parameterModes: String,
      base: Int,
      length: Int
  ): IntCodeStateM[List[Parameter]] = {
    val padded = parameterModes.reverse.padTo(length, '0')
    for {
      s <- State.get[IntCodeState]
    } yield padded
      .toCharArray()
      .map(_.toString.toInt)
      .toList
      .zip(List.range(base + 1, base + 1 + length))
      .map {
        case (0, x) => Position(s.program, x)
        case (1, x) => Immediate(s.program, x)
        case x =>
          println(s"Unknown mode: $x")
          ???
      }
  }
}
