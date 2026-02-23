package com.irka.authService
package api.codecs

import com.irka.authCore.identity.{EmailIdentity, UsernameIdentity}
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

package object identity:

  implicit val emailEncoder: JsonEncoder[EmailIdentity] = DeriveJsonEncoder.gen[EmailIdentity]
  implicit val emailDecoder: JsonDecoder[EmailIdentity] =
    DeriveJsonDecoder.gen[EmailIdentity].mapOrFail(_.validate.left.map(_.getMessage))

  implicit val usernameEncoder: JsonEncoder[UsernameIdentity] = DeriveJsonEncoder.gen[UsernameIdentity]
  implicit val usernameDecoder: JsonDecoder[UsernameIdentity] =
    DeriveJsonDecoder.gen[UsernameIdentity].mapOrFail(_.validate.left.map(_.getMessage))
