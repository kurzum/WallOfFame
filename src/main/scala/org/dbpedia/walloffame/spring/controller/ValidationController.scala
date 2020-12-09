package org.dbpedia.walloffame.spring.controller

import java.io
import java.io.FileInputStream

import better.files.File
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils
import org.apache.jena.riot.RiotException
import org.dbpedia.walloffame.Config
import org.dbpedia.walloffame.convert.ModelToJSONConverter
import org.dbpedia.walloffame.uniform.WebIdUniformer
import org.dbpedia.walloffame.validation.WebIdValidator
import org.dbpedia.walloffame.virtuoso.VirtuosoHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestParam}
import org.springframework.web.servlet.ModelAndView

@Controller
class ValidationController(config: Config) {

  //value = Array("url") is the url the resulting site will be located at
  //viewname is the path to the related jsp file
  @GetMapping(value = Array("/", "/validate"))
  def getValidate(): String = "validate"

  @PostMapping(value = Array("/", "validate"))
  def sendWebIdToValidate(@RequestParam("webid") webid: String) = {

    println(webid)

    import java.io.PrintWriter
    val fileToValidate = File("./tmp/webIdToValidate.ttl")
    new PrintWriter(fileToValidate.toJava) {
      write(webid)
      close
    }

    var result = ""
    try {

      result = WebIdValidator.validateWithShacl(fileToValidate)

      if (result == "") {
        //valid webid

//        val model = WebIdUniformer.uniform(fileToValidate)
//
//        var wait = true
//        while (wait) {
//          try {
//            VirtuosoHandler.insertModel(model,config.virtuoso)
//            wait = false
//          } catch {
//            case e: Exception =>
//              println("waiting for vos to start up")
//              Thread.sleep(1000)
//          }
//        }
//
//        fileToValidate.delete()
//        val webids = ModelToJSONConverter.appendToJSONFile(VirtuosoHandler.getModel(config.virtuoso), File(config.exhibit.file))
////        val webids = ModelToJSONConverter.toJSON(model)
        val webids =""
        new ModelAndView("redirect:static/exhibit/walloffame.html", "webids" , webids)

      }
      else {

        fileToValidate.delete()
        new ModelAndView("result", "result", result)

      }
    } catch {
      case riot: RiotException => {
        result = riot.toString
        fileToValidate.delete()
        new ModelAndView("result", "result", result)
      }
    }

  }

  @GetMapping(value = Array("/webids.js"),produces = Array("application/json"))
  def getJson(response: HttpServletResponse): Unit = {
    try {

      IOUtils.copy(new FileInputStream(new io.File(config.exhibit.file)),response.getOutputStream)
      response.setStatus(200)
    } catch {
      case e: Exception => response.setStatus(500)
    }
  }

}
