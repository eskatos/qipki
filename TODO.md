
# Work in progress

Add some generated documentation to the build process


# Next steps

Fix entitystore-sql failures exposed by the multithreaded stress test

Replace indexing-rdf by indexing-solr

Add @Aggregated in EscrowedKeyPair

Store keystores and CRLs on the filesystem using FileConfiguration API

Add CRL Endpoint in issued X509Certificates

    Full url given by X509Profile
    If no url on profile creation, create it with a sensible defaut, allow edition too
    Clients could use the uri builder to easily create the default one 

Apply qi4j data migration system

CronLike scheduler

    Embedd the qi4j-library-scheduler
    Use it to generate CRLs so this is not done in request threads




# After that

Add basic profiles creation in qi4j-test-support

Enhance X509Profile with domain rules and certificate template creation

Create a CAProfile role

    Needed to add rules to CAs that are use case driven and not standard driven
    A CA will addapt its behavior based on it's profile

Create a CRLIssuer role

    To be a CRLIssuer a CA must be allowed by it's CAProfile
    One could want to disable CRL issuance on a CA with appropriate CAProfile
    Revocation on a SubSubSubCA would climb the CAs hierarchy unless finding a CA isssuing CRLs

Use @Concerns to factorize http Resources (error handling, logging etc..)

Add shiro for handling roles/permissions

    Model with one root Role and Permissions, other Roles will emerge themselves later
    See if programmatic security algorithms (vs. annotations) fits well in DCI Contexts
    See if the security can be applied to domain layer and it's behavior configured in representation layer

Add tool http resources, for example a resource that parse a given x509 certificate and return a X509DetailValue

    With this we can write unit tests for certificate parsing using a bunch of external certificates

Expose X509ProfileAssignments as a CA subresource so client can POST on it to update CA assignments

    /ca/{identity}/profiles return a list of profiles, POST update this list

Enhance Netscape extensions handling

    In X509DetailValue : all of them
    Automatically filled by CAs
        Netscape CA Revocation URL

Package a http assembly as a unix daemon

Change the way key pairs are protected

    Add PKCS#11 KeyStore support
    Add encipered filesystem based storage
    Add a key wallet to officers with the keys they're allowed to use
    A CAProfile allows, or not, issuance of escrowed certified keypairs
        The CA gets an EscrowSecretKey to protect the escrowed certified keypairs
    A CAProfile allows, or not, certificate issuance for known escrowed keypairs
    Add a warning if a CA signs a PKCS#10 
    

Add a domain auditing bounded context filled by @SideEffects

Wrap client api as a small qi4j application usable without qi4j imports

Swing Remote

    A GUI for dev, debug and test purpose only
    Start with a single jvm assembly
    Run with a profile giving urls for http and jmx of components
        Load configuration values before assembly in a json (SingletonAssembly bootstrap)
    Simple Client/UI reflecting naive rest api list/factory/entity
        Example: request to x509 factory is presented with two fields:    ComboBox: choose Fca in list   &   TextArea: paste pkcs10 pem
  Client UI
  resource my/tasks
    list
    horizontal sliding view
    differenciate with tags so clients could choose icons/styling
    provide (rss?) feed



