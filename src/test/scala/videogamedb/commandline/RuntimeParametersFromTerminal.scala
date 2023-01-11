package videogamedb.commandline

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RuntimeParametersFromTerminal extends Simulation{

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  def USERCOUNT = System.getProperty("USERS", "5").toInt
  def RAMPDURATION = System.getProperty("RAMP_DURATION", "10").toInt
  def TESTDURATION = System.getProperty("TEST_DURATION", "30").toInt

  before{

   println(s"Running Test With ${USERCOUNT} users")
   println(s"Ramping Users over ${RAMPDURATION} seconds")
   println(s"Total Test Duration : ${TESTDURATION} seconds")

  }



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
    rampUsers(USERCOUNT).during(RAMPDURATION)
  )).protocols(httpProtocol)
    .maxDuration(TESTDURATION)








}
