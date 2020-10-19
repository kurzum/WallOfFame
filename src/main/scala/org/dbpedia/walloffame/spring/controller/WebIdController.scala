package org.dbpedia.walloffame.spring.controller


import java.io.ByteArrayOutputStream

import better.files.File
import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.{Lang, RDFDataMgr, RDFFormat}
import org.apache.log4j.TTCCLayout
import org.dbpedia.walloffame.crawling.WebIdCrawler
import org.dbpedia.walloffame.uniform.WebIdUniformer
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.{RequestMapping, RequestParam, ResponseBody, RestController}
import org.springframework.web.servlet.ModelAndView

@Controller
class WebIdController {

  @RequestMapping(value = Array("/webids"), method = Array(GET))
  //viewname is the path to the related jsp file
  @ResponseBody
  def crawlAndUniformWebIds():String = {
    val uniformedModel = WebIdUniformer.uniformWebIds(WebIdCrawler.crawl())

    val out = new ByteArrayOutputStream()
    RDFDataMgr.write(out, uniformedModel, Lang.TTL)
    out.toString
  }

}
