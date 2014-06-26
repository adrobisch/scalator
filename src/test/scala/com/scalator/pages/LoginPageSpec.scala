package com.scalator.pages

import org.scalatest.{ShouldMatchers, FlatSpec}
import org.scalatest.selenium.WebBrowser
import org.scalatest.concurrent.Eventually
import org.scalatest.concurrent.IntegrationPatience

class LoginPageSpec extends FlatSpec with ShouldMatchers with WebBrowser with Eventually with IntegrationPatience with AppLoaded {
  "The App " should "redirect to the Login Page" in {
    go to host
    pageLoaded {
      eventually {
        currentUrl should endWith("#/login")
      }
    }
  }

  "The App " should "login the Test User" in {
    go to host
    pageLoaded {
      textField("login").value = "admin"
      pwdField("password").value = "admin"
      click on "loginButton"
    }
  }
}
