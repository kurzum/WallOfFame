package org.dbpedia.walloffame.spring.controller

import better.files.File
import org.dbpedia.walloffame.Config
import org.dbpedia.walloffame.convert.ModelToJSONConverter
import org.dbpedia.walloffame.virtuoso.VirtuosoHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import virtuoso.jdbc4.VirtuosoException


@Controller
class WoFController {

  @Autowired
  private var config: Config = _

  @RequestMapping(value = Array("/walloffame"), method = Array(GET))
  def getIndexPage(): String = {
    val optModel =
    try{
      VirtuosoHandler.getModelOfAllWebids(config.virtuoso)
    } catch {
      case virtuosoException: VirtuosoException => None
    }

    if (!(optModel ==None)) ModelToJSONConverter.createJSONFile(optModel.get, File(config.exhibit.file))
    "walloffame"
  }

}
