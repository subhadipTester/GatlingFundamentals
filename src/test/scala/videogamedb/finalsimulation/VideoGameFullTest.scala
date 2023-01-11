package videogamedb.finalsimulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class VideoGameFullTest extends Simulation {

  // ** HTTP Configuration ** //

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  // ** Runtime Parameters ** //

  def USERCOUNT = System.getProperty("USERS", "5").toInt
  def RAMPDURATION = System.getProperty("RAMP_DURATION", "10").toInt
  def TESTDURATION = System.getProperty("TEST_DURATION", "30").toInt

  // ** VARIABLES FOR FEEDERS ** //

  val csvFeeder = csv("testdata/gameCsvFile.csv").random

  before{

    println(s"Running Performance Scaling Test With ${USERCOUNT} users")
    println(s"Ramping Virtual Users over ${RAMPDURATION} seconds")
    println(s"Total Performance Test Duration : ${TESTDURATION} seconds")

  }

  //** HTTP CALLS **//

  def getAllVideoGame () = {

    exec(http("Get All Video Games from Endpoint")
      .get("/videogame")
      .check(status.is(200))
    ).pause(5)


  }

  def authenticate() = {
    exec(http("Authenticate")
      .post("/authenticate")
      .body(StringBody("{\n  \"password\": \"admin\",\n  \"username\": \"admin\"\n}"))
      .check(jsonPath("$.token").saveAs("jwtToken")))
  }

  def createNewGame() = {

    feed(csvFeeder)
      .exec(http("Create New Game - #{name}")
        .post("/videogame")
        .header("Authorization", "Bearer #{jwtToken}")
        .body(ElFileBody("bodies/newGameTemplate.json")).asJson)

  }

  def getSpecificVideoGame() ={

    exec(http("Get Specific single game - #{name}")
      .get("/videogame/#{gameId}")
      .check(jsonPath("$.name").is("#{name}")))
  }


  def deleteGame() ={

    exec(http("Delete game - #{name}")
      .delete("/videogame/#{gameId}")
      .header("Authorization", "Bearer #{jwtToken}")
      .check(bodyString.is("Video game deleted")))
  }


  //** SCENARIO DESIGN *//

  val scenario_final = scenario("Video Game DB Final Script")
    .forever{

      exec(getAllVideoGame())
        .pause(5)
        .exec(authenticate())
        .pause(2)
        .exec(createNewGame())
        .pause(5)
        .exec(getSpecificVideoGame())
        .pause(5)
        .exec(deleteGame())

    }

  setUp(scenario_final.inject(
    nothingFor(10),
    rampUsers(USERCOUNT).during(RAMPDURATION)
  )).protocols(httpProtocol)
    .maxDuration(TESTDURATION)

  after{
   println("Stress Test is Completed for VideoGame DB")

  }









}
