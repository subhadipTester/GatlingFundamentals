package videogamedb.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicLoadSimulation extends Simulation{

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")


  def getAllVideoGame () = {

    exec(http("Get All Video Games from Endpoint")
      .get("/videogame")
      .check(status.is(200)))


  }


  def getSpecificVideoGame () ={

    exec(http("Get Specific VideoGame from Endpoint")

      .get("/videogame/4")

    )


  }


  val scenario_basicloadsimulation = scenario("Basic Load Simulation")
    .exec(getAllVideoGame())
    .pause(5)
    .exec(getSpecificVideoGame())
    .pause(5)
    .exec(getAllVideoGame())


  setUp(scenario_basicloadsimulation.inject(
    nothingFor(5),
    atOnceUsers(5),
    rampUsers(10).during(10)
  ).protocols(httpProtocol)

  )







}
