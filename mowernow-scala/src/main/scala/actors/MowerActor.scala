package actors

import akka.actor.{Actor, ActorLogging, Props}
import model.{Command, Forward, Mower, Position}

class MowerActor extends Actor with ActorLogging {

  def receive = {
    case MowerMessages.ExecuteCommands(mower: Mower, commands: List[Command]) =>
      log.info(s"===> Executing instructions for $mower")
      if (commands.isEmpty) {
        context.parent ! MowerMessages.AllCommandsExecuted(mower)
      } else {
        commands.head match {
          case command@Forward =>
            log.debug(s"===> Going forward, current:$command")
            val newState = mower.forward
            context.parent ! MowerMessages.RequestAuthorisation(mower, newState, commands)

          case command@_ =>
            log.debug(s"===> Rotating, remaining:${commands.tail}")
            val newState = mower.rotate(command)
            self ! MowerMessages.ExecuteCommands(newState, commands.tail)
        }

      }

    case MowerMessages.PositionAllowed(mower: Mower, commands: List[Command]) =>
      log.info(s"===> Position ${mower.pos} authorized, remaining:$commands")
      self ! MowerMessages.ExecuteCommands(mower, commands.tail)

    case MowerMessages.PositionRejected(mower: Mower, commands: List[Command]) =>
      log.info(s"===> Position ${mower.pos} rejected!!")
      self ! MowerMessages.ExecuteCommands(mower, commands)

    case MowerMessages.PrintPosition =>
      log.info(s"===> Current position ")

    case MowerMessages.TerminateProcessing =>
      context stop self

  }

}

object MowerMessages {

  case class ExecuteCommands(mower: Mower, commands: List[Command])

  case class RequestAuthorisation(currentState: Mower, newState: Mower, commands: List[Command])

  case class PositionAllowed(mower: Mower, commands: List[Command])

  case class PositionRejected(mower: Mower, commands: List[Command])

  case object PrintPosition

  case object TerminateProcessing

  case class AllCommandsExecuted(mower: Mower)

}

object MowerActor {

  def props(): Props = Props(classOf[MowerActor])

}
