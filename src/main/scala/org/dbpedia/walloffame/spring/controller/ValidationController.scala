package org.dbpedia.walloffame.spring.controller


import better.files.File
import javax.validation.Valid
import javax.validation.constraints.NotNull
import org.apache.jena.riot.RiotException
import org.dbpedia.walloffame.convert.RDFtoJSONConverter
import org.dbpedia.walloffame.spring.data.WebId
import org.dbpedia.walloffame.uniform.WebIdUniformer
import org.dbpedia.walloffame.validation.WebIdValidator
import org.dbpedia.walloffame.virtuoso.VirtuosoHandler
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMethod._
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.web.servlet.ModelAndView

import scala.beans.BeanProperty

@Controller
class ValidationController {


  //value = Array("url") is the url the resulting site will be located at
  @RequestMapping(value = Array("/validateWebId"), method = Array(GET))
  //viewname is the path to the related jsp file
  def showNewValidationForm() = new ModelAndView("webid/validate.jsp", "validation", new ValidationPageData)


  @RequestMapping(value = Array("validateWebId"), method = Array(POST))
  def sendWebIdToValidate(@Valid @ModelAttribute("validation") validation: ValidationPageData, bindingResult: BindingResult) = {

    if (bindingResult.hasErrors) {

      new ModelAndView("webid/validate.jsp", "validation", new ValidationPageData)

    } else {

      val newWebId = new WebId
      validation.copyTo(newWebId)

      import java.io.PrintWriter
      val fileToValidate = File("webIdToValidate.ttl")
      new PrintWriter(fileToValidate.toJava) {
        write(newWebId.webid)
        close
      }

      var result = ""
      try{

        result = WebIdValidator.validateWithShacl(fileToValidate)
        if(result=="") {
          result= "Your WebId is valid."
          val model = WebIdUniformer.uniform(fileToValidate)

          //TODO uncomment next line to insert all valid results into virtuoso db
          VirtuosoHandler.insertModel(model)

          fileToValidate.delete()
          RDFtoJSONConverter.toJSON(VirtuosoHandler.getModel())
          "redirect:static/exhibit/walloffame.html"

        }
        else {

          fileToValidate.delete()
          new ModelAndView("webid/result.jsp", "result", result)

        }
      } catch {
        case riot:RiotException => {
          result = riot.toString
          fileToValidate.delete()
          new ModelAndView("webid/result.jsp", "result", result)}
      }


    }
  }

}



class ValidationPageData {
  @BeanProperty
  @NotNull
  @NotEmpty
  var webid: String = _

  override def toString:String = "[WebIdPageData: webid = " + this.webid + "]"

  def copyTo(webid: WebId): Unit = {
    webid.webid = this.webid
  }

  def copyFrom(webid: WebId): Unit = {
    this.webid = webid.webid
  }
}

object ValidationPageData {
  def apply(w: WebId): ValidationPageData = {
    val data = new ValidationPageData
    data.copyFrom(w)
    data
  }
}