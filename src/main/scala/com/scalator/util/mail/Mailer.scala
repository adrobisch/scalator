package com.scalator.util.mail

import org.apache.commons.mail._
import scala.Some
import com.scalator.configuration.Configuration

case class Mail(from: (String, String), // (email -> name)
                 to: Seq[String],
                 cc: Seq[String] = Seq.empty,
                 bcc: Seq[String] = Seq.empty,
                 subject: String,
                 message: String,
                 richMessage: Option[String] = None,
                 attachment: Option[(java.io.File)] = None)

case class MailConfig(hostName: String = "localhost", port: Int = 25, username: String, password: String, ssl: Boolean = false)

// taken from gist: https://gist.github.com/mariussoutier/3436111
class Mailer(val configuration: Configuration) {


  val mailConfig = MailConfig(hostName = configuration.smtpHost,
    port = configuration.smtpPort, username = configuration.smtpUsername, password = configuration.smtpPassword)

  implicit def stringToSeq(single: String): Seq[String] = Seq(single)

  implicit def liftToOption[T](t: T): Option[T] = Some(t)

  sealed abstract class MailType

  case object Plain extends MailType

  case object Rich extends MailType

  case object MultiPart extends MailType

  def send(mail: Mail) {

    val format =
      if (mail.attachment.isDefined) MultiPart
      else if (mail.richMessage.isDefined) Rich
      else Plain

    val commonsMail: Email = format match {
      case Plain => new SimpleEmail().setMsg(mail.message)
      case Rich => new HtmlEmail().setHtmlMsg(mail.richMessage.get).setTextMsg(mail.message)
      case MultiPart => {
        val attachment = new EmailAttachment()
        attachment.setPath(mail.attachment.get.getAbsolutePath)
        attachment.setDisposition(EmailAttachment.ATTACHMENT)
        attachment.setName(mail.attachment.get.getName)
        new MultiPartEmail().attach(attachment).setMsg(mail.message)
      }
    }

    commonsMail.setHostName(mailConfig.hostName)
    commonsMail.setSmtpPort(mailConfig.port)
    commonsMail.setAuthenticator(new DefaultAuthenticator(mailConfig.username, mailConfig.password))

    if (mailConfig.ssl)
      commonsMail.setSSLOnConnect(true)

    // Can't add these via fluent API because it produces exceptions
    mail.to foreach (commonsMail.addTo(_))
    mail.cc foreach (commonsMail.addCc(_))
    mail.bcc foreach (commonsMail.addBcc(_))

    commonsMail.
      setFrom(mail.from._1, mail.from._2).
      setSubject(mail.subject).
      send()
  }
}
