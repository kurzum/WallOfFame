package org.dbpedia.walloffame

import java.io.FileOutputStream

import better.files.File
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.dbpedia.walloffame.queries.QueryHandler
import org.dbpedia.walloffame.queries.webid.{ConstructQueries, SelectQueries}
import org.slf4j.LoggerFactory

object WebIdValidator {

  val logger = LoggerFactory.getLogger("validator")
  val constructModel = ModelFactory.createDefaultModel()

  def checkWebIds(dir: File) = {
    val constructedWebIdFile = File("constructedWebIds.ttl")

    if (dir.exists && dir.isDirectory) {
      dir.listRecursively().foreach(file => check(file))
    }

    val os = new FileOutputStream(constructedWebIdFile.toJava)
    RDFDataMgr.write(os, constructModel, Lang.TTL)
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

    if (!construct(ConstructQueries.constructWebIdURL())) {
      logger.error(s"no webIdURL found for ${webidFile.name}.")
      return false
    }

    val webIdResource = queryHandler.executeSingleResultQuery(SelectQueries.getWebIdURL(), constructModel).asResource()
    if (!construct(ConstructQueries.constructMakerURL(webIdResource))) {
      logger.error(s"no makerURL found for ${webidFile.name}.")
      return false
    }

    val makerResource = queryHandler.executeSingleResultQuery(SelectQueries.getMakerURL(webIdResource), constructModel).asResource()
    if (!construct(ConstructQueries.constructMakerName(makerResource))) {
      logger.error(s"no makerName found for ${webidFile.name}.")
      return false
    }

    if (!construct(ConstructQueries.constructCertValue(makerResource))) {
      logger.error(s"no certificate found for ${webidFile.name}.")
      return false
    }

    val stmts = constructModel.listStatements()
    while (stmts.hasNext) println(stmts.nextStatement())

    true
  }


}
