import better.files.File
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.RDFDataMgr
import org.dbpedia.walloffame.WebIdValidator
import org.dbpedia.walloffame.queries.QueryHandler
import org.dbpedia.walloffame.queries.webid.{ConstructQueries, SelectQueries}
import org.scalatest.flatspec.AnyFlatSpec

class WebIdFilterTest extends AnyFlatSpec {

  "filter" should "pass correct result" in {
    val webid = "/home/eisenbahnplatte/git/Eisenbahnplatte/WallOfFame/crawl/tmp/webids/webid1.ttl"
    val model = RDFDataMgr.loadModel(webid)

    val queryStr =
      s"""
         |PREFIX foaf: <http://xmlns.com/foaf/0.1/>
         |PREFIX w3cert: <http://www.w3.org/ns/auth/cert#>
         |
         |SELECT ?webid ?maker ?name ?keyvalue {
         |  ?webid a foaf:PersonalProfileDocument ;
         |        foaf:maker ?maker .
         |  ?maker foaf:name ?name ;
         |         w3cert:key ?key .
         |  ?key w3cert:modulus ?keyvalue .
         |}
    """.stripMargin


    val results = QueryHandler.executeQuery(queryStr, model)

    if (results.nonEmpty) {
      val result = results.head
      Seq[String](
        result.getResource("webid").toString,
        result.getResource("maker").toString,
        result.getLiteral("name").getString,
        result.getLiteral("keyvalue").getString).foreach(println(_))


    }


  }

  //  "constructQuery" should "return model with correct triples" in {
  //
  //    val webid = "/home/eisenbahnplatte/git/Eisenbahnplatte/WallOfFame/crawl/tmp/webids/webid1.ttl"
  //    val model = RDFDataMgr.loadModel(webid)
  //
  //
  //    val queryStr=
  //      """
  //        |PREFIX foaf: <http://xmlns.com/foaf/0.1/>
  //        |
  //        |CONSTRUCT   { ?webid a foaf:PersonalProfileDocument }
  //        |WHERE       { ?webid a foaf:PersonalProfileDocument }
  //        |""".stripMargin
  //
  //
  //    val constructModel = QueryHandler.executeConstructQuery(queryStr, model)
  //
  //    val stmts = constructModel.listStatements()
  //    while(stmts.hasNext) {
  //      println(stmts.nextStatement())
  //    }
  //  }
  //
  //  "filter" should "work correctly" in {
  //    val webid = "/home/eisenbahnplatte/git/Eisenbahnplatte/WallOfFame/crawl/tmp/webids/webid1.ttl"
  //
  //    val model = RDFDataMgr.loadModel(webid)
  //
  //    val validator = QueryHandler
  //    var result = validator.executeQuery(SelectQueries.getWebIdURL(),model)
  //    val webidURL = validator.getSingleResult(result).asResource()
  //
  //    result = validator.executeQuery(SelectQueries.getMakerURL(webidURL),model)
  //    val makerURL = validator.getSingleResult(result).toString
  //
  //    result = validator.executeQuery(SelectQueries.getMakerName(makerURL),model)
  //    println(validator.getSingleResult(result))
  //  }

  "construct" should "work properly" in {
    val webid = "/home/eisenbahnplatte/git/Eisenbahnplatte/WallOfFame/crawl/tmp/webids/webid1.ttl"

    val model = RDFDataMgr.loadModel(webid)
    val constructModel = ModelFactory.createDefaultModel()
    val validator = QueryHandler

    validator.executeConstructQuery(
      ConstructQueries.constructWebIdURL(),
      model,
      constructModel
    )

    validator.executeConstructQuery(
      ConstructQueries.constructMakerURL(validator.executeSingleResultQuery(SelectQueries.getWebIdURL(), constructModel).asResource()),
      model,
      constructModel
    )
  }

  "construct2" should "work properly" in {
    val webid = "/home/eisenbahnplatte/git/Eisenbahnplatte/WallOfFame/crawl/tmp/webids/webid1.ttl"
    WebIdValidator.check(File(webid))
  }


}
