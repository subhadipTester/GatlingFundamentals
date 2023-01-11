package videogamedb.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CheckResponseBodyAndExtract extends Simulation{
  // 1.Http Configuration
  val httpProtocol = http.baseUrl("https://www.videogamedb.uk:443/api/v2/")
    .acceptHeader("application/json");

  // Scenario Definition

  val responseChecker = scenario("Response check with JsonPath")
    .exec(http("Get Specific VideoGame")
      .get("videogame/4")
      .check(jsonPath("$.name")
        .is("Super Mario 64")))
    .exec(http("Get All VideoGames")
      .get("videogame")
      .check(jsonPath("$[1].id")
        .saveAs("gameId")))
    .exec { session =>
      println(session);
      session
    }
    .exec(http("Get Specific VideoGame")
      .get("videogame/#{gameId}")
      .check(jsonPath("$.name")
        .is("Gran Turismo 3"))
      .check(bodyString.saveAs("responseBody")))

    .exec{session=>println(session("responseBody").as[String]);session}



  //3. Load Scenario Design
  setUp(responseChecker.inject(atOnceUsers(1))).protocols(httpProtocol)








}
