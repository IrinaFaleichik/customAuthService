package com.irka.authCore
package password

import java.security.MessageDigest
import java.util.Base64
import scala.annotation.tailrec

trait HashingUtils:
  val secret: Secret

  @tailrec
  private def hashing(plainPassword: String, currentIteration: Int): String =
    // Simple SHA-256
    val bytes = MessageDigest.getInstance("SHA-256").digest(plainPassword.getBytes("UTF-8"))
    val hash = Base64.getEncoder.encodeToString(bytes)
    if (currentIteration == secret.iterations) hash
    else hashing(hash, currentIteration + 1)

  def fromPlainText(plainPassword: String): HashedPassword =
    HashedPassword.create(hashing(plainPassword + secret.salt, 0))
