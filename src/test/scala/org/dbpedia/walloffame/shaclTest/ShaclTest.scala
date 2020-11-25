package org.dbpedia.walloffame.shaclTest

import java.io.ByteArrayOutputStream

import better.files.File
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.apache.jena.shacl.lib.ShLib
import org.apache.jena.shacl.{ShaclValidator, Shapes}
import org.dbpedia.walloffame.validation.WebIdValidator
import org.junit.jupiter.api.Test

class ShaclTest {

  val shapeFile = File("./src/main/resources/shacl/shapes.ttl")
  val testResourceDir = File("./src/test/resources/")

  @Test
  def shouldPrintOutCorrectOutput {
    val webIdFile = testResourceDir / "wrongWebId.ttl"
    println(WebIdValidator.validate(webIdFile.toJava, shapeFile.toJava))
  }

  @Test
  def shouldSuccess {
    val webIdFile = testResourceDir / "correctWebId.ttl"
    println(WebIdValidator.validateWithShacl(webIdFile))
  }

  @Test
  def shaclShouldSuccess: Unit = {
    val webIdFile = testResourceDir / "correctWebId.ttl"
    validate(webIdFile,shapeFile)
  }

  @Test
  def shaclShouldFail: Unit = {
    val webIdFile = testResourceDir/ "wrongWebId.ttl"
    validate(webIdFile,shapeFile)
  }

  def validate(webIdFile: File, shapesFile: File): Boolean = {

    val stmts = RDFDataMgr.loadModel(shapesFile.pathAsString).listStatements()
    while (stmts.hasNext) println(stmts.nextStatement())


    val shapesGraph = RDFDataMgr.loadGraph(shapesFile.pathAsString)
    val dataGraph = RDFDataMgr.loadGraph(webIdFile.pathAsString)
    val shapes = Shapes.parse(shapesGraph)

    println("SHAPE is empty?")
    println(shapes.isEmpty)
    val it = shapes.iterator()
    while (it.hasNext) println(it.next())


    val report = ShaclValidator.get.validate(shapes,dataGraph)







    println("REPORT")
    ShLib.printReport(report)
    System.out.println()
    val out = new ByteArrayOutputStream()
    RDFDataMgr.write(System.out, report.getModel, Lang.TTL)
    out.toString
    ShLib.printReport(report)
    report.conforms()
  }


  @Test
  def correctFileShouldPass:Unit= {

    val webIdFile = testResourceDir /"correctWebId.ttl"
    println(WebIdValidator.validate(webIdFile.toJava,shapeFile.toJava))
  }

  @Test
  def wrongFileShouldNotPass:Unit= {
    val webIdFile = testResourceDir / "wrongWebId.ttl"
    println(WebIdValidator.validate(webIdFile.toJava,shapeFile.toJava))
  }

  @Test
  def stringShould:Unit ={
    val str = """
                |@prefix sh:    <http://www.w3.org/ns/shacl#> .
                |@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
                |@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
                |@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .
                |@prefix foaf:  <http://xmlns.com/foaf/0.1/> .
                |@prefix shsh:  <http://www.w3.org/ns/shacl-shacl#> .
                |
                |[ a            sh:ValidationReport ;
                |  sh:conforms  false ;
                |  sh:result    [ a                             sh:ValidationResult ;
                |                 sh:focusNode                  <https://akirsche.github.io/webid.ttl> ;
                |                 sh:resultMessage              "minCount[1]: Invalid cardinality: expected min 1: Got count = 0" ;
                |                 sh:resultPath                 foaf:maker ;
                |                 sh:resultSeverity             sh:Violation ;
                |                 sh:sourceConstraintComponent  sh:MinCountConstraintComponent ;
                |                 sh:sourceShape                []
                |               ]
                |] .
                |""".stripMargin
  }


  @Test
  def shapeShouldNotBeEmpty:Unit ={
    val shapes = Shapes.parse("/home/eisenbahnplatte/git/Eisenbahnplatte/WallOfFame/src/test/resources/New Folder/shape.ttl")

    assert(!shapes.isEmpty)
  }

}

