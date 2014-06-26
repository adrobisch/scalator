package com.scalator

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import com.scalator.api.ApiActor
import scala.concurrent.Future
import akka.pattern.ask
import scala.util.Success
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import com.scalator.api.route.Routes
import scaldi.{Injector, Injectable}
import com.scalator.configuration.{KeyStoreProvider, SslConfiguration, Configuration}
import java.security.KeyStore

case class AppStarted()

class Boot(implicit injector: Injector) extends SslConfiguration with Injectable {
  val initializer: AppInitializer = inject[AppInitializer]
  val routes: Routes = inject[Routes]
  val config = inject[Configuration]
  val keyStoreConfig = inject[KeyStoreProvider]

  val actorSystem: ActorSystem = inject[ActorSystem] (by default ActorSystem("app"))

  def start(): Future[AppStarted] = {
    import actorSystem.dispatcher
    initializer.init()
    createServiceActorAndBindHttp(actorSystem).andThen({
      case success: Success[Http.Bound] => postStart()
    }).map(bound => {
      AppStarted()
    })
  }

  def postStart() = {
  }

  def stop() = {
    actorSystem.shutdown()
  }

  def createServiceActorAndBindHttp(actorSystem: ActorSystem): Future[Http.Bound] = {
    implicit val system = actorSystem
    implicit val timeout = Timeout(10, TimeUnit.SECONDS)

    val service = system.actorOf(apiProps(), "service")

    (IO(Http) ? Http.Bind(service,
      interface = config.httpInterface,
      port = config.httpPort)).mapTo[Http.Bound]
  }

  def apiProps(): Props = {
    Props[ApiActor](new ApiActor(routes, config))
  }

  override def loadKeyStore: KeyStore = keyStoreConfig.keystore

  override def keyStorePassword = config.keyStorePassword.get.toCharArray
}
