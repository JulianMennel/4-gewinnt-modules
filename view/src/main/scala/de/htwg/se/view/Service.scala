package de.htwg.se.view

import com.google.inject.Guice
import de.htwg.se.controller._
object VierGewinnt {
    def main(args: Array[String]) = {
        val injector = Guice.createInjector(new MainModule)
        val controller = injector.getInstance(classOf[ControllerInterface])
        var rest = Rest(controller)
        //val gui = GUI(controller)
        val tui = TUI(controller)
    }
}