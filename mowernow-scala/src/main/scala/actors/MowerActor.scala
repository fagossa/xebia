package actors

import actors.MowerActor.{PositionRejected, PositionAutorised, ExecuteCommands}
import akka.actor.{Actor, Props}
import model.{Command, Mower, Position}

class MowerActor extends Actor {

  def receive = {
    case ExecuteCommands(mower: Mower, commands: List[Command]) =>
      println(s"===> Executing instructions for $mower")

    case PositionAutorised =>
      // TODO: advance mower
    
    case PositionRejected =>
    // TODO: try one more time
    
  }

}

object MowerActor {

  case class ExecuteCommands(mower: Mower, commands: List[Command])

  case class RequestPosition(current: Position, previous: Position)
  
  case object PositionAutorised
  
  case object PositionRejected

  case class AllCommandsExecuted(mower: MowerActor)

  def props(): Props = Props(classOf[MowerActor])

}
