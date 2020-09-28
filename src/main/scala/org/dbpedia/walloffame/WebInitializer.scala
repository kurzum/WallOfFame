package org.dbpedia.walloffame

import org.dbpedia.walloffame.config.SpringConfig
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  @Override
  protected def getRootConfigClasses(): Array[Class[_]] = {
    null
  }

  @Override
  protected def getServletConfigClasses(): Array[Class[_]] = {
    Array.fill[Class[_]](10)(classOf[SpringConfig])
  }

  @Override
  protected def getServletMappings(): Array[String] = {
    Array.fill[String](10)("/")
  }

}
