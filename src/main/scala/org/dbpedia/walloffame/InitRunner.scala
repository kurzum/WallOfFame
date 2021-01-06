package org.dbpedia.walloffame

import better.files.File
import org.apache.jena.rdf.model.ModelFactory
import org.dbpedia.walloffame.convert.ModelToJSONConverter
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
    File("./tmp/").delete(true)
    File("./tmp/").createDirectory()

    prepareWallOfFame()
  }

  def prepareWallOfFame() = {
    //crawl webids
    val dir = WebIdCrawler.crawl()

    //delete all graphs from Session before!
    var wait = true
    while (wait) {
      try {
        VirtuosoHandler.getAllGraphs(config.virtuoso).foreach(graph => VirtuosoHandler.clearGraph(config.virtuoso, graph))
        wait = false
      } catch {
        case e: Exception =>
          println("waiting for vos to start up")
          Thread.sleep(1000)
      }
    }

    //insert all uniformed webids into virtuoso
    dir.children.foreach(webid =>{
      val uniformedModel = WebIdUniformer.uniform(webid)
      VirtuosoHandler.insertModel(uniformedModel,config.virtuoso, webid.nameWithoutExtension)
    })

    //create json for exhibit
    ModelToJSONConverter.createJSONFile(
      VirtuosoHandler.getModelOfAllWebids(config.virtuoso),
      File(config.exhibit.file)
    )
  }
}
