package videogamedb.commandline
import io.gatling.core.Predef._
import io.gatling.http.Predef._
class RuntimeParameters extends Simulation{

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  def getAllVideoGame () = {

    exec(http("Get All Video Games from Endpoint")
      .get("/videogame")
      .check(status.is(200))
    ).pause(5)


  }

  val scenario_runtime = scenario("Run From Commandline")
    .forever{
      exec(getAllVideoGame())


    }



  setUp(scenario_runtime.inject(
    nothingFor(10),
    rampUsers(10).during(20)
  )).protocols(httpProtocol)
    .maxDuration(20)








}
