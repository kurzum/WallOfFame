package org.dbpedia.walloffame.shaclTest

import java.io.File

import org.dbpedia.walloffame.crawling.WebIdCrawler
import org.dbpedia.walloffame.validation.WebIdValidator
import org.junit.Test

class crawltest {
  @Test
  def shouldPrintOutCorrectOutput {
    println(WebIdCrawler.crawl())
    //    assertThat(viewCustomerPage.text, containsString("Adrian"))
  }
}
