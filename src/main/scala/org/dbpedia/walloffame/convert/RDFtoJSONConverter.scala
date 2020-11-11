package org.dbpedia.walloffame.convert

import better.files.File
import org.apache.jena.rdf.model.Model
import org.dbpedia.walloffame.uniform.QueryHandler
import org.dbpedia.walloffame.uniform.queries.SelectQueries

import scala.collection.mutable.ListBuffer

object RDFtoJSONConverter {

  def toJSON(model:Model): Unit = {

    val results = QueryHandler.executeQuery(SelectQueries.getQueryWebIdData(), model)

    val items = new ListBuffer[String]

    val result = results.head

//    val webid = result.getResource("?webid").toString
//    val maker = result.getResource("?maker").toString
//    val name = result.getLiteral("?name").getLexicalForm
//    val keyname = result.getLiteral("?keyname").getLexicalForm
//    val keyvalue = result.getLiteral("?keyvalue").getLexicalForm

    def addToListBuffer(varName:String, entry:String){
      items +=
        s"""
           |"$varName": "$entry"
           |""".stripMargin
    }

    addToListBuffer("webid", result.getResource("?webid").toString)
    addToListBuffer("maker", result.getResource("?maker").toString)
    addToListBuffer("name", result.getLiteral("?name").getLexicalForm)
    addToListBuffer("keyname", result.getLiteral("?keyname").getLexicalForm)
    addToListBuffer("keyvalue", result.getLiteral("?keyvalue").getLexicalForm)

    val rawJSON = s"""
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
        |       { ${items.toList.mkString(",")}
        |       }
        |    ]
        |}
      """.stripMargin

    import spray.json._
    val outJSON= rawJSON.parseJson

    import java.io.PrintWriter
    val outFile = File("./src/main/webapp/WEB-INF/jsp/exhibit/validatedWebId/webid.js")

    new PrintWriter(outFile.toJava) {
      write(outJSON.prettyPrint)
      close
    }

  }
}
