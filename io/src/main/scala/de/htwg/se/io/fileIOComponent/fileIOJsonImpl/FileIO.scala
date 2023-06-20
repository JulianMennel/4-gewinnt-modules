package de.htwg.se.io.fileIOComponent.fileIOJsonImpl

import de.htwg.se.io.fileIOComponent.FileIOInterface
import scala.io.Source
import de.htwg.se.model.fieldComponent.FieldInterface
import play.api.libs.json._
import com.google.inject.Guice
import java.io._
import de.htwg.se.model.fieldComponent.fieldBaseImpl.TruePlayerState
class FileIO(var field: FieldInterface) extends FileIOInterface {
  override def load: FieldInterface = {
    val source: String = Source.fromFile("field.json").getLines.mkString
    val json: JsValue = Json.parse(source)
    for (index <- 0 until field.size * field.size2)
      val row = (json \\ "row")(index).as[Int]
      val col = (json \\ "col")(index).as[Int]
      val value = (json \\ "value")(index).as[String]
      field = field.set(row, col, value)
    field.changeMode((json \\ "mode")(0).as[String])
    field.changePlayer((json \\ "player")(0).as[String])
    field
  }

  override def save(field: FieldInterface): Unit = {
    val pw = new PrintWriter(new File("field.json"))
    pw.write(Json.prettyPrint(fieldToJson(field)))
    pw.close
  }

  def fieldToJson(field: FieldInterface) = {
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
    )
  }
}