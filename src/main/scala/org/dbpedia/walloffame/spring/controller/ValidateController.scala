package org.dbpedia.walloffame.spring.controller


import better.files.File
import com.sun.xml.internal.bind.v2.TODO
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
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping, ResponseBody}
import org.springframework.web.servlet.ModelAndView

import scala.beans.BeanProperty

@Controller
class ValidateController {


  //value = Array("url") is the url the resulting site will be located at
  @RequestMapping(value = Array("/validate"), method = Array(GET))
  //viewname is the path to the related jsp file
  def showNewCustomerForm() = new ModelAndView("webid/validate", "validation", new ValidatePageData)

  @RequestMapping(value = Array("validate"), method = Array(POST))
  def sendWebIdToValidate(@Valid @ModelAttribute("validation") validation: ValidatePageData, bindingResult: BindingResult) : ModelAndView= {
    if (bindingResult.hasErrors) {
      new ModelAndView("webid/validate", "validation", new ValidatePageData)
    } else {
      val newWebId = new WebId
      validation.copyTo(newWebId)

      import java.io.PrintWriter
      val fileToValidate = File("webIdToValidate.ttl")

      new PrintWriter(fileToValidate.toJava) {
        write(newWebId.webid)
        close
      }

//      if (!WebIdValidator.validateWithShacl(new File("webIdToValidate.ttl"))) {
//        bindingResult.rejectValue("webid", "error.parseError", "Your WebId not valid.")
//      }

      var result = ""

      try{
        result = WebIdValidator.validateWithShacl(fileToValidate)
        if(result=="") {
          result= "Your WebId is valid."
          val model = WebIdUniformer.uniform(fileToValidate)
          RDFtoJSONConverter.toJSON(model)
          //TODO uncomment next line to insert all valid results into virtuoso db
//          VirtuosoHandler.insertFile(fileToValidate)
          new ModelAndView("validatedWebId/webid")
        }
      } catch {
        case riot:RiotException => result = riot.toString
      }

      fileToValidate.delete()

      new ModelAndView("webid/validationResult", "result", result)
    }
  }
}



class ValidatePageData {
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

object ValidatePageData {
  def apply(w: WebId): ValidatePageData = {
    val data = new ValidatePageData
    data.copyFrom(w)
    data
  }
}