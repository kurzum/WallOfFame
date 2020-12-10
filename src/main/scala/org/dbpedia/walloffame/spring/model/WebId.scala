package org.dbpedia.walloffame.spring.model

import org.apache.jena.rdf.model.Model
import org.dbpedia.walloffame.uniform.QueryHandler
import org.dbpedia.walloffame.uniform.queries.{SelectOptionalQueries, SelectQueries}

import scala.collection.mutable.ListBuffer

class WebId {

  var turtle: String = _
  var geekCode:String = _
  var name:String =_
  var firstName:String = _
  var img:String =_
  var maker:String =_
  var gender:String =_

  def setTurtle(value:String) ={
    this.turtle=value
  }

  def getTurtle():String={
    turtle
  }

  def setGeekCode(value:String) ={
    this.geekCode=value
  }

  def getGeekCode():String={
    geekCode
  }
  def setName(value:String) ={
    this.name=value
  }

  def getName():String={
    name
  }
  def setImg(value:String) ={
    this.img=value
  }

  def getImg():String={
    img
  }

  def setFirstName(value:String):Unit ={
    this.firstName= value
  }

  def getFirstName():String={
    firstName
  }

  def getMaker():String={
    maker
  }

  def setMaker(value:String):Unit={
    this.maker = value
  }

  def setGender(value:String)={
    this.gender = value
  }

  def getGender():String={
    gender
  }


  def insertFieldsFromTurtle(model:Model):Unit ={

    val stmts = model.listStatements()
    while(stmts.hasNext) println(stmts.nextStatement())
    val mandatory = QueryHandler.executeQuery(SelectQueries.getQueryWebIdData(), model).head

    setName(mandatory.getLiteral("?name").getLexicalForm)
    setMaker(mandatory.getResource("?maker").toString)


    var optional = QueryHandler.executeQuery(SelectOptionalQueries.queryFirstName(), model)
    if(optional.nonEmpty) setFirstName(optional.head.getLiteral("?firstname").getLexicalForm)

    optional = QueryHandler.executeQuery(SelectOptionalQueries.queryGeekCode(), model)
    if(optional.nonEmpty) setGeekCode(optional.head.getLiteral("?geekcode").getLexicalForm)

    optional = QueryHandler.executeQuery(SelectOptionalQueries.queryImg(), model)
    if(optional.nonEmpty) setImg(optional.head.getResource("?img").toString)

    optional = QueryHandler.executeQuery(SelectOptionalQueries.queryGender(), model)
    if(optional.nonEmpty) setGender(optional.head.getLiteral("?gender").getLexicalForm)
  }
}
