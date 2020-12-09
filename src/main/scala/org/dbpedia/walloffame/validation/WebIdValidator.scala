package org.dbpedia.walloffame.validation

import better.files.File
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.apache.jena.shacl.{ShaclValidator, Shapes}

import java.io.{ByteArrayOutputStream, File => JavaFile}


object WebIdValidator {


  def validateWithShacl(webIdFile: File): String = {
    import org.springframework.core.io.support.PathMatchingResourcePatternResolver
    val resolver = new PathMatchingResourcePatternResolver
    val resources = resolver.getResources("classpath:shacl/*.ttl")


    var result = ""
    val tmpShapeFile = File("./tmp/tmpShapeFile.ttl").toJava
    for (resource <- resources) {
      val is = resource.getInputStream
      val in = scala.io.Source.fromInputStream(is)
      val out = new java.io.PrintWriter(tmpShapeFile)
      try {
        in.getLines().foreach(out.println(_))
      }
      finally {
        out.close
      }

      val partResult = validate(webIdFile.toJava, tmpShapeFile).getOrElse("")
      if (!(partResult == "")) result = result.concat(s"$partResult\n")
      tmpShapeFile.delete()
    }

//    println(getClass.getClassLoader.getResource("shacl"))
//    val shapDir = File(getClass.getClassLoader.getResource("shacl").getFile)
//
//    shapDir.children.foreach(println(_))
//    var result = ""
//
//
//
//    val listedShapeFiles = shapDir.toJava.listFiles(new FilenameFilter {
//      override def accept(file: JavaFile, name: String): Boolean = {
//        name.matches(".*.ttl")
//      }
//    })

//    listedShapeFiles.foreach(
//      shapesFile => {
//        val partResult = validate(webIdFile.toJava, shapesFile).getOrElse("")
//        if (!(partResult == "")) result = result.concat(s"$partResult\n")
//      })

    result

//    validate(webIdFile.toJava, File("").toJava).getOrElse("")
  }

  def validate(webIdFile: JavaFile, shapesFile: JavaFile): Option[String] = {

    val shapesGraph = RDFDataMgr.loadGraph(shapesFile.getAbsolutePath)
    val dataGraph = RDFDataMgr.loadGraph(webIdFile.getAbsolutePath)
    val shapes = Shapes.parse(shapesGraph)
    val report = ShaclValidator.get.validate(shapes, dataGraph)

    val out = new ByteArrayOutputStream()
    RDFDataMgr.write(out, report.getModel, Lang.TTL)
    if (report.conforms()) None
    else Option(out.toString)
  }
}
