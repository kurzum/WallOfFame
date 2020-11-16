package org.dbpedia.walloffame.virtuoso

import java.io.{InputStream, InputStreamReader}

import better.files.File
import org.apache.jena.rdf.model.Model
import org.apache.jena.util.FileManager
import virtuoso.jena.driver.{VirtGraph, VirtModel}

object VirtuosoHandler {

  val graphName ="http://webids"
  val url = "jdbc:virtuoso://localhost:1111/charset=UTF-8/log_enable=2"
  val user = "dba"
  val password = "dba"

  def insertFile(file:File)={
    try {
      val model: Model = VirtModel.openDatabaseModel(this.graphName, this.url, this.user, this.password)
      val in: InputStream = FileManager.get().open(file.pathAsString)
      if (in == null) {
        throw new IllegalArgumentException("File: " + file + " not found")
      }
      model.read(new InputStreamReader(in), null, "TURTLE")
      model.close()
    } catch {
      case e:Exception => System.out.println("Ex="+e)
    }
  }

  def insertModel(model:Model)={
      val virtmodel: VirtModel = VirtModel.openDatabaseModel(this.graphName, this.url, this.user, this.password)

      virtmodel.add(model)
      virtmodel.close()
  }

  def clearGraph()={
    val set = new VirtGraph(this.graphName, this.url, this.user, this.password)
    set.clear()
  }

  def getModel():Model={
    val model: Model = VirtModel.openDatabaseModel(this.graphName, this.url, this.user, this.password)
    model
  }




}
