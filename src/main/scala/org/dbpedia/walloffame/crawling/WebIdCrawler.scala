package org.dbpedia.walloffame.crawling

import better.files.File

object WebIdCrawler {

  def crawl():File ={
    var classLoader = Thread.currentThread.getContextClassLoader

    if (classLoader == null) classLoader = classOf[Class[_]].getClassLoader

    val crawlFile = classLoader.getResource("crawl/crawl.sh").getFile
    println(crawlFile)
    import sys.process._
    val result:String = s"${crawlFile.toString}" !!

    File(result.trim)
  }
}
