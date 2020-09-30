package org.dbpedia.walloffame.queries.webid

import org.apache.jena.rdf.model.Resource

object ConstructQueries {
  def constructWebIdURL(): String = {
    """
      |PREFIX foaf: <http://xmlns.com/foaf/0.1/>
      |
      |CONSTRUCT   { ?webid a foaf:PersonalProfileDocument }
      |WHERE       { ?webid a foaf:PersonalProfileDocument }
      |""".stripMargin
  }

  def constructMakerURL(webId: Resource): String = {
    s"""
       |PREFIX foaf: <http://xmlns.com/foaf/0.1/>
       |
       |CONSTRUCT   { <${webId.toString}> foaf:maker ?maker }
       |WHERE       { <${webId.toString}> foaf:maker ?maker }
       |""".stripMargin
  }

  def constructMakerName(makerURL: Resource): String = {
    s"""
       |PREFIX foaf: <http://xmlns.com/foaf/0.1/>
       |
       |CONSTRUCT   { <${makerURL.toString}> foaf:name ?makerName }
       |WHERE       { <${makerURL.toString}> foaf:name ?makerName }
       |""".stripMargin
  }

  def constructCertValue(makerURL: Resource): String = {
    s"""
       |PREFIX foaf: <http://xmlns.com/foaf/0.1/>
       |
       |CONSTRUCT   { <${makerURL.toString}> <http://www.w3.org/ns/auth/cert#modulus> ?keyvalue }
       |WHERE       {
       |  <${makerURL.toString}> <http://www.w3.org/ns/auth/cert#key> ?key .
       |  ?key <http://www.w3.org/ns/auth/cert#modulus> ?keyvalue .
       |}
       |""".stripMargin
  }


}
