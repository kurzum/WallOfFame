package org.dbpedia.walloffame.uniformTest

import java.io.File

import org.dbpedia.walloffame.crawling.WebIdCrawler
import org.dbpedia.walloffame.uniform.WebIdUniformer
import org.dbpedia.walloffame.validation.WebIdValidator
import org.junit.Test

class UniformTests {

  @Test
  def shouldDownloadAndFilterWebIds {
    WebIdUniformer.uniformWebIds(WebIdCrawler.crawl())

    //    assertThat(viewCustomerPage.text, containsString("Adrian"))
  }
}
