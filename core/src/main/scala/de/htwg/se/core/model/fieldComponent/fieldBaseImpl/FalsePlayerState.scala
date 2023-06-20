package de.htwg.se.model.fieldComponent.fieldBaseImpl

import de.htwg.se.util.PlayerState

case class FalsePlayerState() extends PlayerState {
  override def put(x: Int, y: Int, field: Field): Field = if (field.stone(x, y) == Stone.Empty) field.copy(field.matrix.replaceCell(x, y, Stone.O), TruePlayerState(), field.mode) else field
  override def toString(): String = "false"
}