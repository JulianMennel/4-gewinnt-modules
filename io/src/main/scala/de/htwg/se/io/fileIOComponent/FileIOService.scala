package de.htwg.se.io.fileIOComponent

import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.ConfigFactory
import de.htwg.se.model.fieldComponent.fieldBaseImpl.Field
import de.htwg.se.model.fieldComponent.FieldInterface
import de.htwg.se.io.fileIOComponent.fileIOJsonImpl.FileIO

object FileIOService extends FileIOInterface {
    implicit val system: ActorSystem = ActorSystem("FileIOService")
    val io = FileIO(new Field())
    val route = 
            get {
                concat(
                    path("save") {
                        complete(save.toString)
                    },
                    path("load") {
                        complete(load.toString)
                    }
                )
            }
    
    override def load: FieldInterface = io.load
    override def save(field: FieldInterface): Unit = io.save(io.field)

    @main
    def main: Unit = {
        val bindingFuture = Http().newServerAt("localhost", 8081).bind(route)
        println(s"Server online at http://localhost:8081/")
    }
}