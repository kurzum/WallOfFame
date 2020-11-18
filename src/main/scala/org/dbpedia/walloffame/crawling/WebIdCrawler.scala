package org.dbpedia.walloffame.crawling

import better.files.File

object WebIdCrawler {

  val crawlScript = "crawl/crawl.sh"

  def crawl(): File = {
    //    var classLoader = Thread.currentThread.getContextClassLoader
    //    if (classLoader == null) classLoader = classOf[Class[_]].getClassLoader
    //
    //    val crawlFileStream = classLoader.getResourceAsStream("crawl/crawl.sh")
    //    val crawlStr = scala.io.Source.fromInputStream(crawlFileStream).mkString

    import sys.process._
    val result: String = crawlScript !!

    File(result.trim)
  }
}
