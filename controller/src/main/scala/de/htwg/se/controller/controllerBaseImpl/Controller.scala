package de.htwg.se.controller.controllerBaseImpl

import de.htwg.se.model.fieldComponent.fieldBaseImpl.Field
import de.htwg.se.model.fieldComponent.FieldInterface
import de.htwg.se.controller.ControllerInterface
import de.htwg.se.util.Observable
import de.htwg.se.util.Command
import de.htwg.se.util.UndoManager
import de.htwg.se.util.ModeStrategy
import com.google.inject.Guice
import de.htwg.se.controller.MainModule
import de.htwg.se.io.fileIOComponent.FileIOInterface
import com.google.inject.Inject
import com.google.inject.name.Named
import de.htwg.se.model.fieldComponent.fieldBaseImpl.TruePlayerState
import de.htwg.se.model.fieldComponent.fieldBaseImpl.FalsePlayerState
import de.htwg.se.model.fieldComponent.fieldBaseImpl.Stone
import de.htwg.se.model.moveComponent.Move
import scala.util.Try
import scala.util.Failure
import scala.util.Success

case class Controller @Inject()(@Named("DefField") var field: FieldInterface) extends ControllerInterface with Observable:

  val undoManager = new UndoManager[FieldInterface]
  val injector = Guice.createInjector(new MainModule)
  val fileIo = injector.getInstance(classOf[FileIOInterface])

  def newField =
    field = new Field()
    notifyObservers
  
  def put(m: Option[Move]) =
    val put = Try {
      m match {
        case Some(move: Move) => field = undoManager.doStep(field, new PutCommand(m, this))
        case None =>
      }
    }
    put match {
      case Failure(e) => redo
      case Success(f) => 
    }
    notifyObservers
  
  def undo = 
    field = undoManager.undoStep(field)
    notifyObservers
  
  def redo = 
    field = undoManager.redoStep(field)
    notifyObservers
  
  def changeMode(str: String): FieldInterface = 
    field = field.changeMode(str)
    field
 
  def quit = 
    killObservers

  def save: Unit =
    fileIo.save(field)
    notifyObservers

  def load: Unit =
    field = fileIo.load
    notifyObservers

  override def toString = field.toString