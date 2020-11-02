package org.dbpedia.walloffame.spring.controller

import org.springframework.stereotype.Controller
import org.hibernate.SessionFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod._
import org.springframework.web.servlet.ModelAndView

@Controller
class HomePageController {
  implicit def sessionFactory2Session(sf: SessionFactory) = sf.getCurrentSession

  @RequestMapping(value = Array("/home"), method = Array(GET))
  def loadCustomers() =
    new ModelAndView("home")
}
