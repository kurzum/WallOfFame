package org.dbpedia.walloffame.uniformTest

import better.files.File
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.dbpedia.walloffame.crawling.WebIdCrawler
import org.dbpedia.walloffame.uniform.WebIdUniformer
import org.dbpedia.walloffame.validation.WebIdValidator
import org.junit.Test

class UniformTests {

//  @Test
//  def shouldDownloadAndFilterWebIds {
//    WebIdUniformer.uniformWebIds(WebIdCrawler.crawl())
//
//    //    assertThat(viewCustomerPage.text, containsString("Adrian"))
//  }
//
//  @Test
//  def shouldConstructCorrect: Unit = {
//    WebIdUniformer.uniform(File("./src/test/resources/correctWebId.ttl"))
//
//    val stmts = WebIdUniformer.constructModel.listStatements()
//
//    while (stmts.hasNext) println(stmts.nextStatement())
//  }
//
//  @Test
//  def shouldPrintOutCorrect:Unit ={
//    WebIdUniformer.uniform(File("./src/test/resources/webid8.ttl"))
//
//    RDFDataMgr.write(System.out, WebIdUniformer.constructModel, Lang.TTL)
//  }
}
