package org.dbpedia.walloffame.webservice.controller

import java.util.Date

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class WelcomeController {


  private final val logger = LoggerFactory.getLogger(classOf[WelcomeController])

  @GetMapping(Array("/"))
  def index(model: Model): String = {
    logger.debug("Welcome to DBpedia's Wall of Fame")
    model.addAttribute("msg", getMessage())
    model.addAttribute("today", new Date())
    "index"
  }

  def getMessage(): String = {
    "Hello World"
  }

}
