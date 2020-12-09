package org.dbpedia.walloffame.crawling

import java.io.{ByteArrayOutputStream, FileOutputStream, IOException}
import better.files.File
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.rdfxml.xmlinput.NTriple
import org.apache.jena.riot.{Lang, RDFDataMgr, RiotException, RiotNotFoundException}
import org.slf4j.LoggerFactory

object WebIdCrawler {

  var accounts = Seq.empty[Seq[String]]

  def crawl():File ={
    val crawlDir = File("./tmp/crawl/")
    crawlDir.createDirectoryIfNotExists()

    val url = "https://databus.dbpedia.org/system/api/accounts"
    val model = ModelFactory.createDefaultModel()
    model.read(url, "N-TRIPLE")

    val stmts = model.listStatements()
    while(stmts.hasNext) {
      val stmt=stmts.nextStatement()
      val ob =stmt.getObject.toString.replaceFirst("https://databus.dbpedia.org/","")
      accounts=accounts:+Seq(stmt.getSubject.toString,ob)
    }

    accounts.foreach(account => {
      try{
        val accountModel = ModelFactory.createDefaultModel()
        accountModel.read(account.head, "TURTLE")

        val outFile= crawlDir/ s"${account(1)}.nt"
        RDFDataMgr.write(new FileOutputStream(outFile.toJava), accountModel, Lang.NTRIPLES)
      } catch {
        case riotNotFoundException: RiotNotFoundException => LoggerFactory.getLogger("Crawler").error(s"url ${account.head} not found.")
        case riotException: RiotException => LoggerFactory.getLogger("Crawler").error(s"riotException in ${account.head}.")
      }

    })

    crawlDir
  }


//  def crawl(): File = {
//    val crawlStream = getClass.getClassLoader.getResourceAsStream("crawl.sh")
//    val in = scala.io.Source.fromInputStream(crawlStream)
//
//    val crawlFile = File("./tmp/crawl.sh")
//    crawlFile.parent.createDirectoryIfNotExists()
//
//    val out = new java.io.PrintWriter(crawlFile.toJava)
//    try {
//      in.getLines().foreach(out.println(_))
//    }
//    finally {
//      out.close
//    }
//
//    var result =""
//
//    try{
//      import sys.process._
//      Seq("chmod", "+x", crawlFile.pathAsString).!!
//      result = Seq(crawlFile.pathAsString).!!
//
////      crawlFile.delete()
//    } catch {
//      case io:IOException => println(s"${crawlFile.pathAsString} not found")
//    }
//
//    File(result.trim)
//  }
}
