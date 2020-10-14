package org.dbpedia.walloffame.crawling

import better.files.File

object WebIdCrawler {

  def crawl():File ={
    import sys.process._
    val result:String = "./crawl/crawl.sh" !!

    File(result.trim)
  }
}
