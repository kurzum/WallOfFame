package org.dbpedia.walloffame

import better.files.File

object WallOfFame {

  def main(args: Array[String]): Unit = {
    val webIdDir = File("./crawl/tmp/webids/")
    WebIdValidator.checkWebIds(webIdDir)
  }

  //  def checkWebIdsWithShacl() = {
  //    val shapesPath: String = "shacl/shapes.ttl"
  //    val webIds = "crawl/tmp/webids/webid.ttl"
  //
  //    val shapesGraph: Graph = RDFDataMgr.loadGraph(shapesPath)
  //    val webIdsGraph = RDFDataMgr.loadGraph(webIds)
  //
  //    val shapes = Shapes.parse(shapesGraph)
  //
  //    val report = ShaclValidator.get().validate(shapes, webIdsGraph)
  //
  //    val it = report.getEntries.iterator()
  //    while (it.hasNext) println(it.next())
  //
  //    ShLib.printReport(report)
  //    System.out.println()
  //    RDFDataMgr.write(System.out, report.getModel(), Lang.TTL)
  //  }

}
