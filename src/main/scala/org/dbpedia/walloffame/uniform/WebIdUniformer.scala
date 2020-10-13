package org.dbpedia.walloffame.uniform

import better.files.File
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.apache.jena.riot.RDFDataMgr
import org.dbpedia.walloffame.crawling.WebIdCrawler
import org.dbpedia.walloffame.uniform.queries.{ConstructQueries, SelectQueries}
import org.slf4j.LoggerFactory

object WebIdUniformer {

  val logger = LoggerFactory.getLogger("validator")
  val constructModel = ModelFactory.createDefaultModel()

  def uniformWebIds(outFile:File):Model = {
    val dir = WebIdCrawler.crawl()

    if (dir.exists && dir.isDirectory) {
      dir.listRecursively().foreach(file => check(file))
    }

    constructModel
  }


  def check(webidFile: File): Boolean = {

    val model = RDFDataMgr.loadModel(webidFile.pathAsString)
    val queryHandler = QueryHandler

    def construct(constructQuery: String) = {
      QueryHandler.executeConstructQuery(
        constructQuery,
        model,
        constructModel
      )
    }

    def log(notFound:String) ={
      logger.error(s"no $notFound found for ${webidFile.name}.")
    }

    if (!construct(ConstructQueries.constructWebIdURL())) {
      log("webIdURL")
      return false
    }

    val webIdResource = queryHandler.executeSingleResultQuery(SelectQueries.getWebIdURL(), constructModel).asResource()
    if (!construct(ConstructQueries.constructMakerURL(webIdResource))) {
      log("makerURL")
      return false
    }

    val makerResource = queryHandler.executeSingleResultQuery(SelectQueries.getMakerURL(webIdResource), constructModel).asResource()
    if (!construct(ConstructQueries.constructMakerName(makerResource))) {
      log("makerName")
      return false
    }

    if (!construct(ConstructQueries.constructCertValue(makerResource))) {
      log("certificate")
      return false
    }

    val stmts = constructModel.listStatements()
    while (stmts.hasNext) println(stmts.nextStatement())

    true
  }

}
