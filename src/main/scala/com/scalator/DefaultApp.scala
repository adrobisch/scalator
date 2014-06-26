package com.scalator

import com.scalator.util.Logging
import scaldi.Injectable
import com.scalator.api.route.Routes

object DefaultApp extends App with Logging with Injectable {
  implicit val modules = Modules.defaultModules

  new Boot().start()
}
