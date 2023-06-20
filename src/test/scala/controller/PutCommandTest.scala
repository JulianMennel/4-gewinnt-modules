package de.htwg.se.controller.controllerBaseImpl

import de.htwg.se.model.fieldComponent.fieldBaseImpl.Field
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers.*
import de.htwg.se.model.moveComponent.Move

class PutCommandSpec extends AnyWordSpec {
  "PutCommand" should {

    val field = new Field()
    val field2 = new Field()
    field2.put(Some(Move('i', 0, 0)))
    val putCommand = new PutCommand(Some(Move('i', 0, 0)), new Controller(new Field))

    "have a noStep function" in {
      putCommand.noStep(field) should be(field)
    }
  }
}