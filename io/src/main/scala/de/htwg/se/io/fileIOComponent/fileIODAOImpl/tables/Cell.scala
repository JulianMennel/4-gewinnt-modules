package de.htwg.se.io.fileIOComponent.fileIODAOImpl.tables

import slick.jdbc.PostgresProfile.api.*
import slick.lifted.TableQuery
import scala.annotation.targetName

class Cell(tag: Tag, field: TableQuery[Field]) extends Table[(Int, Int, Int, String)](tag, "Cell") {
    def fieldId = column[Int]("FIELD_ID")
    def row = column[Int]("ROW")
    def col = column[Int]("COL")
    def filling = column[String]("FILLING")
    override def * = (fieldId, row, col, filling)
    def pk = primaryKey("CELL_PK", (fieldId, row, col))
    def fieldFk = foreignKey("FIELD_FK", fieldId, field)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
}