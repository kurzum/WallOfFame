package org.dbpedia.walloffame.crawling

import better.files.File

object WebIdCrawler {

  def crawl(): File = {
    val classLoader = getClass.getClassLoader

    val crawlSh = classLoader.getResource("/crawl.sh").getFile

    import sys.process._
    val result: String = crawlSh.!!

    File(result.trim)
  }
}
