package org.dbpedia.walloffame.uniform

import better.files.File
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.apache.jena.riot.RDFDataMgr
import org.dbpedia.walloffame.crawling.WebIdCrawler
import org.dbpedia.walloffame.uniform.queries.{ConstructQueries, SelectQueries}
import org.dbpedia.walloffame.validation.WebIdValidator
import org.slf4j.{Logger, LoggerFactory}

object WebIdUniformer {

  val logger:Logger = LoggerFactory.getLogger("validator")

  def uniformWebIds(dir:File):Model = {

    val constructModel = ModelFactory.createDefaultModel()

    if (dir.exists && dir.isDirectory) {
      dir.listRecursively().foreach(file => {
        println(file.pathAsString)
        if(WebIdValidator.validateWithShacl(file.toJava).isEmpty) uniform(file, constructModel)
      })
    }

    val stmts = constructModel.listStatements()
    while (stmts.hasNext) println(stmts.nextStatement())

    constructModel
  }


  def uniform(webidFile: File, constructModel:Model): Boolean = {

    val model = RDFDataMgr.loadModel(webidFile.pathAsString)

    def construct(constructQuery: String):Boolean = {
      QueryHandler.executeConstructQuery(
        constructQuery,
        model,
        constructModel
      )
    }

    if (!construct(ConstructQueries.constructWebId())) {
      logger.error(s"mandatory item(s) not found for ${webidFile.name}.")
      return false
    }
//
//    def log(notFound:String) ={
//      logger.error(s"no $notFound found for ${webidFile.name}.")
//    }
//
//    ConstructQueries.getClass.getMethods.foreach(
//      meth =>
//        if (!construct(meth)) {
//          log("makerURL")
//          return false
//        }
//    )
//
//    val webIdResource = QueryHandler.executeSingleResultQuery(SelectQueries.getWebIdURL(), constructModel).asResource()
//    if (!construct(ConstructQueries.constructMakerURL(webIdResource))) {
//      log("makerURL")
//      return false
//    }
//
//    val makerResource = QueryHandler.executeSingleResultQuery(SelectQueries.getMakerURL(webIdResource), constructModel).asResource()
//    if (!construct(ConstructQueries.constructMakerName(makerResource))) {
//      log("makerName")
//      return false
//    }
//
//    if (!construct(ConstructQueries.constructCertValue(makerResource))) {
//      log("certificate")
//      return false
//    }



    true
  }

}
