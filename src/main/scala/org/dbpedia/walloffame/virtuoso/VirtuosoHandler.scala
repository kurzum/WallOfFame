package org.dbpedia.walloffame.virtuoso

import java.io.{InputStream, InputStreamReader}

import better.files.File
import org.apache.jena.rdf.model.Model
import org.apache.jena.util.FileManager
import org.dbpedia.walloffame.VosConfig
import virtuoso.jena.driver.{VirtGraph, VirtModel}

object VirtuosoHandler {
//  val toLoadDir = File("./docker/toLoad")

  def insertFile(file: File,vosConfig: VosConfig) = {
    try {
      val model: Model = VirtModel.openDatabaseModel(vosConfig.graph, vosConfig.url, vosConfig.usr, vosConfig.psw)
      val in: InputStream = FileManager.get().open(file.pathAsString)
      if (in == null) {
        throw new IllegalArgumentException("File: " + file + " not found")
      }
      model.read(new InputStreamReader(in), null, "TURTLE")
      model.close()
    } catch {
      case e: Exception => System.out.println("Ex=" + e)
    }
  }

  def insertModel(model: Model, vosConfig: VosConfig) = {
    val virtmodel: VirtModel = VirtModel.openDatabaseModel(vosConfig.graph, vosConfig.url, vosConfig.usr, vosConfig.psw)
    virtmodel.add(model)
    virtmodel.close()
  }

  def clearGraph(vosConfig: VosConfig) = {
    val set = new VirtGraph(vosConfig.graph, vosConfig.url, vosConfig.usr, vosConfig.psw)
    set.clear()
  }

  def getModel(vosConfig: VosConfig): Model = {
    val model: Model = VirtModel.openDatabaseModel(vosConfig.graph, vosConfig.url, vosConfig.usr, vosConfig.psw)
    model
  }

//  def moveFileToLoadDir(file: File): Unit = {
//    file.moveToDirectory(toLoadDir)
//  }


}
