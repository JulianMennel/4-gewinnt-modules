package de.htwg.se.io.fileIOComponent.fileIODAOImpl

import de.htwg.se.io.fileIOComponent.FileIOInterface
import de.htwg.se.io.fileIOComponent.fileIODAOImpl.DAOInterface
import slick.jdbc.PostgresProfile.api._
import java.sql.Connection
import scala.util.{Success, Failure}
import de.htwg.se.model.fieldComponent.FieldInterface
import de.htwg.se.io.fileIOComponent.fileIODAOImpl.tables.{Field, Cell}
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import org.mongodb.scala.*
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.result.{DeleteResult, InsertOneResult, UpdateResult}
import play.api.libs.json._
import com.mongodb.client.model.UpdateOptions
import scala.concurrent.ExecutionContext.Implicits.global

class MongoIO(var field: FieldInterface) extends DAOInterface {

    private val databaseUrl: String = "mongodb://localhost:27017/?authSource=admin"
    private val maxSec = Duration(15, SECONDS)
    private val client: MongoClient = MongoClient(databaseUrl)
    private val db: MongoDatabase = client.getDatabase("db2")
    private val gameCollection: MongoCollection[Document] = db.getCollection("game")

    override def load: FieldInterface = {
        val gameDocument: Document = Await.result(gameCollection.find(equal("_id", 1)).first().head(), Duration.Inf)
        fromJson(gameDocument("game").asString().getValue.toString)
    }

    override def save(field: FieldInterface): Unit = {
        val future1 = Future(delete(Some(1)))
        val future2 = Future(create)
        val future3 = Future(update(Some(1), field))
        future1.onComplete{
            case Success(value) => print("Deleted Game")
            case Failure(exception) => print("Error while deleting.")
        }
        future2.onComplete{
            case Success(value) => print("Created Game")
            case Failure(exception) => print("Error while creating.")
        }
        future3.onComplete{
            case Success(value) => print("Updated Game")
            case Failure(exception) => print("Error while updating.")
        }
    }

    override def delete(fieldId: Option[Int]): Unit = {
        Await.result(deleteFuture, Duration.Inf)
    }

    private def deleteFuture:Future[String] = {
        gameCollection.deleteMany(equal("_id", 1)).subscribe(
            (dr: DeleteResult) => println(s"Deleted gameDocument"),
            (e: Throwable) => println(s"Error while trying to delete gameDocument: $e")
        )
        Future {
            "Finished deleting!"
        }
    }

    override def update(fieldId: Option[Int], field: FieldInterface): Unit = {
        val json = toJson(field)
        observerUpdate(gameCollection.updateOne(equal("_id", 1), set("game", json)))
    }

    def create:Unit ={
        val gameDocument: Document = Document("_id" -> 1, "game" -> toJson(field))
        observerInsertion(gameCollection.insertOne(gameDocument))
    }

    def toJson(field: FieldInterface): String = {
        Json.obj(
        "field" -> Json.obj(
            "mode" -> JsString(field.mode.toString()),
            "player" -> JsString(field.player.toString()),
            "cells" -> Json.toJson(
            for {
                row <- 0 until field.size
                col <- 0 until field.size2
            } yield {
                Json.obj(
                "row" -> row,
                "col" -> col,
                "value" -> field.stone(row, col).toString,
                )
            }
            )
        )
        ).toString
    }

    def fromJson(string: String): FieldInterface = {
        val json: JsValue = Json.parse(string)
        for (index <- 0 until field.size * field.size2) {
            val row = (json \\ "row")(index).as[Int]
            val col = (json \\ "col")(index).as[Int]
            val value = (json \\ "value")(index).as[String]
            field = field.set(row, col, value)
        }
        field.changeMode((json \\ "mode")(0).as[String])
        field.changePlayer((json \\ "player")(0).as[String])
        field
    }

    private def observerInsertion(insertObservable: SingleObservable[InsertOneResult]): Unit = {
        insertObservable.subscribe(new Observer[InsertOneResult] {
        override def onNext(result: InsertOneResult): Unit = println(s"inserted: $result")

        override def onError(e: Throwable): Unit = println(s"insert onError: $e")

        override def onComplete(): Unit = println("completed insert")
        })
    }

    private def observerUpdate(insertObservable: SingleObservable[UpdateResult]): Unit = {
        insertObservable.subscribe(new Observer[UpdateResult] {
        override def onNext(result: UpdateResult): Unit = println(s"updated: $result")

        override def onError(e: Throwable): Unit = println(s"update onError: $e")

        override def onComplete(): Unit = println("completed update")
    })
  }
}