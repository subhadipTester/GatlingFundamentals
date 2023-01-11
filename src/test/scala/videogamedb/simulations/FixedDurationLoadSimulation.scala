package videogamedb.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FixedDurationLoadSimulation extends Simulation{


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


  val scenario_fixedDuration = scenario("Fixed Duration Load Simulation")
    .forever {
      exec(getAllVideoGame())
        .pause(5)
        .exec(getSpecificVideoGame())
        .pause(5)
        .exec(getAllVideoGame())
    }

  setUp(scenario_fixedDuration.inject(

    nothingFor(10),
    atOnceUsers(10),
    rampUsers(20).during(30))
    .protocols(httpProtocol)).maxDuration(60)




}
