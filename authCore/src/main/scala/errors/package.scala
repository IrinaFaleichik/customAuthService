package com.irka.authCore

package object errors:
  
  class AuthError(err: String) extends Exception(s"Authentication error: $err")

  class SecretNotConfigured extends AuthError("Secret is not configured")


