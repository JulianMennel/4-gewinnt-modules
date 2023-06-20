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

class SlickIO(var field: FieldInterface) extends DAOInterface {

    private val databaseUrl: String =
        s"jdbc:mysql://" +
            s"db:" +
            s"3306/" +
            s"db?serverTimezone=CET?allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false"

    private val database = Database.forURL(
      url = databaseUrl,
      driver = "com.mysql.cj.jdbc.Driver",
      user = "user",
      password = "password"
    )

    private val maxSec = Duration(15, SECONDS)
    private val fieldTable = new TableQuery(new Field(_))
    private val cellTable = new TableQuery(new Cell(_, fieldTable))

    private val setup: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(fieldTable.schema.createIfNotExists, cellTable.schema.createIfNotExists)

    override def load: FieldInterface = {
        val loadField = fieldTable.filter(_.id === 0)
        val loadCells = cellTable.filter(_.fieldId === 0)

        val fieldResult = Await.result(database.run(loadField.result), maxSec)
        val rows = fieldResult.head._2
        val cols = fieldResult.head._3
        val mode = fieldResult.head._4
        val player = fieldResult.head._5

        field.changePlayer(player)
        field.changeMode(mode)

        val cellResults = Await.result(database.run(loadCells.result), maxSec)
        for (row <- 0 until rows) {
            for ( col <- 0 until cols) {
                val cell = cellResults.filter(_._2 == row).filter(_._3 == col).head._4
                field.set(row, col, cell)
            }
        }
        field
    }

    override def save(field: FieldInterface): Unit = {
        val insertField = (fieldTable returning fieldTable.map(_.id)) += (0, field.size, field.size2, field.mode.toString(), field.player.toString())
        val fieldId = Await.result(database.run(insertField), maxSec)
        for (row <- 0 until field.size) {
            for (col <- 0 until field.size2) {
                val insertCell = cellTable += (fieldId, row, col, field.stone(row,col).toString)
                Await.result(database.run(insertField), maxSec)
            }
        }
    }

    override def delete(fieldId: Option[Int]): Unit = {
        val fieldAction = fieldId.map(id => fieldTable.filter(_.id === id).delete).getOrElse(fieldTable.filter(_.id === fieldId).delete)
        val cellAction = fieldId.map(id => cellTable.filter(_.fieldId === id).delete).getOrElse(cellTable.filter(_.fieldId === fieldId).delete)
        Await.result(database.run(fieldAction), maxSec)
        Await.result(database.run(cellAction), maxSec)
    }

    override def update(fieldId: Option[Int], field: FieldInterface): Unit = {
        val fieldAction = fieldTable.insertOrUpdate((fieldId.productArity, field.size, field.size2, field.mode.toString(), field.player.toString()))
        val cellAction = cellTable.filter(_.fieldId === fieldId).delete
        Await.result(database.run(fieldAction), maxSec)
        Await.result(database.run(cellAction), maxSec)
    }

}