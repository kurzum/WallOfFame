package org.dbpedia.walloffame.spring.controller

import java.io.File

import javax.validation.Valid
import javax.validation.constraints.NotNull
import org.dbpedia.walloffame.spring.data.WebId
import org.dbpedia.walloffame.validation.WebIdValidator
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.RequestMethod._
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping, ResponseBody}
import org.springframework.web.servlet.ModelAndView

import scala.beans.BeanProperty

@Controller
class WebIdController {


  //value = Array("url") is the url the resulting site will be located at
  @RequestMapping(value = Array("/webid/validateWebId"), method = Array(GET))
  //viewname is the path to the related jsp file
  def showNewCustomerForm() = new ModelAndView("webid/validate", "validation", new WebIdPageData)

  @RequestMapping(value = Array("webid/validateWebId"), method = Array(POST))
  def sendWebIdToValidate(
                           @Valid @ModelAttribute("validation") validation: WebIdPageData, bindingResult: BindingResult) : ModelAndView= {
    if (bindingResult.hasErrors) {
      new ModelAndView("webid/validate", "validation", new WebIdPageData)
    } else {
      val newWebId = new WebId
      validation.copyTo(newWebId)

      import java.io.PrintWriter
      val fileToValidate = new File("webIdToValidate.ttl")

      new PrintWriter(fileToValidate) {
        write(newWebId.webid)
        close
      }

//      if (!WebIdValidator.validateWithShacl(new File("webIdToValidate.ttl"))) {
//        bindingResult.rejectValue("webid", "error.parseError", "Your WebId not valid.")
//      }

      var result = WebIdValidator.validateWithShacl(fileToValidate)
      if(result=="") result= "Your WebId is valid."
//      else result = result.replaceAll("\n", "<br/>")

      fileToValidate.delete()

      new ModelAndView("webid/validationResult", "result", result)
    }
  }
}



class WebIdPageData {
  @BeanProperty
  @NotNull
  @NotEmpty
  var webid: String = null

  override def toString = "[WebIdPageData: webid = " + this.webid + "]"

  def copyTo(webid: WebId): Unit = {
    // TODO: Use Dozer to do this automatically
    webid.webid = this.webid
  }

  def copyFrom(webid: WebId): Unit = {
    // TODO: Use Dozer to do this automatically
    this.webid = webid.webid
  }
}

object WebIdPageData {
  def apply(w: WebId): WebIdPageData = {
    val data = new WebIdPageData
    data.copyFrom(w)
    data
  }
}