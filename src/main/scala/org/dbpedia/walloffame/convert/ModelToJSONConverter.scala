package org.dbpedia.walloffame.convert

import java.io.FileWriter
import java.nio.file.{Files, Paths}

import better.files.File
import better.files.File.OpenOptions
import org.apache.jena.rdf.model.Model
import org.dbpedia.walloffame.uniform.QueryHandler
import org.dbpedia.walloffame.uniform.queries.SelectQueries
import scala.collection.mutable.ListBuffer

object ModelToJSONConverter {


  def createJSONFile(model: Model, outFile:File):File={
    val json = toJSON(model)

    val bw = outFile.newBufferedWriter
    bw.write(json)
    bw.close()

    outFile
  }

//  def appendToJSONFile(model: Model, jsonFile:File):File={
//    val json = toJSON(model)
//
//    val bw = jsonFile.newBufferedWriter(openOptions = OpenOptions.append)
//    bw.write(json)
//    bw.close()
//
//    jsonFile
//  }


  def toJSON(model: Model): String = {

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
    rawJSON.parseJson.prettyPrint
  }
}
