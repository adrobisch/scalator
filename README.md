![scalator_logo](scalator_big.png "scalator")

Scalator
========

Scalator is a template for a modern web application using **Scala + AngularJS** with hot reload for Scala and JavaScript code,
created in the spirit of [jhipster](https://github.com/jhipster).

Requirements
============

You need `npm` and `sbt` to use *scalator*.

Getting Started
===============

Execute
```
npm install
bowser install
```
in *src/main/web*

To run the with hot-compile and reload:
```
    sbt watch
```

To build the distribution package in *target/unviveral*:
```
    sbt dist
```

Setup a Keystore with your certificate
======================================

You will need the private key (.key), the certificate file (.crt) and the CA certificate file (.ca)

    openssl pkcs12 -export -in www.scalator.org.crt -inkey www.scalator.org.key \
               -out scalator.p12 -name scalator \
               -CAfile www.scalator.org.ca -caname root

    keytool -importkeystore \
        -deststorepass password -destkeypass password -destkeystore keystore.jks \
        -srckeystore scalator.p12 -srcstoretype PKCS12 -srcstorepass password \
        -alias scalator

Enter a password for your keystore.

License
=======

Scalator is published under the terms of the Apache 2.0 License.
