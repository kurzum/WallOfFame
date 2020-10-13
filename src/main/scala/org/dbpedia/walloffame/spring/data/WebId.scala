package org.dbpedia.walloffame.spring.data

import javax.persistence.Entity

import scala.beans.BeanProperty

@Entity
class WebId extends AbstractEntity {
  @BeanProperty
  var webid: String = null

  override def toString = "[WebId: id = " + id + ", webid = " + webid + "]"
}
