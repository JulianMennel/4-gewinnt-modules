package de.htwg.se.controller

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.model.fieldComponent._
import de.htwg.se.model.fieldComponent.fieldBaseImpl.{Field, Matrix, PlayerModeStrategy, Stone, TruePlayerState}
import de.htwg.se.io.fileIOComponent._
import de.htwg.se.io.fileIOComponent.fileIODAOImpl.SlickIO

class MainModule extends AbstractModule {
  override def configure() = {
    bind(classOf[ControllerInterface]).to(classOf[controllerBaseImpl.Controller])
    bind(classOf[FieldInterface]).annotatedWith(Names.named("DefField")).toInstance(new Field())
    bind(classOf[FieldInterface]).toInstance(new Field())
    bind(classOf[FileIOInterface]).toInstance(fileIODAOImpl.MongoIO(new Field()))
  }
}