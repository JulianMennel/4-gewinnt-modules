package de.htwg.se.io.fileIOComponent

import de.htwg.se.model.fieldComponent.FieldInterface

trait FileIOInterface {
  def load: FieldInterface
  def save(field: FieldInterface): Unit
}