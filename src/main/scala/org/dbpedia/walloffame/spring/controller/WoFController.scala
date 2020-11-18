package org.dbpedia.walloffame.spring.controller

import org.dbpedia.walloffame.convert.ModelToJSONConverter
import org.dbpedia.walloffame.virtuoso.VirtuosoHandler
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET


@Controller
class WoFController {

  @RequestMapping(value = Array("/walloffame"), method = Array(GET))
  def getIndexPage(): String = {
    ModelToJSONConverter.toJSON(VirtuosoHandler.getModel())
    "redirect:static/exhibit/walloffame.html"
  }

}
