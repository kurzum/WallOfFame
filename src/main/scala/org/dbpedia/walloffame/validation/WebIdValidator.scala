package org.dbpedia.walloffame.validation

import java.io.{ByteArrayOutputStream, FilenameFilter, File => JavaFile}

import better.files.File
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.apache.jena.shacl.{ShaclValidator, Shapes}


object WebIdValidator {

  val shapesDir = File("./shacl")


  def validateWithShacl(webIdFile: File): String = {
    val shapDir = shapesDir.toJava
    var result = ""

    val listedShapeFiles = shapDir.listFiles(new FilenameFilter {
      override def accept(file: JavaFile, name: String): Boolean = {
        name.matches(".*.ttl")
      }
    })

    listedShapeFiles.foreach(
      shapesFile => {
        val partResult = validate(webIdFile.toJava, shapesFile).getOrElse("")
        if (!(partResult == "")) result = result.concat(s"$partResult\n")
      })

    result
  }

  def validate(webIdFile: JavaFile, shapesFile: JavaFile): Option[String] = {

    val shapesGraph = RDFDataMgr.loadGraph(shapesFile.getPath)
    val dataGraph = RDFDataMgr.loadGraph(webIdFile.getPath)
    val shapes = Shapes.parse(shapesGraph)
    val report = ShaclValidator.get.validate(shapes, dataGraph)

    val out = new ByteArrayOutputStream()
    RDFDataMgr.write(out, report.getModel, Lang.TTL)

    if (report.conforms()) None
    else Option(out.toString)
  }
}
