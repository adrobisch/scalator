package com.scalator.util.mail

import org.specs2.mutable.Specification
import org.subethamail.wiser.{WiserMessage, Wiser}
import scala.collection.JavaConversions._
import com.scalator.TestConfiguration

class MailSpec extends Specification {
  def withWiserRunning(block: Wiser => Unit): List[WiserMessage] = {
    val port = 2525 + (Math.random() * 100).toInt
    val wiser = new Wiser(port)

    wiser.start()
    block.apply(wiser)
    wiser.stop()

    wiser.getMessages.toList
  }

  "Mail util" should {
    "send mail" in {
      val messages = withWiserRunning {wiser: Wiser =>
        val mailer = new Mailer(new TestConfiguration {
          override def smtpPort: Int = wiser.getServer.getPort
        })
        val mail = Mail(from = "info@noreply.org" -> "Test",
          to = Seq("testuser@noreply.org"),
          subject = "Test Subject",
          message = "Test Message")
          mailer.send(mail)
      }

      messages.size must beEqualTo(1)

      val message: WiserMessage = messages.get(0)

      message.getEnvelopeSender must beEqualTo("info@noreply.org")
      message.getEnvelopeReceiver must beEqualTo("testuser@noreply.org")
      message.getMimeMessage.getContent must beEqualTo("Test Message\r\n")
    }
  }
}
