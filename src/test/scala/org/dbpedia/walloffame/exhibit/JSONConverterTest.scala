import better.files.File
import org.apache.jena.riot.RDFDataMgr
import org.dbpedia.walloffame.convert.ModelToJSONConverter
import org.junit.jupiter.api.Test

class JSONConverterTest() {

  @Test
  def convert(): Unit = {
    val model = RDFDataMgr.loadModel("./src/test/resources/correctWebId.ttl")

//    val out = new ByteArrayOutputStream()
//    RDFDataMgr.write(out, model, Lang.JSONLD)
//    println( out.toString)
    ModelToJSONConverter.toJSON(model, File("./webid.js"))
  }
}

