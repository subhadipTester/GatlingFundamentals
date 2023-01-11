package videogamedb.scriptfundamentals
import io.gatling.core.Predef._
import io.gatling.http.Predef._


class Authentication extends Simulation{

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  def authenticate() = {
    exec(http("Bearer Token Authentication")
      .post("/authenticate")
      .body(StringBody("{\n  \"password\": \"admin\",\n  \"username\": \"admin\"\n}"))
      .check(jsonPath("$.token").saveAs("jwtToken")))
  }

  def createNewGame() = {
    exec(http("Create new game")
      .post("/videogame")
      .header("Authorization", "Bearer #{jwtToken}")
      .body(StringBody(
        "{\n  \"category\": \"Platform\",\n  \"name\": \"Code Of Duty-Modern Warfare\",\n  \"rating\": \"Mature\",\n  \"releaseDate\": \"2012-05-04\",\n  \"reviewScore\": 85\n}"
      )))
  }

  val scn = scenario("Authentication Scenario")
    .exec(authenticate())
    .exec(createNewGame())

  setUp(
    scn.inject(atOnceUsers(2))
  ).protocols(httpProtocol)













}
