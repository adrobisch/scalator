package com.scalator.pages

import org.scalatest.concurrent.Eventually
import org.scalatest.{Suite, BeforeAndAfterAll, ShouldMatchers}
import org.scalatest.selenium.WebBrowser
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import scala.concurrent.Await
import com.scalator.TestApp
import scala.concurrent.duration._

trait AppLoaded extends BeforeAndAfterAll {
  self: Suite with Eventually with ShouldMatchers with WebBrowser =>

  // TODO: make configurable
  implicit val webDriver: WebDriver = new FirefoxDriver
  val testApp = new TestApp()

  val host = "https://localhost:3333/"

  override def beforeAll() = {
    Await.result(testApp.start, 10 seconds)
  }

  override def afterAll() = {
    webDriver.close()
    testApp.stop()
  }

  def pageLoaded[T](fun:  => T) {
    eventually {
      val loaded = executeAsyncScript(
        """
          | var callback = arguments[arguments.length - 1];
          | callback(document.appLoaded == true);
        """.stripMargin)
      loaded shouldEqual true
    }
    fun
  }
}
