package org.dbpedia.walloffame.spring.model

class WebId {

  var value: String = _

  def setValue(value:String) ={
    this.value=value
  }

  def getValue():String={
    value
  }
}
