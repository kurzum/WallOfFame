package org.dbpedia.walloffame.virtuoso

import java.io.{File, InputStream, InputStreamReader}

import org.apache.jena.rdf.model.Model
import org.apache.jena.util.FileManager
import virtuoso.jena.driver.{VirtGraph, VirtModel}

object VirtuosoHandler {

  val graphName ="http://webids"

  def insertFile(file:File)={
    try {
      val model: Model = VirtModel.openDatabaseModel(this.graphName, "jdbc:virtuoso://localhost:1111/charset=UTF-8/log_enable=2", "dba", "dba")
      val in: InputStream = FileManager.get().open(file.getAbsolutePath)
      if (in == null) {
        throw new IllegalArgumentException("File: " + file + " not found")
      }
      model.read(new InputStreamReader(in), null, "TURTLE")
      model.close()

      println("fertig")
    } catch {
      case e:Exception => System.out.println("Ex="+e)
    }
  }

  def clearGraph()={
    val set = new VirtGraph(this.graphName,"jdbc:virtuoso://localhost:1111", "dba", "dba")
    set.clear()
  }


}
