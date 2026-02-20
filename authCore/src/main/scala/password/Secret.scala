package com.irka.authCore
package password

import errors.SecretNotConfigured

import scala.util.Try

trait Secret:
  val salt: String
  val iterations: Int
