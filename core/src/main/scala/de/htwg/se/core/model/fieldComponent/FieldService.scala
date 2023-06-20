package de.htwg.se.core.model.fieldComponent

import de.htwg.se.model.fieldComponent.FieldInterface
import de.htwg.se.model.moveComponent.Move
import de.htwg.se.model.fieldComponent.fieldBaseImpl.Field
import de.htwg.se.model.fieldComponent.fieldBaseImpl.Stone
import scala.concurrent.ExecutionContext
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import de.htwg.se.util.ModeStrategy
import de.htwg.se.util.PlayerState

object FieldService extends FieldInterface {
    implicit val system: ActorSystem = ActorSystem("FieldService")
    var field = new Field()
    var counter = 0
    val route = 
            get {
                concat(
                    path("put" / IntNumber / IntNumber) { (row: Int, col: Int) =>
                        complete(put(Some(Move('i', row, col))).toString)
                    },
                    path("stone" / IntNumber / IntNumber) { (row: Int, col: Int) =>
                        complete(stone(row, col).toString)
                    },
                    path("set" / IntNumber / IntNumber / Segment) { (row: Int, col: Int, filling: String) =>
                        complete(set(row, col, filling).toString)
                    },
                    path("changeMode" / Segment) { (str: String) =>
                        complete(changeMode(str).toString)
                    },
                    path("changePlayer" / Segment) { (str: String) =>
                        complete(changePlayer(str).toString)
                    },
                    path("new") {
                        complete(newField().toString)
                    },
                    path("undo" / IntNumber / IntNumber) { (row: Int, col: Int) =>
                        complete(undo(Some(Move('i', row, col))).toString)
                    }
                )
            }

    override def size: Int = field.size
    override def size2: Int = field.size2
    override def mode: ModeStrategy = field.mode
    override def player: PlayerState = field.player
    override def put(m: Option[Move]): Field = {
        field.put(m)
    }
    override def stone(x: Int, y: Int): Stone = field.stone(x, y)
    override def set(x: Int, y: Int, filling: String): FieldInterface = field.set(x, y, filling)
    override def changeMode(str: String): FieldInterface = field.changeMode(str)
    override def changePlayer(player: String): FieldInterface = field.changePlayer(player)
    override def undo(m: Option[Move]): FieldInterface = field.undo(m)
    override def newField(): FieldInterface = 
        field = new Field()
        field

    @main
    def main: Unit = {
        val bindingFuture = Http().newServerAt("localhost", 8082).bind(route)
        println(s"Server online at http://localhost:8082/")
    }
}