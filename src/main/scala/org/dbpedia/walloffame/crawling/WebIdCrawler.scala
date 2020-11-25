package org.dbpedia.walloffame.crawling

import better.files.File
import org.dbpedia.walloffame.validation.WebIdValidator.validate

object WebIdCrawler {

  def crawl(): File = {
    val crawlStream = getClass.getClassLoader.getResourceAsStream("/crawl.sh")
    val in = scala.io.Source.fromInputStream(crawlStream)

    val crawlFile = File("crawl.sh").toJava

    val out = new java.io.PrintWriter(crawlFile)
    try {
      in.getLines().foreach(out.println(_))
      import sys.process._
      val result: String = crawlFile.getAbsolutePath.!!

      File(result.trim)
    }
    finally {
      out.close
      crawlFile.delete()
    }
  }
}
