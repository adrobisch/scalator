package com.scalator

import com.scalator.util.Logging
import scaldi.Injectable
import com.scalator.api.route.Routes

class TestApp extends Logging with Injectable {
  implicit val modules = new TestModule :: Modules.defaultModules

  val app = new Boot()

  def start = app.start()
  def stop() = app.stop()
}
