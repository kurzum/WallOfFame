package org.dbpedia.walloffame

;

import java.io.{IOException, PrintWriter}
import java.util.Collection

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.aksw.rdfunit.RDFUnitConfiguration
import org.aksw.rdfunit.exceptions.TestCaseExecutionException
import org.aksw.rdfunit.model.interfaces.results.TestExecution
import org.aksw.rdfunit.model.interfaces.{TestGenerator, TestSuite}
import org.aksw.rdfunit.sources.TestSource
import org.aksw.rdfunit.validate.ParameterException
import org.aksw.rdfunit.validate.utils.ValidateUtils
import org.aksw.rdfunit.validate.wrappers.{RDFUnitStaticValidator, RDFUnitTestSuiteGenerator}
import org.aksw.rdfunit.validate.ws.{AbstractRDFUnitWebService, ValidateWS}
import org.apache.commons.cli.{CommandLine, HelpFormatter, ParseException}
import org.slf4j.{Logger, LoggerFactory};

/**
 * Validation as a web service
 *
 * @author Dimitris Kontokostas
 * @since 6/13/14 1:50 PM
 */
class ShaclWS extends AbstractRDFUnitWebService {
  final val LOGGER: Logger = LoggerFactory.getLogger(classOf[ValidateWS])

  // TODO: pass dataFolder in configuration initialization
  var autogenerators: Collection[TestGenerator] = _

  override def init() = {

    val testSuiteGenerator =
      new RDFUnitTestSuiteGenerator.Builder()
        .addLocalResource("none", "")
        .addSchemaURI("none", "https://raw.githubusercontent.com/kurzum/shacldemo/master/test-case1.ttl").build();
    RDFUnitStaticValidator.initWrapper(testSuiteGenerator)
  }

  override def destroy() = {
    // do nothing.
  }

  override protected def getTestSuite(configuration: RDFUnitConfiguration, dataset: TestSource): TestSuite = {
    this.synchronized {
      RDFUnitStaticValidator.getTestSuite()
    }
  }

  @throws(classOf[TestCaseExecutionException])
  override protected def validate(configuration: RDFUnitConfiguration, dataset: TestSource, testSuite: TestSuite): TestExecution = {
    RDFUnitStaticValidator.validate(configuration.getTestCaseExecutionType(), dataset, testSuite)

    /*
            final TestExecutor testExecutor = TestExecutorFactory.createTestExecutor(configuration.getTestCaseExecutionType());
            if (testExecutor == null) {
                throw new TestCaseExecutionException(null, "Cannot initialize test executor. Exiting");
            }
            final SimpleTestExecutorMonitor testExecutorMonitor = new SimpleTestExecutorMonitor();
            testExecutorMonitor.setExecutionType(configuration.getTestCaseExecutionType());
            testExecutor.addTestExecutorMonitor(testExecutorMonitor);

            // warning, caches intermediate results
            testExecutor.execute(dataset, testSuite);

            return testExecutorMonitor.getTestExecution();
      */
  }

  @throws(classOf[ParameterException])
  override protected def getConfiguration(httpServletRequest: HttpServletRequest): RDFUnitConfiguration = {
    val arguments = convertArgumentsToStringArray(httpServletRequest);

    var commandLine: CommandLine = null;
    try {
      commandLine = ValidateUtils.parseArguments(arguments);
    } catch {
      case parseException: ParseException =>
        val errorMEssage = "Error! Not valid parameter input";
        throw new ParameterException(errorMEssage, parseException);
    }

    // TODO: hack to print help message
    if (commandLine.hasOption("h")) {
      throw new ParameterException("");
    }

    var configuration: RDFUnitConfiguration = null;
    configuration = ValidateUtils.getConfigurationFromArguments(commandLine);

    if (configuration.getOutputFormats().size() != 1) {
      throw new ParameterException("Error! Multiple formats defined");
    }

    configuration
  }

  private def convertArgumentsToStringArray(httpServletRequest: HttpServletRequest): Array[String] = {
    //twice the size to split key value
    val args: Array[String] = new Array[String](httpServletRequest.getParameterMap().size() * 2)

    var x = 0;
    val it = httpServletRequest.getParameterMap().keySet().iterator()

    while (it.hasNext) {
      val key = it.next()
      var pname = key.toString
      //transform key to CLI style
      //            pname = (pname.length() == 1) ? "-" + pname : "--" + pname;

      //collect CLI args
      x += 1
      args(x) = pname;
      x += 1
      args(x) = httpServletRequest.getParameter(key.toString);
    }

    return args;
  }

  @throws(classOf[IOException])
  override protected def printHelpMessage(httpServletResponse: HttpServletResponse): Unit = {
    httpServletResponse.setContentType("text/html");
    val printWriter: PrintWriter = httpServletResponse.getWriter();

    val formatter: HelpFormatter = new HelpFormatter();
    formatter.printHelp(printWriter, 200, "/validate?", "<pre>", ValidateUtils.getCliOptions(), 0, 0, "</pre>");
  }
}