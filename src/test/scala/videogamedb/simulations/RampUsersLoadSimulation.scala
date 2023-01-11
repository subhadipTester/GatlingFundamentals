package videogamedb.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RampUsersLoadSimulation extends Simulation{


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


  val scenario_rampusersloadsimulation = scenario("Ramp Users Load Simulation")
    .exec(getAllVideoGame())
    .pause(5)
    .exec(getSpecificVideoGame())
    .pause(5)
    .exec(getAllVideoGame())

  setUp(scenario_rampusersloadsimulation.inject(

    nothingFor(10),
    constantUsersPerSec(10).during(10),
    rampUsersPerSec(1).to(5).during(20)
  ).protocols(httpProtocol)



  )



}
