
# Quid ?

* Add @Aggregated in EscrowedKeyPair
  * On ManyAssociation<X509> x509s() ?
  * This would mean that when a EKP is deleted every associated X509 are deleted too
* Replace indexing-rdf by indexing-solr ?


# Work in progress

* (#---) Reduce LOC in ReST Resources
* (#---) Follow state refactoring with the Qi4j data migration system
  * Move code to qipki-main-core ?
  * Write integration tests for migrations
* (----) Support CRLs
  * Store CRLs on the filesystem
  * Use the task scheudler to generate CRLs so this is not done in request threads
  * Implements CRL next-update mechanism (See CRL.java in qipki-ca)
    * See if providing two next-update implementations is worth the effort (Netscape and Microsoft ways)
  * Add CRL Endpoint in issued X509Certificates
    * Full url given by X509Profile
    * If no url on profile creation, create it with a sensible defaut, allow edition too
    * Clients could use the uri builder to easily create the default one 
* (##--) Apply Qi4j FileConfiguration API to all filesystem storage
  * Store EntityStore on the Filesystem
  * Store KeyStores on the Filesystem

# Next steps

* Add basic profiles creation in Qi4j-test-support
* Use @Concerns to factorize http Resources (error handling, logging etc..)
  * Find a way to declare Resources as interfaces so we can use TransientComposites instead of injected Objects
  * See {@link org.qipki.ca.http.presentation.rest.RestletFinder#create}
* See if it's possible to hook in the http/java serialization and make it as generic as possible
* WebUI
  * Http resources produces their autonomous views with their html/js code
  * The css must be as global as possible
  * Provide a generic host page (html/js) that use an event bus for views collaboration


# After that

* Add shiro for handling roles/permissions
  * Model with one root Role and Permissions, other Roles will emerge themselves later
  * See if programmatic security algorithms (vs. annotations) fits well in DCI Contexts
  * See if the security can be applied to domain layer and it's behavior configured in representation layer
* Implement http cache handling
  * @Cacheable Concern using a payload store
  * UoW hooks to fill in cache store with payload bytes along http metadatas for caching support (etag...) and remove invalid entries
* Monitoring BoundedContext
  * See how it would be possible to reuse the JMX world in a web UI
  * JMX client in web, in an applet, as a webstart ?
  * JMX over websockets ?
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
* Enhance Netscape extensions handling
  * In X509DetailValue : all of them
  * Automatically filled by CAs : Netscape CA Revocation URL
* Change the way key pairs are protected
  * Add PKCS#11 KeyStore support
  * Add encipered filesystem based storage
  * Add a key wallet to officers with the keys they're allowed to use
  * A CAProfile allows, or not, issuance of escrowed certified keypairs
    * The CA gets an EscrowSecretKey to protect the escrowed certified keypairs
  * A CAProfile allows, or not, certificate issuance for known escrowed keypairs
  * Add a warning if a CA signs a PKCS#10 
* Add a domain auditing bounded context filled by @SideEffects
* Wrap client api as a small Qi4j Application usable without Qi4j imports
