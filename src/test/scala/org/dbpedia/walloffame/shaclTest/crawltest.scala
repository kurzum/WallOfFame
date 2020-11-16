package org.dbpedia.walloffame.shaclTest

import org.dbpedia.walloffame.crawling.WebIdCrawler
import org.junit.Test

class crawltest {
  @Test
  def shouldPrintOutCorrectOutput {
    println(WebIdCrawler.crawl())
    //    assertThat(viewCustomerPage.text, containsString("Adrian"))
  }
}
