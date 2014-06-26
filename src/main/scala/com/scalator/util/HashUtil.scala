package com.scalator.util

import java.security.MessageDigest
import org.apache.commons.codec.binary.Hex

object HashUtil {
  def sha256(message:String) : String = {
    val md = MessageDigest.getInstance("SHA-256")
    toHexString(md.digest(message.getBytes("UTF-8")))
  }

  def md5(message:String) : String = {
    val md = MessageDigest.getInstance("MD5")
    toHexString(md.digest(message.getBytes("UTF-8")))
  }

  def toHexString(bytes:Array[Byte]) : String = {
    new String(Hex.encodeHex(bytes))
  }
}
