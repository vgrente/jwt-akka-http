package com.emarsys.eventsegmentation.authentication

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.emarsys.authentication.jwt.JwtAuthentication
import org.scalatest.{Matchers, WordSpec}
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

class JwtAuthenticationSpec extends WordSpec with Matchers with JwtAuthentication with ScalatestRouteTest {
  val secret = "secret123"

  "JwtAuthentication directive" when {
    "using a valid jwt token" should {
      "return OK" in {
        val claim = defaultClaim.expiresIn(200)
        val validToken = encodeToken(claim, secret)
        Get("/") ~> withJwtToken(validToken) ~> jwtAuthenticate(secret)(
          _ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.OK
        }
      }
    }

    "using no jwt token" should {
      "return Unauthorized" in {
        Get("/") ~> jwtAuthenticate(secret)(
          _ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.Unauthorized
        }
      }
    }

    "using an invalid jwt token" should {
      "return Unauthorized" in {
        val invalidToken = "invalid token"
        Get("/") ~> withJwtToken(invalidToken) ~> jwtAuthenticate(secret)(
          _ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.Unauthorized
        }
      }
    }

    "using a jwt token with invalid secret" should {
      "return Unauthorized" in {
        val claim = defaultClaim.expiresIn(200)
        val invalidToken = encodeToken(claim, "invalid secret")
        Get("/") ~> withJwtToken(invalidToken) ~> jwtAuthenticate(secret)(
          _ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.Unauthorized
        }
      }
    }

    "using a jwt token that is not yet valid" should {
      "return Unauthorized" in {
        val claim = defaultClaim.startsIn(200).expiresIn(400)
        val invalidToken = encodeToken(claim, secret)
        Get("/") ~> withJwtToken(invalidToken) ~> jwtAuthenticate(secret)(
          _ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.Unauthorized
        }
      }
    }

    "using an expired jwt token" should {
      "return Unauthorized" in {
        val claim = defaultClaim.expiresNow
        val expiredToken = encodeToken(claim, secret)
        Get("/") ~> withJwtToken(expiredToken) ~> jwtAuthenticate(secret)(
          _ => complete(StatusCodes.OK)) ~> check {
          status shouldBe StatusCodes.Unauthorized
        }
      }
    }

    "using a valid jwt token" should {
      "pass the jwt claim data" in {
        import spray.json._
        val claim = defaultClaim.expiresIn(200)
        val validToken = encodeToken(claim, secret)
        Get("/") ~> withJwtToken(validToken) ~> jwtAuthenticate(secret)(
          claimData => complete(claimData)) ~> check {
          val resultClaimData = responseAs[String]
          resultClaimData.parseJson shouldEqual claim.toJson.parseJson
        }
      }
    }
  }

  private val claimJson = """{"name": "Slartibartfast", "id": 24}"""
  private val defaultClaim: JwtClaim = JwtClaim(claimJson)

  private def encodeToken(claim: JwtClaim, secret: String) =
    Jwt.encode(claim, secret, JwtAlgorithm.HS256)

  private def withJwtToken(jwtToken: String) = {
    addHeader(Authorization(OAuth2BearerToken(jwtToken)))
  }
}
