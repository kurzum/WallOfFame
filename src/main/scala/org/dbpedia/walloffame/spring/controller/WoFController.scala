package org.dbpedia.walloffame.spring.controller

import better.files.File
import org.dbpedia.walloffame.Config
import org.dbpedia.walloffame.convert.ModelToJSONConverter
import org.dbpedia.walloffame.virtuoso.VirtuosoHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod}
import virtuoso.jdbc4.VirtuosoException


@Controller
class WoFController {

  @Autowired
  private var config: Config = _

  @RequestMapping(value = Array("/walloffame"), method = Array(RequestMethod.GET))
  def getIndexPage(): String = {
    val optModel =
      try {
        VirtuosoHandler.getModelOfAllWebids(config.virtuoso)
      } catch {
        case virtuosoException: VirtuosoException => None
      }

    if (!(optModel == None)) ModelToJSONConverter.createJSONFile(optModel.get, File(config.exhibit.file))
    "walloffame"
  }

  @RequestMapping(value = Array("/result"), method = Array(RequestMethod.GET))
  def getResultPage(): String = {
    "result"
  }

}
