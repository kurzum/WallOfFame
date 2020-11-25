package org.dbpedia.walloffame.convert

import better.files.File
import org.apache.jena.rdf.model.Model
import org.dbpedia.walloffame.uniform.QueryHandler
import org.dbpedia.walloffame.uniform.queries.SelectQueries

import scala.collection.mutable.ListBuffer

object ModelToJSONConverter {

  def toJSON(model: Model): File = {
    //
    //    val stms = model.listStatements()
    //    while (stms.hasNext) println(stms.nextStatement())

    val results = QueryHandler.executeQuery(SelectQueries.getQueryWebIdData(), model)

    val items = new ListBuffer[ListBuffer[String]]

    results.foreach(result => {
      val item = new ListBuffer[String]

      def addToListBuffer(varName: String, entry: String) {
        item +=
          s"""
             |"$varName": "$entry"
             |""".stripMargin
      }

      addToListBuffer("webid", result.getResource("?webid").toString)
      addToListBuffer("maker", result.getResource("?maker").toString)
      addToListBuffer("name", result.getLiteral("?name").getLexicalForm)
      addToListBuffer("keyname", result.getLiteral("?keyname").getLexicalForm)
      addToListBuffer("keyvalue", result.getLiteral("?keyvalue").getLexicalForm)

      items += item
    })


    var rawJSON =
      s"""
         |{
         |    "types": {
         |        "WebId": {
         |            "pluralLabel": "WebIds"
         |        }
         |    },
         |    "properties": {
         |        "webid": {
         |            "valueType": "url"
         |        },
         |        "type": {
         |            "valueType": "url"
         |        },
         |        "maker": {
         |            "valueType": "url"
         |        },
         |        "primaryTopic": {
         |            "valueType": "url"
         |        }
         |    },
         |    "items": [
      """.stripMargin

    items.foreach(item => rawJSON = rawJSON.concat(s"{ ${item.toList.mkString(",")} },"))

    rawJSON = rawJSON.dropRight(1).concat("]}")

    import spray.json._
    val outJSON = rawJSON.parseJson

//    import org.springframework.core.io.support.PathMatchingResourcePatternResolver
//    val resolver = new PathMatchingResourcePatternResolver
//    val resources = resolver.getResources("classpath:shacl/*.ttl")

    import java.io.PrintWriter
    val outFile = File("webids.js")

    new PrintWriter(outFile.toJava) {
      write(outJSON.prettyPrint)
      close
    }

    outFile
  }
}
