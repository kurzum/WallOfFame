package org.dbpedia.walloffame.queries.webid

import org.apache.jena.rdf.model.Resource

object SelectQueries {


  def getQueryWebId(): String = {
    s"""
       |SELECT ?webid ?maker ?name ?keyvalue {
       |  ?webid a <http://xmlns.com/foaf/0.1/PersonalProfileDocument> ;
       |        <http://xmlns.com/foaf/0.1/maker> ?maker .
       |  ?maker <http://xmlns.com/foaf/0.1/name> ?name ;
       |         <http://www.w3.org/ns/auth/cert#key> ?key .
       |  ?key <http://www.w3.org/ns/auth/cert#modulus> ?keyvalue .
       |}
    """.stripMargin

  }

  def getWebIdURL() = {
    """
      |SELECT ?webid {
      |   ?webid a <http://xmlns.com/foaf/0.1/PersonalProfileDocument> .
      |}
      |""".stripMargin
  }

  def getMakerURL(webId: Resource): String = {
    s"""
       |SELECT ?maker {
       |  <${webId.toString}> <http://xmlns.com/foaf/0.1/maker> ?maker .
       |}
       |""".stripMargin
  }

  def getMakerName(makerURL: Resource): String = {
    s"""
       |SELECT ?name {
       |  <$makerURL> <http://xmlns.com/foaf/0.1/name> ?name ;
       |}
       |""".stripMargin
  }

}
