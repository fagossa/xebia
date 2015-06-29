package actors

import actors.MowerActor._
import actors.SurfaceActor.{PrintSystemState, BeginProcessing, SurfaceConfig, TerminateProcessing}
import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import model.{Command, Mower, Position, Surface}

class SurfaceActor(val sur: Surface, initialState: SurfaceConfig) extends Actor {

  val config = ConfigFactory.load()
  val system = ActorSystem("system", config)

  var usedPositions = scala.collection.mutable.TreeSet[Position]()

  def receive = {
    case BeginProcessing =>
      initialState.foreach(key => {
        val mowerRef = system.actorOf(MowerActor.props())
        mowerRef ! ExecuteCommands(mower = key._1, commands = key._2)
      })

    case RequestPosition(current: Position, previous: Position) =>
      if (usedPositions.contains(current)) {
        sender() ! PositionRejected
      } else {
        sender() ! PositionAutorised
        usedPositions -= previous
        usedPositions += current
      }

    case AllCommandsExecuted(mower: MowerActor) =>
      println(s"All commands executed for <$mower> ...")

    case PrintSystemState =>
      // TODO: implement

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
