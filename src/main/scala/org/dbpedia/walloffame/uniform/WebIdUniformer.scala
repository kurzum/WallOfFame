package org.dbpedia.walloffame.uniform

import better.files.File
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.apache.jena.riot.RDFDataMgr
import org.dbpedia.walloffame.uniform.queries.{ConstructOptionalQueries, ConstructQueries}
import org.dbpedia.walloffame.validation.WebIdValidator
import org.slf4j.{Logger, LoggerFactory}

object WebIdUniformer {

  val logger: Logger = LoggerFactory.getLogger("validator")

  def uniformWebIds(dir: File): Model = {

    val constructModel = ModelFactory.createDefaultModel()

    if (dir.exists && dir.isDirectory) {
      dir.listRecursively().foreach(file => {
        println(file.pathAsString)
        if (WebIdValidator.validateWithShacl(file).isEmpty) uniform(file, constructModel)
      })
    }

    //    val stmts = constructModel.listStatements()
    //    while (stmts.hasNext) println(stmts.nextStatement())

    constructModel
  }


  def uniform(webidFile: File, constructModel: Model): Boolean = {

    val model = RDFDataMgr.loadModel(webidFile.pathAsString)

    def construct(constructQuery: String): Boolean = {
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
    true
  }

  def uniform(webidFile: File): Model = {

    val model = RDFDataMgr.loadModel(webidFile.pathAsString)
    val constructModel = ModelFactory.createDefaultModel()

    def construct(constructQuery: String): Boolean = {
      QueryHandler.executeConstructQuery(
        constructQuery,
        model,
        constructModel
      )
    }

    if (!construct(ConstructQueries.constructWebId())) {
      logger.error(s"mandatory item(s) not found for ${webidFile.name}.")
      return constructModel
    }


    if (!construct(ConstructOptionalQueries.constructFirstName())) {
      logger.error(s"mandatory item(s) not found for ${webidFile.name}.")
      return constructModel
    }
    if (!construct(ConstructOptionalQueries.constructGeekCode())) {
      logger.error(s"mandatory item(s) not found for ${webidFile.name}.")
      return constructModel
    }
    if (!construct(ConstructOptionalQueries.constructGender())) {
      logger.error(s"mandatory item(s) not found for ${webidFile.name}.")
      return constructModel
    }
    if (!construct(ConstructOptionalQueries.constructImg())) {
      logger.error(s"mandatory item(s) not found for ${webidFile.name}.")
      return constructModel
    }
    if (!construct(ConstructOptionalQueries.constructName())) {
      logger.error(s"mandatory item(s) not found for ${webidFile.name}.")
      return constructModel
    }


//    ConstructOptionalQueries.getClass.getMethods.foreach(method =>
//      if (!construct(method)) {
//        logger.warn(s"optional item(s) not found for ${webidFile.name}.")
//      }
//    )


    constructModel
  }

}
