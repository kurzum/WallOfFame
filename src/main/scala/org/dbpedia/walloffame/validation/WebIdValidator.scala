package org.dbpedia.walloffame.validation

import better.files.File
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.apache.jena.shacl.{ShaclValidator, Shapes}
import org.dbpedia.walloffame.uniform.QueryHandler

import java.io.{ByteArrayOutputStream, File => JavaFile}

object WebIdValidator {


  def validateWithShacl(webIdFile: File): (Boolean, Seq[(Boolean, String)]) = {

    import org.springframework.core.io.support.PathMatchingResourcePatternResolver
    val resolver = new PathMatchingResourcePatternResolver
    val resources = resolver.getResources("classpath:shacl/*.ttl")


    var result = Seq.empty[(Boolean, String)]
    var conforms = true
    val tmpShapeFile = File("./tmp/tmpShapeFile.ttl")

    for (resource <- resources) {
      //write shacl file out of jar, because Jena can't handle stream
      val is = resource.getInputStream
      val in = scala.io.Source.fromInputStream(is)
      val out = new java.io.PrintWriter(tmpShapeFile.toJava)
      try {
        in.getLines().foreach(out.println(_))
      }
      finally {
        out.close
      }

      val partResult = validate(webIdFile, tmpShapeFile)

      if (!partResult._1) conforms = false
      result = result :+ partResult
      tmpShapeFile.delete()
    }

    result.foreach(tuple => println(tuple._2))

    (conforms, result)
  }

  //  def validate(webIdFile: JavaFile, shapesFile: JavaFile): Option[String] = {
  //
  //    val shapesGraph = RDFDataMgr.loadGraph(shapesFile.getAbsolutePath)
  //    val dataGraph = RDFDataMgr.loadGraph(webIdFile.getAbsolutePath)
  //    val shapes = Shapes.parse(shapesGraph)
  //    val report = ShaclValidator.get.validate(shapes, dataGraph)
  //
  //    val out = new ByteArrayOutputStream()
  //    RDFDataMgr.write(out, report.getModel, Lang.TTL)
  //    if (report.conforms()) None
  //    else Option(out.toString)
  //  }

  def validate(webIdFile: File, shapesFile: File): (Boolean, String) = {

    val shapesGraph = RDFDataMgr.loadGraph(shapesFile.pathAsString)
    val dataGraph = RDFDataMgr.loadGraph(webIdFile.pathAsString)
    val shapes = Shapes.parse(shapesGraph)

    val report = ShaclValidator.get.validate(shapes, dataGraph)

    val out = new ByteArrayOutputStream()
    RDFDataMgr.write(out, report.getModel, Lang.TTL)

    val reportModel = report.getModel

    val query =
      """
        |PREFIX sh: <http://www.w3.org/ns/shacl#>
        |
        |SELECT (count(?error) as ?countErrors)
        |WHERE {
        |  ?report  a  sh:ValidationReport ;
        |           sh:result ?result .
        |  ?result  sh:resultSeverity sh:Violation ;
        |           sh:resultSeverity ?error.
        |}
        |""".stripMargin
    val result = QueryHandler.executeQuery(query, reportModel).head
    val conforms = result.getLiteral("?countErrors").getInt.equals(0)
    (conforms, out.toString)
  }
}
