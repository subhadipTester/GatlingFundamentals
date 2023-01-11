package videogamedb.feeders

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Random

class ComplexCustomFeeder extends Simulation{

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  var idNumbers = (1 to 100).iterator
  val rnd = new Random()
  def randomString(length: Int) = {
  rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }
  val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  val now = LocalDate.now()

  def getRandomDate(startDate: LocalDate, random: Random): String = {
    startDate.minusDays(random.nextInt(30)).format(pattern)

  }

  val customFeeder = Iterator.continually(Map(
    "gameId" -> idNumbers.next(),
    "name"   -> ("Age of Empire-"+ randomString(5)),
    "releaseDate"   -> getRandomDate(now,rnd),
    "reviewScore"   -> rnd.nextInt(100),
    "rating"   -> ("Mature-"+ randomString(6)),
    "category" -> ("Platform-"+ randomString(4))
  ))
  def authenticate() = {
    exec(http("Bearer Token Authentication")
      .post("/authenticate")
      .body(StringBody("{\n  \"password\": \"admin\",\n  \"username\": \"admin\"\n}"))
      .check(jsonPath("$.token").saveAs("jwtToken")))
  }

  def createNewVideoGame() ={

    repeat(10){

      feed(customFeeder)
        .exec(http("Create New Video Game- #{name}")
          .post("/videogame")
          .header("Authorization", "Bearer #{jwtToken}")
          .body(ElFileBody("bodies/newGameTemplate.json")).asJson
          .check(bodyString.saveAs("responseBody")))
        .exec{session => println(session("responseBody").as[String]);session}
        .pause(5)

    }
  }

  val scenario_customfeeder = scenario("Parameterization using Complex Custom Feeder")
    .exec(authenticate())
    .exec(createNewVideoGame())



  setUp(scenario_customfeeder.inject(atOnceUsers(1))).protocols(httpProtocol)




}
