package de.htwg.se.model.fieldComponent

import de.htwg.se.model.fieldComponent.fieldBaseImpl.Stone
import de.htwg.se.model.fieldComponent.fieldBaseImpl.Field
import de.htwg.se.util.ModeStrategy
import de.htwg.se.util.PlayerState
import de.htwg.se.model.fieldComponent.fieldBaseImpl.PlayerModeStrategy
import de.htwg.se.model.moveComponent.Move
import scala.util.Try

trait FieldInterface {
  def size: Int
  def size2: Int
  def mode: ModeStrategy
  def player: PlayerState
  def put(m: Option[Move]): Field
  def stone(x: Int, y: Int): Stone
  def set(x: Int, y: Int, filling: String): FieldInterface
  def changeMode(str: String): FieldInterface
  def changePlayer(player: String): FieldInterface
  def undo(m: Option[Move]): FieldInterface
  def newField(): FieldInterface
}