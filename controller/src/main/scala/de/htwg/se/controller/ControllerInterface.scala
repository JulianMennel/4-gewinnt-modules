package de.htwg.se.controller

import de.htwg.se.util.ModeStrategy
import de.htwg.se.model.fieldComponent.FieldInterface
import de.htwg.se.util.Observable
import de.htwg.se.model.moveComponent.Move

trait ControllerInterface extends Observable {
  def field: FieldInterface
  def newField: Unit
  def put(m: Option[Move]): Unit
  def undo: Unit
  def redo: Unit
  def changeMode(str: String): FieldInterface
  def quit: Unit
  def save: Unit
  def load: Unit
  def toString: String
}