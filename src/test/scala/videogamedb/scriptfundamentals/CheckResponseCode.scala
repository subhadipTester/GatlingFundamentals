package videogamedb.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class CheckResponseCode extends Simulation{


  // 1.Http Configuration
  val httpProtocol = http.baseUrl("https://www.videogamedb.uk:443/api/v2/")
    .acceptHeader("application/json");

  // Scenario Definition

  val scenario_responsecode  = scenario("Response Status Check")
    .exec(http("Get All VideoGames-->1st Call").get("videogame").check(status.is(200))).pause(5)
    .exec(http("Get Specific Video game--> 1st Call").get("videogame/3").check(status.in(200 to 210))).pause(1,5)
    .exec(http("Get All VideoGames-->2nd Call ").get("videogame").check(status.not(404),status.not(500))).pause(3000.milliseconds)

  //3. Load Scenario Design
  setUp(scenario_responsecode.inject(atOnceUsers(2))).protocols(httpProtocol)







}
