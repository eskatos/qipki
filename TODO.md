# QiPki Roadmap

This documents the constantly evolving QiPki Roadmap. Until decided this is the
prefered way to gather and track progress. After that a true issue management
system will be used, but not before.

## Overview

### QiPki 1.0

Released early 2012, it contains a comprehensive embeddable CA and is based on
Qi4j 1.4.

### QiPki 1.1

Work is already in progress to enhance the Crypto API and CA context.
From here it will be based on Qi4j 2.0.


## Details

QiPki generally uses a Commit-Then-Review policy on most changes.

We use the git branching model provided by
[git-flow](https://github.com/nvie/gitflow#readme) described in
[this web page](http://nvie.com/posts/a-successful-git-branching-model/).


### Work in progress

* (#---) Enhance Crypto API
  * Review, test and document MACs
    * [DONE] Still to write the SupportedTransformations test method for HMACs
    * Implement and test [CBC-MAC](http://en.wikipedia.org/wiki/CBC-MAC), use [openssl](http://stackoverflow.com/questions/2611251/openssl-hmac-using-aes-256-cbc) to create test data
    * Implement and test CMAC
  * Review, test and document signatures
  * Review, test and document X.509
  * Provide an artifact containing the CryptoAPI without any Qi4j dependencies
  * Add FlexiProvider based tests to testdrive the Provider independence
    http://www.flexiprovider.de/


### Next steps

* Change the way keys are handled
  * Write behavioral mixins for entities using the crypto services
  * Apply DCI for usecases modeling
  * Add PKCS#11 KeyStore support
  * Add encipered filesystem based storage
  * Add a key wallet to officers with the keys they're allowed to use
  * CAProfile allows, or not, issuance of escrowed certified keypairs
    * The CA gets an EscrowSecretKey to protect the escrowed certified keypairs
  * CAProfile allows, or not, certificate issuance for known escrowed keypairs
  * Add a warning if a CA signs a PKCS#10


### After that

* What about integration of http://www.cryptool.org/en/jcryptool ?
* (#---) Follow state refactoring with the Qi4j data migration system
  * Build a documented database sample for 1.0-alpha6 and use it as a test
    resource
  * Write a complete test scenario from the embedder point of view around the
    sample database
  * Write unit tests for migrations
* (#---) Documentation
  * Extract code snippets from actual source to generate documentation
    * Rework the snippet parsing code in order to remove the javaparser
      dependency
  * Enhance unit test code lisibility for snippets
  * Crypto API
  * Embedded CA
* Wrap client api as a small Qi4j Application usable without Qi4j imports
* Create a CAProfile role
  * Needed to add rules to CAs that are use case driven and not standard driven
  * A CA will addapt its behavior based on it's profile
* Create a CRLIssuer role
  * To be a CRLIssuer a CA must be allowed by it's CAProfile
  * One could want to disable CRL issuance on a CA with appropriate CAProfile
  * Revocation on a SubSubSubCA would climb the CAs hierarchy unless finding a
    CA isssuing CRLs
* Enhance X509Profile with domain rules and certificate template creation
* (----) Enhance Netscape extensions handling
  * In X509DetailValue : all of them
  * Netscape CA Revocation URL
  * Netscape Revocation URL
  * Netscape Renewal URL
  * Automatically filled by CAs : Netscape CA Revocation URL
  * In X509Profile : choose the good ones
* (----) Enhance CRL support
  * Use the task scheduler to generate CRLs so this is not done in request
    threads
  * Add a command to force CRL regeneration
  * Implements CRL next-update mechanism (See CRL.java in qipki-ca)
    * See if providing two next-update implementations is worth the effort
      (Netscape and Microsoft ways)
* (----) Use mocks in unit tests
* (----) Use shiro for handling roles/permissions
  * Model with one root Role and Permissions, other Roles will emerge
    themselves later
  * See if programmatic security algorithms (vs. annotations) fits well in DCI
    Contexts
  * See if the security can be applied to bounded contexts (ca, ra ..) without
    any web context and then add http handling
  * Permissions defines permissions on interactions
  * Privileges defines permissions on resources: get & head / post & put
  * How to deal with domain needs ?
  * Roles can be built by the deployer
  * One role is built in, immutable and can be enabled/disabled live : root
  * Some roles are built in:
    * ( chief security officer - ??? - maybe not needed )
    * security officer
    * certificate authority
    * request authority officer
    * end entity
  * Permissions and Privileges needed for building roles are built in and
    mutables
* (#---) WebUI
  * Generate Javascript Overlay Types for all json resources at build time
  * Find a way to write an eventbus bridge crossing iframes
  * [Split gwt code](http://mojo.codehaus.org/gwt-maven-plugin/user-guide/productivity.html)
* Reduce LOC in ReST Resources
  * Qi4j 2.0 will provide constructs allowing to declare Resources as
    TransientComposites directly
  * See {@link org.qipki.ca.http.presentation.rest.RestletFinder#create}
  * Use @Concerns to factorize http Resources (error handling, logging etc..)
* Write a (Jamon|GroovyTemplate)StructureReflector in tools/reflect using
  [Jamon](http://www.jamon.org/(
* Implement http cache handling
  * @Cacheable Concern using a payload store
  * UoW hooks to fill in cache store with payload bytes along http metadatas
    for caching support (etag...) and remove invalid entries
* Monitoring BoundedContext
  * Integrate [JavaMelody](http://code.google.com/p/javamelody/) with Qi4j
    using a @Concern
  * What about integrating [GwtMeasure](http://code.google.com/p/gwt-measure/) with JavaMelody?
  * See how it would be possible to reuse the JMX world in a web UI
  * JMX client in web, in an applet, as a webstart?
  * JMX over websockets?
  * http://www.oracle.com/technetwork/java/javase/jnlp-136707.html
* Domain auditing bounded context filled by @SideEffects
* (#---) Add some generated documentation to the build process
* Add tool http resources, for example a resource that parse a given x509
  certificate and return a X509DetailValue
  * With this we can write unit tests for certificate parsing using a bunch of
    external certificates
* Add tool for encryption see
  [this web page](http://docs.codehaus.org/display/SONAR/Settings+Encryption).
