package org.dbpedia.walloffame

import org.apache.jena.graph.Graph
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.apache.jena.shacl.lib.ShLib
import org.apache.jena.shacl.{ShaclValidator, Shapes}

object WallOfFame {

  def main(args: Array[String]): Unit = {
    crawlWebIds()
  }

  def crawlWebIds() = {
    val shapesPath: String = "shacl/shapes.ttl"
    val webIds = "crawl/tmp/webids/webid.ttl"

    val shapesGraph: Graph = RDFDataMgr.loadGraph(shapesPath)
    val webIdsGraph = RDFDataMgr.loadGraph(webIds)

    val shapes = Shapes.parse(shapesGraph)

    val report = ShaclValidator.get().validate(shapes, webIdsGraph)

    val it = report.getEntries.iterator()
    while (it.hasNext) println(it.next())

    ShLib.printReport(report)
    System.out.println()
    RDFDataMgr.write(System.out, report.getModel(), Lang.TTL)
  }

}
