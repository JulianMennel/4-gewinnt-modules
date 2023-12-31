package de.htwg.se.view

import de.htwg.se.controller.ControllerInterface
import scala.swing._
import de.htwg.se.util.Observer
import scala.swing.event._
import scala.swing.Swing.LineBorder
import de.htwg.se.model.moveComponent.Move

class GUI(controller: ControllerInterface) extends Observer:
  controller.add(this)

  var button = Array.ofDim[Button](controller.field.size, controller.field.size2)
  override def update: Unit = redraw()
  override def kill: Unit = System.exit(0)

  //val icon = new java.awt.Image() {
    // OUR GAME ICON IS STILL UNDER CONSTRUCTION
  //}
  val frame = new Frame {
    title = "4-Gewinnt - Gruppe 15"
    override def closeOperation(): Unit = controller.quit
    //iconImage = icon

    val spielfeld = new GridPanel(controller.field.size, controller.field.size2) {
      border = LineBorder(java.awt.Color.GRAY, 2)
      background = java.awt.Color.BLACK
      for (index <- 0 until controller.field.size;
           index2 <- 0 until controller.field.size2)
          button(index)(index2) = new Button(controller.field.stone(index, index2).toString) {
            reactions += { case event.ButtonClicked(_) =>
              controller.put(Some(new Move('i', index, index2)))
              button(index)(index2).text = controller.field.stone(index, index2).toString
            }
          }
          contents += button(index)(index2)
    }

    val optionen = new GridPanel(2, 2) {
      border = LineBorder(java.awt.Color.WHITE, 20)
      background = java.awt.Color.WHITE
      val undoButton = new Button("Undo") {
        reactions += { case event.ButtonClicked(_) =>
          controller.undo
        }
      }
      undoButton.preferredSize_=(new Dimension(30,50))
      contents += undoButton
      val redoButton = new Button("Redo") {        
        reactions += { case event.ButtonClicked(_) =>
          controller.redo
        }
      }
      redoButton.preferredSize_=(new Dimension(30,50))
      contents += redoButton
      val ModeSelect = new GridPanel(2,1) {
        val playerRadioButton = new RadioButton("Player") {
          reactions += { case event.ButtonClicked(_) =>
            controller.changeMode("player")
          }
        }
        playerRadioButton.preferredSize_=(new Dimension(30, 50))
        contents += playerRadioButton
        val computerRadioButton = new RadioButton("Computer") {          
          reactions += { case event.ButtonClicked(_) =>
            controller.changeMode("computer")
          }
        }
        computerRadioButton.preferredSize_=(new Dimension(30, 50))
        val gruppe1 = new ButtonGroup(playerRadioButton, computerRadioButton)
        gruppe1.select(playerRadioButton)
        contents += computerRadioButton
      }
      contents += ModeSelect
      
      val saveLoad = new GridPanel(2,1) {
        val save = new Button("Save") {
          reactions += { case event.ButtonClicked(_) =>
            controller.save
          }
        }
        val load = new Button("Load") {
          reactions += { case event.ButtonClicked(_) =>
            controller.load
          }
        }
        contents += save
        contents += load
      }
      contents += saveLoad
    }

    val myMenu = new MenuBar {
      contents += new Menu("Options") {
        mnemonic = Key.O
        contents += new MenuItem(Action("New Game") { controller.newField })
        contents += new MenuItem(Action("Quit") {
        controller.quit
        close})
      }
    }
    


    val frameContents = new BoxPanel(Orientation.Vertical) {
      contents += myMenu
      contents += spielfeld
      contents += optionen
    }

    contents = frameContents
    

    size = new Dimension(500, 500)
    centerOnScreen()
    open()
  }

  def redraw(): Unit =
    for (index <- 0 to controller.field.size - 1)
      for (index2 <- 0 to controller.field.size2 - 1)
        button(index)(index2).text = controller.field.stone(index, index2).toString

  //def closeOperation(): Unit =
    //controller.quit