package org.dbpedia.walloffame.spring.model

import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.apache.jena.riot.RDFDataMgr
import org.dbpedia.walloffame.uniform.QueryHandler
import org.dbpedia.walloffame.uniform.queries.{SelectOptionalQueries, SelectQueries}
import org.dbpedia.walloffame.utils.Util
import org.dbpedia.walloffame.validation.WebIdValidator

import javax.validation.constraints.{Min, Pattern}
import java.net.URL
import scala.beans.BeanProperty
import scala.collection.mutable.ListBuffer

class WebId {

  @BeanProperty
  //  @Pattern(regexp = "^https://.*")
  var url: String = _

  @BeanProperty
  var turtle: String = _

  @BeanProperty
  var geekCode: String = _

  @BeanProperty
  var name: String = _

  @BeanProperty
  var firstName: String = _

  @BeanProperty
  var img: String = _

  @BeanProperty
  var maker: String = _

  @BeanProperty
  var gender: String = _

  def this(model: Model) {
    this()
    fetchFieldsWithModel(model)
  }

  def fetchFieldsWithModel(model: Model): Unit = {
    val mandatory = QueryHandler.executeQuery(SelectQueries.getQueryWebIdData(), model).head

    this.name = mandatory.getLiteral("?name").getLexicalForm
    this.maker = mandatory.getResource("?maker").toString


    var optional = QueryHandler.executeQuery(SelectOptionalQueries.queryFirstName(), model)
    if (optional.nonEmpty) this.firstName = optional.head.getLiteral("?firstname").getLexicalForm

    optional = QueryHandler.executeQuery(SelectOptionalQueries.queryGeekCode(), model)
    if (optional.nonEmpty) this.geekCode = optional.head.getLiteral("?geekcode").getLexicalForm

    optional = QueryHandler.executeQuery(SelectOptionalQueries.queryImg(), model)
    if (optional.nonEmpty) this.img = optional.head.getResource("?img").toString

    optional = QueryHandler.executeQuery(SelectOptionalQueries.queryGender(), model)
    if (optional.nonEmpty) this.gender = optional.head.getLiteral("?gender").getLexicalForm
  }

}
