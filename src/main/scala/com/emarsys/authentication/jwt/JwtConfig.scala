package com.emarsys.authentication.jwt

import java.time.Duration

import com.typesafe.config.Config

class JwtConfig(config: Config) {
  val expirationTime: Duration = config.getDuration("expiration-time")
  val secret: String = config.getString("secret")
}
