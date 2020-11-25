package org.dbpedia.walloffame

import org.dbpedia.walloffame.crawling.WebIdCrawler
import org.dbpedia.walloffame.uniform.WebIdUniformer
import org.dbpedia.walloffame.virtuoso.VirtuosoHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class InitRunner extends CommandLineRunner {

  @Autowired
  private var config: Config = _

  override def run(args: String*): Unit = {
    val dir = WebIdCrawler.crawl()
    val uniformedModel = WebIdUniformer.uniformWebIds(dir)
    dir.parent.delete()
    VirtuosoHandler.insertModel(uniformedModel,config.virtuoso)
  }
}
