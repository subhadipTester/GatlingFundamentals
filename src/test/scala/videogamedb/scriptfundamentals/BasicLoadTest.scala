package videogamedb.scriptfundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._
class BasicLoadTest extends Simulation{

  // 1.Http Configuration
 val httpProtocol = http.baseUrl("https://www.videogamedb.uk:443/api/v2/")
                    .acceptHeader("application/json");

  // 2.Scenario Definition
  val scn = scenario("My First LoadTest Scenario").exec(http("Get All VideoGames").get("videogame"));


  //3. Load Scenario Design
  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol);








}
