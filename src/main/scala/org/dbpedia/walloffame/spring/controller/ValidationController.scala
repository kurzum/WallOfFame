package org.dbpedia.walloffame.spring.controller

import better.files.File
import org.apache.commons.io.IOUtils
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.{RDFDataMgr, RiotException}
import org.dbpedia.walloffame.Config
import org.dbpedia.walloffame.convert.ModelToJSONConverter
import org.dbpedia.walloffame.spring.model.{Result, WebId}
import org.dbpedia.walloffame.uniform.WebIdUniformer
import org.dbpedia.walloffame.validation.WebIdValidator
import org.dbpedia.walloffame.virtuoso.VirtuosoHandler
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PostMapping}
import org.springframework.web.servlet.ModelAndView

import java.io
import java.io.FileInputStream
import javax.servlet.http.HttpServletResponse

@Controller
class ValidationController(config: Config) {

  //value = Array("url") is the url the resulting site will be located at
  //viewname is the path to the related jsp file
  @GetMapping(value = Array("/", "/validate"))
  def getValidate(modelAndView: ModelAndView): ModelAndView = {
    val webid = new WebId
    val str =
      """
        |@base <https://raw.githubusercontent.com/Eisenbahnplatte/eisenbahnplatte.github.io/master/webid.ttl> .
        |@prefix foaf: <http://xmlns.com/foaf/0.1/> .
        |@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
        |@prefix cert: <http://www.w3.org/ns/auth/cert#> .
        |@prefix rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
        |
        |<> a foaf:PersonalProfileDocument ;
        |   foaf:maker <#this> ;
        |   foaf:primaryTopic <#this> .
        |
        |<#this> a foaf:Person ;
        |   foaf:name "Eisenbahnplatte";
        |   foaf:img <https://eisenbahnplatte.github.io/eisenbahnplatte.jpeg>;
        |   foaf:gender "male";
        |   foaf:geekcode "GMU GCS s: d? !a L++ PS+++ PE- G h";
        |   foaf:firstname "Fabian";
        |
        |cert:key [
        |      a cert:RSAPublicKey;
        |      rdfs:label "HP Elitebook";
        |      cert:modulus "C133F14349AC1035EC007228975FA276E52A7D4E2F227710D645C616E92666C861838AFF268491990F9C30F6999E2C62DF3379DA0FDCE300CF1BED6B37F25FF9ADD5BD242E346E1C25E33891A95BD9B998D177D389A163B150383FE6EE1D9F479B2F186EF0BB11B4E8AC87AEB2414BA653741E87E8E72A083D00C813B1242158FFC957089C97044241DBC9CAE553CEE5B869A3667596E4E6A34998CEE9A588617B54432010CCDCF5EC7C4140B6AA3422AB089E5676847F727DA8762D1BA35FA4F0593AF91BFFA5AA4B433C07F1982CA22F1BEB1B538C8890632608C04E4A4E9129C1AA4575BAAE9014E30C0D7A5F96D98BCB4C5D0C794A8B5A2A7D823ECC5411"^^xsd:hexBinary;
        |      cert:exponent "65537"^^xsd:nonNegativeInteger
        |     ] .
        |""".stripMargin

    webid.setTurtle(str)

    import java.io.PrintWriter
    val fileToValidate = File("./tmp/webIdToValidate.ttl")
    new PrintWriter(fileToValidate.toJava) {
      write(str)
      close
    }

    val model = RDFDataMgr.loadModel(fileToValidate.pathAsString)

    webid.insertFieldsFromTurtle(model)
    modelAndView.addObject("webid", webid)
    modelAndView.addObject("result", new Result)
    modelAndView.addObject("shortResult", "")
    modelAndView.setViewName("validate")
    modelAndView
  }

  @PostMapping(value = Array("/", "validate"))
  def sendWebIdToValidate(@ModelAttribute("webid") newWebId: WebId): ModelAndView = {

    val turtle = newWebId.turtle

    //write file from webid string
    import java.io.PrintWriter
    val fileToValidate = File("./tmp/webIdToValidate.ttl")
    new PrintWriter(fileToValidate.toJava) {
      write(turtle)
      close()
    }

    try {

      val result = WebIdValidator.validate(fileToValidate)

      if (result.conforms) {
        //valid webid

        val model = WebIdUniformer.uniform(fileToValidate)

        newWebId.insertFieldsFromTurtle(model)
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
        //        val webids = ModelToJSONConverter.toJSON(model)
        val modelView = new ModelAndView("validate", "result", result)
        modelView.addObject("webid", newWebId)
      }
      else {
        //invalide webid
        fileToValidate.delete()
        new ModelAndView("validate", "result", result)

      }
    } catch {
      case riot: RiotException => {
        val result = new Result
        result.addViolation("RiotException", riot.toString)
        fileToValidate.delete()
        new ModelAndView("validate", "result", result)
      }
    }

  }

  @GetMapping(value = Array("/webids.js"), produces = Array("application/json"))
  def getJson(response: HttpServletResponse): Unit = {
    try {

      IOUtils.copy(new FileInputStream(new io.File(config.exhibit.file)), response.getOutputStream)
      response.setStatus(200)
    } catch {
      case e: Exception => response.setStatus(500)
    }
  }

}
