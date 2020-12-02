package org.dbpedia.walloffame.crawling

import java.io.IOException

import better.files.File

object WebIdCrawler {

  def crawl(): File = {
    val crawlStream = getClass.getClassLoader.getResourceAsStream("crawl.sh")
    val in = scala.io.Source.fromInputStream(crawlStream)

    val crawlFile = File("./tmp/crawl.sh")
    crawlFile.parent.createDirectoryIfNotExists()

    val out = new java.io.PrintWriter(crawlFile.toJava)
    try {
      in.getLines().foreach(out.println(_))
    }
    finally {
      out.close
    }

    var result =""

    try{
      import sys.process._
      Seq("chmod", "+x", crawlFile.pathAsString).!!
      result = Seq(crawlFile.pathAsString).!!

//      crawlFile.delete()
    } catch {
      case io:IOException => println(s"${crawlFile.pathAsString} not found")
    }

    File(result.trim)
  }
}
