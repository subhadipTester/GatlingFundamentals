package videogamedb.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class AddPauseTime extends Simulation{

  // 1.Http Configuration
  val httpProtocol = http.baseUrl("https://www.videogamedb.uk:443/api/v2/")
    .acceptHeader("application/json");

  // Scenario Definition

  val scenario_pause  = scenario("VideoGame DB Multiple GET calls")
    .exec(http("Get All VideoGames-->1st Call").get("videogame")).pause(5)
    .exec(http("Get Specific Video game Second call").get("videogame/3")).pause(1,5)
    .exec(http("Get All VideoGames-->2nd Call ").get("videogame")).pause(3000.milliseconds)

  //3. Load Scenario Design
  setUp(scenario_pause.inject(atOnceUsers(2))).protocols(httpProtocol)

}
