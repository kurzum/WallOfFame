package org.dbpedia.walloffame.spring.controller

import better.files.File
import org.apache.jena.riot.RiotException
import org.dbpedia.walloffame.convert.ModelToJSONConverter
import org.dbpedia.walloffame.uniform.WebIdUniformer
import org.dbpedia.walloffame.validation.WebIdValidator
import org.dbpedia.walloffame.virtuoso.VirtuosoHandler
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestParam}
import org.springframework.web.servlet.ModelAndView

@Controller
class ValidationController {


  //value = Array("url") is the url the resulting site will be located at
  @GetMapping(value = Array("/", "/validate"))
  //viewname is the path to the related jsp file
  def getValidate(): String = "webid/validate"

  @PostMapping(value = Array("/", "validate"))
  def sendWebIdToValidate(@RequestParam("webid") webid: String) = {

    println(webid)

    import java.io.PrintWriter
    val fileToValidate = File("webIdToValidate.ttl")
    new PrintWriter(fileToValidate.toJava) {
      write(webid)
      close
    }

    var result = ""
    try {

      result = WebIdValidator.validateWithShacl(fileToValidate)
      if (result == "") {
        result = "Your WebId is valid."
        val model = WebIdUniformer.uniform(fileToValidate)
        //TODO uncomment next line to insert all valid results into virtuoso db
        VirtuosoHandler.insertModel(model)

        fileToValidate.delete()
        ModelToJSONConverter.toJSON(VirtuosoHandler.getModel())
        "redirect:static/exhibit/walloffame.html"

      }
      else {

        fileToValidate.delete()
        new ModelAndView("webid/result", "result", result)

      }
    } catch {
      case riot: RiotException => {
        result = riot.toString
        fileToValidate.delete()
        new ModelAndView("webid/result", "result", result)
      }
    }

  }

}
