package de.htwg.se

import de.htwg.se.view._
import de.htwg.se.controller.ControllerInterface
import de.htwg.se.controller.MainModule
import com.google.inject.Guice

@main
def main: Unit = {
  val injector = Guice.createInjector(new MainModule)
  val controller = injector.getInstance(classOf[ControllerInterface])
  val rest = Rest(controller)
  //val gui = GUI(controller)
  val tui = TUI(controller)
}