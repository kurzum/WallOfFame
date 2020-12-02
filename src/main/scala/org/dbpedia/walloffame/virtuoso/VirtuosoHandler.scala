package org.dbpedia.walloffame.virtuoso

import java.io.{InputStream, InputStreamReader}

import better.files.File
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.apache.jena.util.FileManager
import org.dbpedia.walloffame.VosConfig
import virtuoso.jena.driver.{VirtGraph, VirtModel, VirtuosoQueryExecution, VirtuosoQueryExecutionFactory}

object VirtuosoHandler {

  def insertFile(file: File,vosConfig: VosConfig, subGraph:String) = {
    try {
      val model: Model = VirtModel.openDatabaseModel(vosConfig.graph.concat(subGraph), vosConfig.url, vosConfig.usr, vosConfig.psw)
      val in: InputStream = FileManager.get().open(file.pathAsString)
      if (in == null) {
        throw new IllegalArgumentException("File: " + file + " not found")
      }
      model.read(new InputStreamReader(in), null, "TURTLE")
      model.close()
    } catch {
      case e: Exception => System.out.println("Ex=" + e)
    }
  }

  def insertModel(model: Model, vosConfig: VosConfig, subGraph:String) = {
    val virtmodel: VirtModel = VirtModel.openDatabaseModel(vosConfig.graph.concat(subGraph), vosConfig.url, vosConfig.usr, vosConfig.psw)
    virtmodel.add(model)
    virtmodel.close()
  }

  def clearGraph(vosConfig: VosConfig, graph:String) = {
    val set = new VirtGraph(graph, vosConfig.url, vosConfig.usr, vosConfig.psw)
    set.clear()
  }

  def getModel(vosConfig: VosConfig, graph:String): Model = {
    val model: Model = VirtModel.openDatabaseModel(graph, vosConfig.url, vosConfig.usr, vosConfig.psw)
    model
  }

  def getAllGraphs(vosConfig: VosConfig):Seq[String] ={
    import org.apache.jena.query.{Query, QueryFactory}

    val virt = new VirtGraph("jdbc:virtuoso://localhost:1111", "dba", "dba")

    val sparql: Query = QueryFactory.create(
      s"""
        |SELECT  DISTINCT ?g
        |WHERE  {
        |   GRAPH ?g {?s ?p ?o}
        |   FILTER regex(?g, "^${vosConfig.graph}")
        |}
        |ORDER BY  ?g
      """.stripMargin)

    val vqe: VirtuosoQueryExecution = VirtuosoQueryExecutionFactory.create(sparql, virt)

    val results = vqe.execSelect

    var graphs = Seq.empty[String]

    while (results.hasNext) {
      val rs = results.nextSolution
      val s = rs.get("g")
      graphs=graphs:+s.toString
    }

    graphs
  }

  def getModelOfAllWebids(vosConfig: VosConfig):Model={
    val model = ModelFactory.createDefaultModel()
    VirtuosoHandler.getAllGraphs(vosConfig).foreach(graph =>
      model.add(VirtuosoHandler.getModel(vosConfig, graph))
    )
    model
  }

}
