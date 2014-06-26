package com.scalator.model

import com.scalator.model.CredentialType.CredentialType
import java.util.Date

case class Credential(login: String, token: String, credentialType: CredentialType, validUntil: Long = new Date().getTime + 1209600)
