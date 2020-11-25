package org.dbpedia.walloffame.virtuosoTest

import java.io.{ByteArrayOutputStream, InputStream, InputStreamReader}

import org.apache.jena.rdf.model.Model
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.apache.jena.util.FileManager
import org.junit.jupiter.api.Test
import virtuoso.jena.driver.{VirtGraph, VirtModel, VirtuosoQueryExecution, VirtuosoQueryExecutionFactory, VirtuosoUpdateFactory}


class VirtuosoHandler {



  @Test
  def insertTriples(): Unit ={
    clearGraph("webids")

    val set = new VirtGraph ("jdbc:virtuoso://localhost", "dba", "dba")

    val str = "INSERT INTO GRAPH <webids> { <aa> <bb> 'cc' . <aa1> <bb1> 123. }"
    val vur = VirtuosoUpdateFactory.create(str, set)

    vur.exec()


    readFromGraph("webids", set)
  }

  @Test
  def insertFile(): Unit ={
    clearGraph("http://webids")

    try {
      val file = "./src/test/resources/correctWebId.ttl"

      val model: Model = VirtModel.openDatabaseModel("http://webids", "jdbc:virtuoso://localhost:1111", "dba", "dba")
      val in: InputStream = FileManager.get().open(file)
      if (in == null) {
        throw new IllegalArgumentException("File: " + file + " not found")
      }
      model.read(new InputStreamReader(in), null, "TURTLE")
      model.close()
    } catch {
      case e:Exception => System.out.println("Ex="+e)
    }

    readFromGraph("http://webids", new VirtGraph("jdbc:virtuoso://localhost:1111", "dba", "dba"))

  }

  @Test
  def insertDataToJenaModel(): Unit ={
    clearGraph("http://webids")

    try {
      val file = "./src/test/resources/correctWebId.ttl"

      val model: Model = VirtModel.openDatabaseModel("http://webids", "jdbc:virtuoso://localhost:1111", "dba", "dba")
      val in: InputStream = FileManager.get().open(file)
      if (in == null) {
        throw new IllegalArgumentException("File: " + file + " not found")
      }
      model.read(new InputStreamReader(in), null, "TURTLE")
      model.close()
    } catch {
      case e:Exception => System.out.println("Ex="+e)
    }

    val model2: Model = VirtModel.openDatabaseModel("http://webids", "jdbc:virtuoso://localhost:1111", "dba", "dba")
    val out = new ByteArrayOutputStream()
    RDFDataMgr.write(out, model2, Lang.TTL)
    println(out)

  }

  def clearGraph(graphName: String)={
    val set = new VirtGraph(graphName,"jdbc:virtuoso://localhost:1111", "dba", "dba")
    set.clear()
  }

  def readFromGraph(graphName:String, graph:VirtGraph) ={
    import org.apache.jena.query.{Query, QueryFactory}

    val sparql: Query = QueryFactory.create(s"SELECT * FROM <$graphName> WHERE { ?s ?p ?o }")

    val vqe: VirtuosoQueryExecution = VirtuosoQueryExecutionFactory.create(sparql, graph)

    val results = vqe.execSelect
    while (results.hasNext) {
      val rs = results.nextSolution
      val s = rs.get("s")
      val p = rs.get("p")
      val o = rs.get("o")
      System.out.println(" { " + s + " " + p + " " + o + " . }")
    }
  }
}
