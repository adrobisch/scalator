package com.scalator.util

import java.io.{OutputStream, InputStream}
import scala.collection.mutable.ListBuffer

object IoUtil {
  def inputStreamToByteArray(is: InputStream): Array[Byte] = {
    val buf = ListBuffer[Byte]()
    var b = is.read()
    while (b != -1) {
      buf.append(b.byteValue)
      b = is.read()
    }
    buf.toArray
  }

  def writeInputToOutput(input : InputStream, output : OutputStream) = {
    Iterator
      .continually (input.read)
      .takeWhile (-1 !=)
      .foreach (output.write)
    input.close()
    output.close()
  }
}
