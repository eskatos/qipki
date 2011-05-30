# QiPKI

QiPKI is a simple X.509 PKI implemented in Java using Qi4j.

Each component is built as a library and has a http service as restfull as possible using json format for data structures.

This is Work In Progress, don't expect something with easy setup and comprehensive documentation for now.

## Changelog

### master

* Fixed X.509 SubjectKeyIdentifier extension parsing
* Global package name change from org.codeartisans.* to org.qipki.*
* Added a configurable Random service
* Added symetric ciphering capabilities to o.q.crypto
* Added a JceDetector and a unit test that enforce that the JCE are installed
* Upstream update: Qi4j 1.3
* Fixed o.q:ca dependencies
* Minor impact upstream update : restlet from 2.0-M? to stable 2.0.6
* Added code documentation

### 1.0-alpha6

* Deactivated CRL generation as the property was growing infinetely, CRLs ...
* Hacking documentation
* Minor impact upstream update : o.c.j:java-toolbox:1.4

### 1.0-alpha5

* Fixed X.509 KeyUsage extension parsing
* Index rebuild on startup only if the index directory does not exists
* Tightened indexed properties in model to reduce data footprint and speed up runtime
* Unit tests now use persistent Entity Store and Index
* Added good support for distinguished names and oid mapping
* Factor out osgi in the build system to make cleaner releases and global cleanup
* Minor impact upstream update : o.c.j:java-toolbox:1.3

### 1.0-alpha4

* Inconditional index rebuild on startup

### 1.0-alpha1

* Initial release

