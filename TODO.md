
# Quid?

* Add @Aggregated in EscrowedKeyPair
  * On ManyAssociation<X509> x509s()?
  * This would mean that when a EKP is deleted every associated X509 are deleted too
  * Check state in the delete interaction method instead?
* Replace indexing-rdf by indexing-solr?


# Work in progress

* (#---) Follow state refactoring with the Qi4j data migration system
  * Write a complete test scenario from the embedder point of view
  * Move code to qipki-main-core?
  * Write integration tests for migrations
* (##--) Apply Qi4j FileConfiguration API to all filesystem storage
  * Store EntityStore on the filesystem
  * Store KeyStores on the filesystem
* (----) Support CRLs
  * Store CRLs on the filesystem
  * Use the task scheduler to generate CRLs so this is not done in request threads
  * Add CRL Endpoint in issued X509Certificates
    * Full url given by X509Profile
    * If no url on profile creation, create it with a sensible defaut, allow edition too
    * Http presentation layer could provide the default one easily
  * Implements CRL next-update mechanism (See CRL.java in qipki-ca)
    * See if providing two next-update implementations is worth the effort (Netscape and Microsoft ways)


# Next steps - That would lead to a tiny 1.0

* (----) Fix http service configuration handling up to qipki-ca-http-main
* (----) Simplify assembly
  * Factor out infrastructure runtimes and assembly in a dedicated module
  * Depend on that module for tests and distribution packaging
  * QUID Make assembly scenarii modules? --> bootstrap
* (----) Add shiro for handling roles/permissions
  * Model with one root Role and Permissions, other Roles will emerge themselves later
  * See if programmatic security algorithms (vs. annotations) fits well in DCI Contexts
  * See if the security can be applied to bounded contexts (ca, ra ..) without any web context and then add http handling
* (#---) WebUI
  * Generate Javascript Overlay Types for all json resources at build time
  * Find a way to write an eventbus bridge crossing iframes
  * Split gwt code, see http://mojo.codehaus.org/gwt-maven-plugin/user-guide/productivity.html
* (----) Enhance Netscape extensions handling
  * In X509DetailValue : all of them
  * Automatically filled by CAs : Netscape CA Revocation URL
* (----) Generate development site and deploy it to github pages
  * Write a working basic site template
  * What about aggregated reports?
  * Add gwt compiler report, see http://mojo.codehaus.org/gwt-maven-plugin/user-guide/compiler-report.html
  * Style the site
  * Write some pages
  * Deploy to GH Pages

# After that

* Write a build script for common development tasks
* Reduce LOC in ReST Resources
  * Find a way to declare Resources as interfaces so we can use TransientComposites instead of injected Objects
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
* Provide an artifact containing the CryptoAPI without any Qi4j dependencies, maybe with optional JSR330 @Inject annotations for use with compatible IoC containers like Guice or CDI
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
