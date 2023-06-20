package de.htwg.se.controller.controllerBaseImpl

import de.htwg.se.model.fieldComponent.FieldInterface
import de.htwg.se.util.Command
import de.htwg.se.util.UndoManager
import de.htwg.se.model.fieldComponent.fieldBaseImpl.TruePlayerState
import de.htwg.se.model.fieldComponent.fieldBaseImpl.Stone
import de.htwg.se.model.moveComponent.Move
import scala.util.Try

class PutCommand(m: Option[Move], controller: Controller) extends Command[FieldInterface]:
  override def noStep(field: FieldInterface): FieldInterface = controller.field
  override def doStep(field: FieldInterface): FieldInterface = controller.field.put(m)
  override def undoStep(field: FieldInterface): FieldInterface = controller.field.undo(m)
  override def redoStep(field: FieldInterface): FieldInterface = controller.field.put(m)