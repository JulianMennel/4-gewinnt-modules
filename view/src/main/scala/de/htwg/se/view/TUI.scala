package de.htwg.se.view

import de.htwg.se.controller.ControllerInterface
import de.htwg.se.model.moveComponent.Move
import scala.io.StdIn.readLine
import de.htwg.se.util.Observer
import scala.util.{Try,Success,Failure}

class TUI(controller: ControllerInterface) extends Observer:
  controller.add(this)
  val eol = sys.props("line.separator")
  val size1 = controller.field.size
  val size2 = controller.field.size2
  val p = "(i [0-5] [0-6])".r
  override def update: Unit = println(controller.toString)
  override def kill: Unit = System.exit(0)
  def inputLoop(): Unit =
    analyseInput(readLine) match
      case None => println("Bitte Eingabe ueberpruefen.")
      case Some(move) => 
        move.m match
          case 'i' => controller.put(Some(move))
          case 'c' => controller.changeMode("computer"); println("Modus wurde gewechselt.")
          case 'p' => controller.changeMode("player"); println("Modus wurde gewechselt.")
          case 'r' => controller.redo; println("Redo erfolgreich.")
          case 'u' => controller.undo; println("Undo erfolgreich.")
          case 'e' => println("Programm wird geschlossen"); controller.quit
          case 's' => controller.save; println("Spiel gespeichert.")
          case 'l' => controller.load; println("Spiel geladen.")
    inputLoop()

  def analyseInput(input: String): Option[Move] = {
    input match
      case "q" => Some(Move('e', 0, 0))
      case "r" => Some(Move('r', 0, 0))
      case "u" => Some(Move('u', 0, 0))
      case "l" => Some(Move('l', 0, 0))
      case "s" => Some(Move('s', 0, 0))
      case "singleplayer" | "Singleplayer" => Some(Move('c', 0, 0))
      case "multiplayer" | "Multiplayer" => Some(Move('p', 0, 0))
      case p(c) => Some(Move('i', input.toCharArray()(2).toInt-'0', input.toCharArray()(4).toInt-'0'))
      case _ => None
  }

  print(eol + "Hochschule fuer Technik, Wirtschaft & Gestaltung" + eol + "AIN SOFTWARE-ENGINEERING WiSe 21/22" + eol + "        ### GRUPPE 15 ###" + eol + eol + ">  Willkommen zu 4-Gewinnt  <" + eol + controller.field.toString)
  inputLoop()