package de.htwg.se.view

import de.htwg.se.controller.ControllerInterface
import de.htwg.se.util.Observer
import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import de.htwg.se.model.moveComponent.Move


class Rest(controller: ControllerInterface) extends Observer {
    controller.add(this)
    override def update: Unit = {}
    override def kill: Unit = system.terminate()
    implicit val system: ActorSystem = ActorSystem()
    implicit val executionContext: ExecutionContext = system.dispatcher

    val route =
        get {
            concat(
                path("put" / IntNumber / IntNumber) { (row: Int, col: Int) =>
                    complete(controller.put(Some(Move('i', row, col))).toString)
                },
                path("undo") {
                    complete(controller.undo.toString)
                },
                path("redo") {
                    complete(controller.redo.toString)
                },
                path("save") {
                    complete(controller.save.toString)
                },
                path("load") {
                    complete(controller.load.toString)
                },
                path("new") {
                    complete(controller.newField.toString)
                },
                path("quit") {
                    complete(controller.quit.toString)
                }
            )
        }

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    println(s"Server online at http://localhost:8080/")
}
