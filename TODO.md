# QiPki Roadmap

This documents the constantly evolving QiPki Roadmap. Until 1.0-rc1 this is the prefered way to gather and track 
progress. After that a true issue management system will be used, but not before.

## Overview

### QiPki 1.0

Work in progress will lead to QiPki 1.0 that will contain a comprehensive embeddable CA and will be based on Qi4j 1.4.

### QiPki 1.1

Work is already in progress to add a reasonable CA with HTTP interactions.


## Details

### Quid?

Do we really want to do that? Is this even possible? Is it the right time?

* Move to Qi4j 2.0-SNAPSHOT?


### Work in progress

* (##--) Documentation
  * Crypto API
  * Embedded CA


### Next steps - That would lead to a tiny 1.0

* Enhance Crypto API
  * Provide an artifact containing the CryptoAPI without any Qi4j dependencies, maybe with optional JSR330 @Inject annotations for use with compatible IoC containers like Guice or CDI

### After that

* (##--) Rework Qi4j HttpService
  * Write unit tests for as much as possible of the configuration options (vhosts, mutual authentication etc..)
  * No more DefaultServlet
    * Remove default servlet added by default
    * Remove rootResourceBase from configuration
    * Write a DefaultServletService & DefaultServletConfiguration
    * Add assembly helpers to add DefaultServlet
  * QUID?
    * See how to restrict requests on a specific host (Host http header)
    * What to expose on JMX? Jetty statistics?
    * Add a pluggable SSLContextProviderService or something like that with assembly facilities
    * SecureJettyMixin would use SecureJettyConfiguration by default but use SSLContextProviderService if present
* Fix Scheduler passivation, it seems one thread is not interrupted correctly causing very slow shutdown during unit tests and runtime
* (----) Review Qi4j SQL Support
  * Merge my and Rickard support for DataSources
  * Find a clever way to use FileConfiguration API to store databases in ~/data for SGBDs that support it (Derby only ATM)
  * Add HSQLDB support to have another embedded SGBD
  * Use MySQL Java deployment facilities
* (----) Add shiro for handling roles/permissions
  * Model with one root Role and Permissions, other Roles will emerge themselves later
  * See if programmatic security algorithms (vs. annotations) fits well in DCI Contexts
  * See if the security can be applied to bounded contexts (ca, ra ..) without any web context and then add http handling
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
  * Permissions and Privileges needed for building roles are built in and mutables
* (#---) WebUI
  * Generate Javascript Overlay Types for all json resources at build time
  * Find a way to write an eventbus bridge crossing iframes
  * Split gwt code, see http://mojo.codehaus.org/gwt-maven-plugin/user-guide/productivity.html
* (----) Enhance Netscape extensions handling
  * In X509DetailValue : all of them
  * Netscape CA Revocation URL
  * Netscape Revocation URL
  * Netscape Renewal URL
  * Automatically filled by CAs : Netscape CA Revocation URL
  * In X509Profile : choose the good ones
* (----) Enhance CRL support
  * Use the task scheduler to generate CRLs so this is not done in request threads
  * Add a command to force CRL regeneration
  * Implements CRL next-update mechanism (See CRL.java in qipki-ca)
    * See if providing two next-update implementations is worth the effort (Netscape and Microsoft ways)
* (#---) Follow state refactoring with the Qi4j data migration system
  * Get a documented database sample for 1.0-alpha6 and use it as a test resource
  * Write a complete test scenario from the embedder point of view around the sample database
  * Write unit tests for migrations
* Reduce LOC in ReST Resources
  * Qi4j 2.0 will provide constructs allowing to declare Resources as TransientComposites directly
  * See {@link org.qipki.ca.http.presentation.rest.RestletFinder#create}
  * Use @Concerns to factorize http Resources (error handling, logging etc..)
* Write a (Jamon|GroovyTemplate)StructureReflector in tools/reflect using http://www.jamon.org/
* Customize JSON serialization in resources with the help of Qi4j 2.0 pluggable serialization
* Implement http cache handling
  * @Cacheable Concern using a payload store
  * UoW hooks to fill in cache store with payload bytes along http metadatas for caching support (etag...) and remove invalid entries
* Monitoring BoundedContext
  * Integrate JavaMelody with Qi4j using a @Concern http://code.google.com/p/javamelody/
  * What about integrating GwtMeasure with JavaMelody? http://code.google.com/p/gwt-measure/
  * See how it would be possible to reuse the JMX world in a web UI
  * JMX client in web, in an applet, as a webstart?
  * JMX over websockets?
  * http://www.oracle.com/technetwork/java/javase/jnlp-136707.html
* Domain auditing bounded context filled by @SideEffects
* (#---) Add some generated documentation to the build process
* Enhance X509Profile with domain rules and certificate template creation
* Create a CAProfile role
  * Needed to add rules to CAs that are use case driven and not standard driven
  * A CA will addapt its behavior based on it's profile
* Create a CRLIssuer role
  * To be a CRLIssuer a CA must be allowed by it's CAProfile
  * One could want to disable CRL issuance on a CA with appropriate CAProfile
  * Revocation on a SubSubSubCA would climb the CAs hierarchy unless finding a CA isssuing CRLs
* Add tool http resources, for example a resource that parse a given x509 certificate and return a X509DetailValue
  * With this we can write unit tests for certificate parsing using a bunch of external certificates
* Change the way key pairs are protected
  * Add PKCS#11 KeyStore support
  * Add encipered filesystem based storage
  * Add a key wallet to officers with the keys they're allowed to use
  * A CAProfile allows, or not, issuance of escrowed certified keypairs
    * The CA gets an EscrowSecretKey to protect the escrowed certified keypairs
  * A CAProfile allows, or not, certificate issuance for known escrowed keypairs
  * Add a warning if a CA signs a PKCS#10 
* Wrap client api as a small Qi4j Application usable without Qi4j imports
