package org.dbpedia.walloffame

import java.io.FileOutputStream

import better.files.File
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.dbpedia.walloffame.crawling.WebIdCrawler
import org.dbpedia.walloffame.uniform.WebIdUniformer
import org.dbpedia.walloffame.virtuoso.VirtuosoHandler

object Main {

  def main(args: Array[String]): Unit = {
    val uniformedModel = WebIdUniformer.uniformWebIds(WebIdCrawler.crawl())
//    VirtuosoHandler.insertModel(uniformedModel)
  }

}
