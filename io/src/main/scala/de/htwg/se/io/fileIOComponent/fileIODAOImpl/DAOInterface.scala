package de.htwg.se.io.fileIOComponent.fileIODAOImpl

import de.htwg.se.model.fieldComponent.FieldInterface
import de.htwg.se.io.fileIOComponent.FileIOInterface

trait DAOInterface extends FileIOInterface {
  def load: FieldInterface
  def save(field: FieldInterface): Unit
  def delete(fieldId: Option[Int]): Unit
  def update(fieldId: Option[Int], field: FieldInterface): Unit
}