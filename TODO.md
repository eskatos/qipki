
# Work in progress

* Add some generated documentation to the build process
* Package a http assembly as a unix daemon
* Apply Qi4j data migration system
* Apply Qi4j FileConfiguration API to all filesystem storage


# Next steps

* Store KeyStores on the Filesystem
* Store CRLs on the Filesystem
* Replace indexing-rdf by indexing-solr
* Add CRL Endpoint in issued X509Certificates
  * Full url given by X509Profile
  * If no url on profile creation, create it with a sensible defaut, allow edition too
  * Clients could use the uri builder to easily create the default one 
* CronLike scheduler
  * Embedd the Qi4j-library-scheduler
  * Use it to generate CRLs so this is not done in request threads


# After that

* Add basic profiles creation in Qi4j-test-support
* Enhance X509Profile with domain rules and certificate template creation
* Create a CAProfile role
  * Needed to add rules to CAs that are use case driven and not standard driven
  * A CA will addapt its behavior based on it's profile
* Create a CRLIssuer role
  * To be a CRLIssuer a CA must be allowed by it's CAProfile
  * One could want to disable CRL issuance on a CA with appropriate CAProfile
  * Revocation on a SubSubSubCA would climb the CAs hierarchy unless finding a CA isssuing CRLs
* Use @Concerns to factorize http Resources (error handling, logging etc..)
* Add shiro for handling roles/permissions
  * Model with one root Role and Permissions, other Roles will emerge themselves later
  * See if programmatic security algorithms (vs. annotations) fits well in DCI Contexts
  * See if the security can be applied to domain layer and it's behavior configured in representation layer
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
* Swing Remote
  * A GUI for dev, debug and test purpose only
  * Start with a single jvm assembly
  * Simple Client/UI reflecting naive rest api list/factory/entity
    * Example: request to x509 factory is presented with two fields:    ComboBox: choose ca in list & TextArea: paste pkcs10 pem

# Quid ?

* Add @Aggregated in EscrowedKeyPair
  * On ManyAssociation<X509> x509s() ?
  * This would mean that when a EKP is deleted every associated X509 are deleted too

