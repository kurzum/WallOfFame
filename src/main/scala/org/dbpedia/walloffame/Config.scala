package org.dbpedia.walloffame

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

import scala.beans.BeanProperty

@Configuration
@ConfigurationProperties
case class Config() {
  @BeanProperty
  var virtuoso: VosConfig = new VosConfig
  @BeanProperty
  var exhibit: ExhibitConfig = new ExhibitConfig
}

case class VosConfig() {
  @BeanProperty
  var url: String = _
  @BeanProperty
  var usr: String = _
  @BeanProperty
  var psw: String = _
  @BeanProperty
  var graph: String = _
}

case class ExhibitConfig(){
  @BeanProperty
  var file:String =_
}
