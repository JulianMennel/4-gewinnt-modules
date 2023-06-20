package de.htwg.se.controller.controllerBaseImpl

import de.htwg.se.model.fieldComponent.fieldBaseImpl._
import de.htwg.se.util.Observer
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import de.htwg.se.model.moveComponent.Move

class ControllerSpec extends AnyWordSpec {
  "A controller" when {
    "observed by an Observer" should {
      val controller = new Controller(new Field())
      val observer = new Observer {
        var updated: Boolean = false
        var killed: Boolean = false

        override def update: Unit = updated = !updated
        override def kill: Unit = killed = !killed

        override def toString: String = updated.toString
      }

      "notify when insert a chip" in {
        controller.add(observer)
        controller.put(Some(Move('i', 1, 1)))
        observer.toString should be("true")
      }
      "notify when redo" in {
        controller.redo
        observer.toString should be("false")
      }
      "notify when undo" in {
        controller.undo
        observer.toString should be("true")
      }

      "after remove should not notify" in {
        controller.remove(observer)
        controller.put(Some(Move('i', 1, 1)))
        observer.toString should be("true")
      }
    } 
  }
}
