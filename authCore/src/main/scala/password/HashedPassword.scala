package com.irka.authCore
package password

case class HashedPassword private(private[password] val hash: String):
  override def toString: String = "Password(***)"

  def hashUnsafe: String = hash

  def verify(hashedPassword: HashedPassword): Boolean =
    this.hash == hashedPassword.hash

  def verifyHashed(hashedPassword: String): Boolean =
    hashedPassword == this.hash

object HashedPassword:
  def create(hash: String): HashedPassword = HashedPassword(hash)
