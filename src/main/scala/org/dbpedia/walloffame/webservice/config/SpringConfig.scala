package org.dbpedia.walloffame.webservice.config

import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}
import org.springframework.web.servlet.config.annotation.{EnableWebMvc, ResourceHandlerRegistry, WebMvcConfigurer}
import org.springframework.web.servlet.view.{InternalResourceViewResolver, JstlView};

@EnableWebMvc
@Configuration
@ComponentScan(Array("org.dbpedia.walloffame"))
class SpringConfig extends WebMvcConfigurer {

  @Override
  override def addResourceHandlers(registry: ResourceHandlerRegistry) {
    registry.addResourceHandler("/resources/**")
      .addResourceLocations("/resources/")
  }

  @Bean
  def viewResolver(): InternalResourceViewResolver = {
    val viewResolver = new InternalResourceViewResolver()
    viewResolver.setViewClass(classOf[JstlView])
    viewResolver.setPrefix("/WEB-INF/views/")
    viewResolver.setSuffix(".jsp")

    viewResolver
  }
}