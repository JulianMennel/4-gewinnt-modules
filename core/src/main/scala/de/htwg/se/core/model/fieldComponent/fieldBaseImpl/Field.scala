package de.htwg.se.model.fieldComponent.fieldBaseImpl

import de.htwg.se.model.fieldComponent.FieldInterface
import de.htwg.se.util.PlayerState
import de.htwg.se.util.ModeStrategy
import de.htwg.se.model.moveComponent.Move
import scala.util.Try

case class Field(matrix: Matrix[Stone], player: PlayerState, mode: ModeStrategy) extends FieldInterface {
  def this(row: Int = 6, column: Int = 7, filling: Stone = Stone.Empty) = this(new Matrix(row, column, filling), TruePlayerState(), PlayerModeStrategy())
  val mode2 = if (mode == PlayerModeStrategy()) ComputerModeStrategy() else PlayerModeStrategy();
  val size = matrix.size
  val size2 = matrix.size2
  val eol = sys.props("line.separator")
  def put(m: Option[Move]): Field = m match { case Some(move: Move) => mode.put(move.x, move.y, this) case None => this }
  def bar(cellWidth: Int = 3, cellNum: Int = 3) : String = (("+" + "-" * cellWidth) * cellNum) + "+" + eol
  def cells(row: Int, cellWidth: Int = 3) : String = matrix.row(row).map(_.toString).map(" " * ((cellWidth - 1) / 2) + _ + " " * ((cellWidth - 1) / 2)).mkString("|", "|", "|") + eol
  def mesh(cellWidth: Int = 3) : String = (0 until size).map(cells(_, cellWidth)).mkString(bar(cellWidth, size2), bar(cellWidth, size2), bar(cellWidth, size2))
  override def toString = mesh()
  def stone(x: Int, y: Int): Stone = matrix.cell(x, y)
  def set(x: Int, y: Int, filling: String): FieldInterface = filling match { case " " => copy(matrix.replaceCell(x, y, Stone.Empty)) case "X" => copy(matrix.replaceCell(x, y, Stone.X)) case "O" => copy(matrix.replaceCell(x, y, Stone.O)) }
  def changeMode(str: String): FieldInterface = str match { case "player" => new Field(matrix, player, PlayerModeStrategy()) case "computer" => new Field(matrix, player, ComputerModeStrategy()) }
  def changePlayer(str: String): FieldInterface = str match { case "false" => new Field(matrix, FalsePlayerState(), mode) case "true" => new Field(matrix, TruePlayerState(), mode) }
  def checkStone(x: Int, y: Int, stone: Stone): Boolean = if (matrix.cell(x,y) == stone) true else false
  def checkStoneX(x: Int, y: Int): Boolean = checkStone(x, y, Stone.X)
  def checkStoneO(x: Int, y: Int): Boolean = checkStone(x, y, Stone.O)
  def checkStoneEmpty(x: Int, y: Int): Boolean = checkStone(x, y, Stone.Empty)
  def newField(): FieldInterface = new Field()


  //--Field related:
  def undo(move: Option[Move]): Field = 
    move match {
      case Some(m: Move) => undoSt(m)
      case None => this
  }


  def undoSt(m: Move): Field = {
    if (mode == PlayerModeStrategy()) {
      if (player == TruePlayerState())
        new Field(matrix, FalsePlayerState(), mode)
      else
        new Field(matrix, TruePlayerState(), mode)
      copy(matrix.replaceCell(m.x, m.y, Stone.Empty)) //write empty Stone
    } else {
      val field2 = copy(matrix.replaceCell(m.x, m.y, Stone.Empty))
      if (m.y + 1 <= size2 - 1)
        copy(field2.matrix.replaceCell(m.x, m.y + 1, Stone.Empty))
      else
        field2
    }
  }
}