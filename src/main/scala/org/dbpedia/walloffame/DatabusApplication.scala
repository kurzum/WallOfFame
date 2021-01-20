package org.dbpedia.walloffame

import org.dbpedia.walloffame.spring.controller
import org.dbpedia.walloffame.spring.controller.{ValidationController, WoFController}
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.{EnableAutoConfiguration, SpringBootApplication}
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.{ComponentScan, Configuration, FilterType}
import org.springframework.core.`type`.filter.RegexPatternTypeFilter

import java.util.regex.Pattern

@Configuration
@EnableAutoConfiguration
@ComponentScan(
  basePackages = Array("org.dbpedia.walloffame"),
  useDefaultFilters = false,
  includeFilters = Array(new ComponentScan.Filter(`type` = FilterType.REGEX, pattern = Array("org.dbpedia.walloffame.Config", "org.dbpedia.walloffame.InitRunnerDatabus")))
  //  excludeFilters = Array(new ComponentScan.Filter(`type` = FilterType.REGEX, pattern = Array("org.dbpedia.walloffame.spring.controller.*", "org.dbpedia.walloffame.InitRunner")))
)
class DatabusApplication extends SpringBootServletInitializer {
  @Override
  protected override def configure(application: SpringApplicationBuilder): SpringApplicationBuilder = {
    application.sources(DatabusApplication.getClass)
  }
}


object DatabusApplication {
  def main(args: Array[String]): Unit = {
    val app = SpringApplication.run(classOf[DatabusApplication], args: _ *)
    SpringApplication.exit(app)
  }
}
