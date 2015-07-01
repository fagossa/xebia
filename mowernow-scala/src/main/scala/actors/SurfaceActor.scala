package actors

import actors.MowerActor._
import actors.SurfaceActor.{BeginProcessing, PrintSystemState, SurfaceConfig, TerminateProcessing}
import akka.actor._
import com.typesafe.config.ConfigFactory
import model.{Command, Mower, Position, Surface}

class SurfaceActor(val sur: Surface, initialState: SurfaceConfig) extends Actor with ActorLogging {

  val config = ConfigFactory.load()
  val system = ActorSystem("system", config)
  var children = scala.collection.mutable.ListBuffer[ActorRef]()

  var usedPositions = scala.collection.mutable.TreeSet[Position]()

  def receive = {
    case BeginProcessing =>
      log.info(s"===> BeginProcessing")
      initialState.foreach(key => {
        val mowerRef = system.actorOf(MowerActor.props())
        children += mowerRef
        mowerRef ! ExecuteCommands(mower = key._1, commands = key._2)
      })

    case RequestPosition(current: Position, previous: Position) =>
      log.info(s"===> RequestPosition")
      if (usedPositions.contains(current)) {
        sender() ! PositionRejected
      } else {
        sender() ! PositionAutorised
        usedPositions -= previous
        usedPositions += current
      }

    case AllCommandsExecuted(mower: Mower) =>
      log.info(s"All commands executed for <$mower> ...")

    case PrintSystemState =>
      children.foreach { elem =>
        elem ! PrintPosition
      }

    case TerminateProcessing =>
      System.exit(1)
  }

}

object SurfaceActor {

  object PrintSystemState

  object TerminateProcessing

  object BeginProcessing

  type SurfaceConfig = Map[Mower, List[Command]]

  def props(surface: Surface, commands: SurfaceConfig): Props =
    Props(classOf[SurfaceActor], surface, commands)
}
