package de.htwg.se.io.fileIOComponent.fileIODAOImpl.tables

import slick.jdbc.PostgresProfile.api.*
import slick.lifted.TableQuery
import scala.annotation.targetName

class Field(tag: Tag) extends Table[(Int, Int, Int, String, String)](tag, "Field") {
    def id = column[Int]("FIELD_ID", O.PrimaryKey, O.AutoInc)
    def height = column[Int]("FIELD_HEIGHT")
    def width = column[Int]("FIELD_WIDTH")
    def mode = column[String]("FIELD_MODE")
    def player = column[String]("FIELD_PLAYER")
    override def * = (id, height, width, mode, player)
}