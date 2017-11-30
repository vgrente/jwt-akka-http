package com.emarsys.authentication.jwt

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._
import pdi.jwt.{Jwt, JwtAlgorithm}

trait JwtAuthentication {
  private final val tokenPrefix = "Bearer "

  private def isValid(jwt: String, secret: String) = {
    Jwt.isValid(jwt, secret, Seq(JwtAlgorithm.HS256))
  }

  def jwtAuthenticate(secret: String): Directive1[String] = optionalHeaderValueByName("Authorization")
    .map(stripBearerPrefix)
    .flatMap {
      case Some(jwt) if isValid(jwt, secret) =>
        provide(decodeToken(jwt, secret))
      case _ => complete(StatusCodes.Unauthorized)
    }

  private def stripBearerPrefix(token: Option[String]): Option[String] = {
    token.map(_.stripPrefix(tokenPrefix))
  }

  private def decodeToken(jwt: String, secret: String): String = Jwt.decode(jwt, secret, Seq(JwtAlgorithm.HS256)).get
}
