package actors

import java.util.UUID

import actors.MowerMessages._
import actors.SurfaceActor.SurfaceConfig
import actors.SurfaceMessages.BeginProcessing
import akka.actor._
import model.{Command, Mower, Position, Surface}

class SurfaceActor(val sur: Surface, initialState: SurfaceConfig) extends Actor with Stash with ActorLogging {

  var children = Set.empty[ActorRef]

  var usedPositions = Set.empty[Position]

  override def preStart(): Unit = {
    super.preStart()
    context become ready
  }

  def receive = PartialFunction.empty

  def ready: Receive = {
    case BeginProcessing =>
      log.info(s"===> BeginProcessing")
      initialState foreach (key => {
        val mowerId = s"Mower-${UUID.randomUUID().toString}"
        val mowerRef = context.actorOf(MowerActor.props(), mowerId)
        children += mowerRef
        log.info(s"===> Handling actor $mowerRef")
        mowerRef ! ExecuteCommands(mower = key._1, commands = key._2)
      })
      context become working
  }

  def working: Receive = {
    case RequestAuthorisation(currentState: Mower, newState: Mower, remainingCommands: List[Command]) =>
      log.info(s"===> RequestPosition")
      usedPositions contains currentState.pos match {
        case true => sender() ! PositionRejected
        case false =>
          sender() ! PositionAllowed(currentState, remainingCommands: List[Command])
          usedPositions -= currentState.pos
          usedPositions += newState.pos
      }

    case AllCommandsExecuted(mower: Mower) =>
      log.info(s"All commands executed for <$mower> ...")

    case SurfaceMessages.PrintSystemState =>
      children foreach (_ ! PrintPosition)

    case TerminateProcessing =>
      children foreach (_ ! TerminateProcessing)
      context stop self
      context become ready
  }

}

object SurfaceMessages {

  object PrintSystemState

  object BeginProcessing

}

object SurfaceActor {

  type SurfaceConfig = Map[Mower, List[Command]]

  def props(surface: Surface, initialState: SurfaceConfig): Props =
    Props(classOf[SurfaceActor], surface, initialState)
}
