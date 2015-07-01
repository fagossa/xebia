package actors

import actors.MowerActor._
import akka.actor.{Actor, ActorLogging, Props}
import model.{Command, Forward, Mower, Position}

class MowerActor extends Actor with ActorLogging {

  def receive = {
    case ExecuteCommands(mower: Mower, commands: List[Command]) =>
      log.info(s"===> Executing instructions for $mower")
      if (commands.isEmpty) {
        context.parent ! AllCommandsExecuted(mower)
      } else {
        commands.head match {
          case command@Forward =>
            log.debug(s"===> Going forward, current:$command ,remaining:${commands.tail}")
            val newState = mower.forward
            context.parent ! RequestPosition(newState.pos, mower.pos)

          case command@_ =>
            log.debug(s"===> Rotating, remaining:${commands.tail}")
            val newState = mower.rotate(command)
            self ! ExecuteCommands(newState, commands.tail)
        }

      }

    case PositionAutorised =>
      // TODO: advance mower
      log.info(s"===> Position authorized")

    case PositionRejected =>
      // TODO: try one more time
      log.info(s"===> Position rejected")

    case PrintPosition =>
      log.info(s"===> Current position ")
  }

}

object MowerActor {

  case class ExecuteCommands(mower: Mower, commands: List[Command])

  case class RequestPosition(current: Position, previous: Position)

  case object PositionAutorised

  case object PositionRejected

  case object PrintPosition

  case class AllCommandsExecuted(mower: Mower)

  def props(): Props = Props(classOf[MowerActor])

}
