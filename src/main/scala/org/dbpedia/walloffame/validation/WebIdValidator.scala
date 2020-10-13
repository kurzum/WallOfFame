package org.dbpedia.walloffame.validation
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File, FilenameFilter}

import org.apache.jena.riot.Lang
import org.apache.jena.riot.RDFDataMgr
import org.apache.jena.shacl.ShaclValidator
import org.apache.jena.shacl.lib.ShLib
import org.apache.jena.shacl.Shapes

object WebIdValidator {

  def validateWithShacl(webIdFile: File): String = {
    val shapesDir = new File("./shacl")
    var result = ""

    val listedShapeFiles = shapesDir.listFiles(new FilenameFilter {
      override def accept(file: File, name: String): Boolean = {
        name.matches(".*.ttl")
      }
    })

    listedShapeFiles.foreach(
      shapesFile => {
        val partResult = validate(webIdFile, shapesFile).getOrElse("")
        if (!(partResult=="")) result=result.concat(s"$partResult\n")
      })

    result
  }

  def validate(webIdFile: File, shapesFile: File): Option[String] = {


    val shapesGraph = RDFDataMgr.loadGraph(shapesFile.getPath)
    val dataGraph = RDFDataMgr.loadGraph(webIdFile.getPath)
    val shapes = Shapes.parse(shapesGraph)
    val report = ShaclValidator.get.validate(shapes, dataGraph)

    val out = new ByteArrayOutputStream()
    RDFDataMgr.write(out, report.getModel, Lang.TTL)

    if(report.conforms()) None
    else Option(out.toString)
  }
}
