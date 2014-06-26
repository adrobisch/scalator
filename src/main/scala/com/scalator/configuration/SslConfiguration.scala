package com.scalator.configuration

import spray.io.ServerSSLEngineProvider
import java.security.{SecureRandom, KeyStore}
import javax.net.ssl.{TrustManagerFactory, KeyManagerFactory, SSLContext}

trait SslConfiguration {
  def loadKeyStore: KeyStore
  def keyStorePassword: Array[Char]

  // if there is no SSLContext in scope implicitly the HttpServer uses the default SSLContext,
  // since we want non-default settings in this example we make a custom SSLContext available here
  implicit def sslContext: SSLContext = {
    val keyStore = loadKeyStore
    val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm)
    keyManagerFactory.init(keyStore, keyStorePassword)

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    trustManagerFactory.init(keyStore)

    val context = SSLContext.getInstance("TLS")
    context.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom)
    context
  }

  // if there is no ServerSSLEngineProvider in scope implicitly the HttpServer uses the default one,
  // since we want to explicitly enable cipher suites and protocols we make a custom ServerSSLEngineProvider
  // available here
  implicit def sslEngineProvider: ServerSSLEngineProvider = {
    ServerSSLEngineProvider {
      engine =>
        engine.setEnabledCipherSuites(Array("TLS_RSA_WITH_AES_256_CBC_SHA"))
        engine.setEnabledProtocols(Array("SSLv3", "TLSv1"))
        engine
    }
  }
}