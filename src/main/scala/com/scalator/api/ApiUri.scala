package com.scalator.api

import spray.http.Uri

trait ApiUri {
  def apiUri(path: String)(implicit requestUri: Uri) = s"${requestUri.scheme}:${requestUri.authority}/api/${path}"
}
