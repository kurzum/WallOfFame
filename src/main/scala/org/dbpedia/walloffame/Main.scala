package org.dbpedia.walloffame

import java.io.FileOutputStream

import better.files.File
import org.apache.jena.riot.{Lang, RDFDataMgr}
import org.dbpedia.walloffame.crawling.WebIdCrawler
import org.dbpedia.walloffame.uniform.WebIdUniformer

object Main {

  def main(args: Array[String]): Unit = {
    val uniformedModel = WebIdUniformer.uniformWebIds(WebIdCrawler.crawl())

    val out = new FileOutputStream(File(args.head).toJava)
    RDFDataMgr.write(out, uniformedModel, Lang.TTL)
    out.toString
  }

}
