package de.htwg.se.model.fieldComponent.fieldBaseImpl

import de.htwg.se.util.PlayerState

case class TruePlayerState() extends PlayerState {
  override def put(x: Int, y: Int, field: Field): Field = if (field.stone(x, y) == Stone.Empty) field.copy(field.matrix.replaceCell(x, y, Stone.X), FalsePlayerState(), field.mode) else field
  override def toString(): String = "true"
}