package org.dbpedia.walloffame.spring.controller

import org.dbpedia.walloffame.Config
import org.dbpedia.walloffame.convert.ModelToJSONConverter
import org.dbpedia.walloffame.virtuoso.VirtuosoHandler
import virtuoso.jdbc4.VirtuosoException


@Controller
class WoFController {

  @Autowired
  private var config: Config = _

  @RequestMapping(value = Array("/walloffame"), method = Array(GET))
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

  @RequestMapping(value = Array("/result"), method = Array(GET))
  def getResultPage(): String = {
    "result"
  }

}
