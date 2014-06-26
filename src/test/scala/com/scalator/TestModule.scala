package com.scalator

import scaldi.Module
import com.scalator.configuration.Configuration
import com.scalator.util.Password
import com.scalator.util.mail.Mailer

class TestModule extends Module {
  bind[Configuration] to new TestConfiguration
  bind[AppInitializer] to new DefaultAppInitializer
  bind[Password] to new Password
  bind[Mailer] to new Mailer(inject[Configuration])
}
