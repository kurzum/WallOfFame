package org.dbpedia.walloffame

import org.dbpedia.walloffame.crawling.WebIdCrawler
import org.dbpedia.walloffame.uniform.WebIdUniformer
import org.dbpedia.walloffame.virtuoso.VirtuosoHandler
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Application {}

object Application {
  def main(args: Array[String]): Unit = {
    val dir = WebIdCrawler.crawl()
    val uniformedModel = WebIdUniformer.uniformWebIds(dir)
    dir.parent.delete()
    VirtuosoHandler.insertModel(uniformedModel)

    SpringApplication.run(classOf[Application], args: _ *)
  }
}