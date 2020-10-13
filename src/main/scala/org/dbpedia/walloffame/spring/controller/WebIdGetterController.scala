package org.dbpedia.walloffame.spring.controller

import better.files.File
import org.dbpedia.walloffame.uniform.WebIdUniformer
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.{RequestMapping, RequestParam, RestController}
import org.springframework.web.servlet.ModelAndView

@Controller
class WebIdGetterController {

  @RequestMapping(value = Array("/webid/webids"), method = Array(GET))
  //viewname is the path to the related jsp file
  def showNewCustomerForm() = new ModelAndView("webid/webids", "webids", WebIdUniformer.check(File("./crawl/tmp/webids/webid1.ttl")))

}
