package com.emarsys.authentication.jwt

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.typesafe.config.{Config, ConfigFactory}
import fommil.sjs.FamilyFormats._
import org.scalatest.{Matchers, WordSpec}
import pdi.jwt.{Jwt, JwtClaim}
import spray.json._

class JwtAuthenticationSpec extends WordSpec with Matchers with JwtAuthentication with ScalatestRouteTest with SprayJsonSupport {

  val config: Config = ConfigFactory.load()
  override val jwtConfig: JwtConfig = new JwtConfig(config.getConfig("jwt"))


  "JwtAuthentication directive" when {
    "using a valid jwt token" should {
      "return OK" in {
        val claim = defaultClaim.expiresIn(200)
        val validToken = encodeToken(claim, jwtConfig.secret)
        Get("/") ~> withJwtToken(validToken) ~> jwtAuthenticate(as[ClaimData]) (_ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.OK
        }
      }
    }

    "using no jwt token" should {
      "return Unauthorized" in {
        Get("/") ~> jwtAuthenticate(as[ClaimData])(_ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.Unauthorized
        }
      }
    }

    "using an invalid jwt token" should {
      "return Unauthorized" in {
        val invalidToken = "invalid token"
        Get("/") ~> withJwtToken(invalidToken) ~> jwtAuthenticate(as[ClaimData])(
          _ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.Unauthorized
        }
      }
    }

    "using a jwt token with invalid secret" should {
      "return Unauthorized" in {
        val claim = defaultClaim.expiresIn(200)
        val invalidToken = encodeToken(claim, "invalid secret")
        Get("/") ~> withJwtToken(invalidToken) ~> jwtAuthenticate(as[ClaimData])(
          _ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.Unauthorized
        }
      }
    }

    "using a jwt token that is not yet valid" should {
      "return Unauthorized" in {
        val claim = defaultClaim.startsIn(200).expiresIn(400)
        val invalidToken = encodeToken(claim, jwtConfig.secret)
        Get("/") ~> withJwtToken(invalidToken) ~> jwtAuthenticate(as[ClaimData])(
          _ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.Unauthorized
        }
      }
    }

    "using an expired jwt token" should {
      "return Unauthorized" in {
        val claim = defaultClaim.expiresNow
        val expiredToken = encodeToken(claim, jwtConfig.secret)
        Get("/") ~> withJwtToken(expiredToken) ~> jwtAuthenticate(as[ClaimData])(
          _ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.Unauthorized
        }
      }
    }

    "using a valid jwt token" should {
      "pass the jwt claim data" in {
        val claim = defaultClaim.expiresIn(200)
        val validToken = encodeToken(claim, jwtConfig.secret)
        Get("/") ~> withJwtToken(validToken) ~> jwtAuthenticate(as[ClaimData])(claimData => complete(claimData)) ~> check {
          val resultClaimData = responseAs[ClaimData]
          resultClaimData shouldEqual extractClaimData(claim)
        }
      }
    }
  }

  case class ClaimData(data: String)

  private val claimData = ClaimData("data")

  private val defaultClaim: JwtClaim = JwtClaim(claimData.toJson.toString)

  private def encodeToken(claim: JwtClaim, secret: String) =
    Jwt.encode(claim, secret, encodingAlgorithm)

  private def withJwtToken(jwtToken: String) =
    addHeader(Authorization(OAuth2BearerToken(jwtToken)))

  private def extractClaimData(jwtClaim: JwtClaim) =
    jwtClaim.toJson.parseJson.convertTo[ClaimData]
}
