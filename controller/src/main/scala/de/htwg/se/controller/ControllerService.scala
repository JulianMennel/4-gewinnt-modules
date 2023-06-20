package de.htwg.se.controller

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import com.google.inject.Guice
import de.htwg.se.model.fieldComponent.FieldInterface
import de.htwg.se.model.moveComponent.Move

object ControllerService extends ControllerInterface {
    implicit val system: ActorSystem = ActorSystem("ControllerService")
    val injector = Guice.createInjector(new MainModule)
    val controller = injector.getInstance(classOf[ControllerInterface])
    val route = 
            get {
                concat(
                    path("newField") {
                        complete(save.toString)
                    },
                    path("put" / IntNumber / IntNumber) { (row: Int, col: Int) =>
                        complete(put(Some(Move('i', row, col))).toString)
                    },
                    path("undo") {
                        complete(undo.toString)
                    },
                    path("redo") {
                        complete(redo.toString)
                    },
                    path("changeMode" / Segment) {(str: String) =>
                        complete(changeMode(str).toString)
                    },
                    path("quit") {
                        complete(quit.toString)
                    },
                    path("save") {
                        complete(save.toString)
                    },
                    path("load") {
                        complete(load.toString)
                    },
                    path("toString") {
                        complete(toString.toString)
                    }
                )
            }
    
    override def field: FieldInterface = controller.field
    override def newField: Unit = controller.newField
    override def put(m: Option[Move]): Unit = controller.put(m)
    override def undo: Unit = controller.undo
    override def redo: Unit = controller.redo
    override def changeMode(str: String): FieldInterface = controller.changeMode(str)
    override def quit: Unit = controller.quit
    override def save: Unit = controller.save
    override def load: Unit = controller.load
    override def toString: String = controller.toString

    @main
    def main: Unit = {
        val bindingFuture = Http().newServerAt("localhost", 8083).bind(route)
        println(s"Server online at http://localhost:8083/")
    }
    
}