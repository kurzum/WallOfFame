
import org.dbpedia.walloffame.webservice.config.SpringConfig
import org.junit.jupiter.api.{BeforeEach, Test}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers._
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringJUnitWebConfig(Array(classOf[SpringConfig]))
class TestWelcome {

  private var mockMvc: MockMvc = _

  @Autowired
  private var webAppContext: WebApplicationContext = _

  @BeforeEach
  def setup(): Unit = {
    mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build()
  }

  @Test
  @throws(classOf[Exception])
  def testWelcome(): Unit = {

    this.mockMvc.perform(
      get("/"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(view().name("index"))
      .andExpect(forwardedUrl("/WEB-INF/views/index.jsp"))
      .andExpect(model().attribute("msg", "Hello World"));
  }

}
