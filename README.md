# QiPKI

QiPKI is a simple X.509 PKI implemented in Java using Qi4j.

Each component is built as a library and has a http service as restfull as possible using json format for data structures.

This is Work In Progress, don't expect something with easy setup and comprehensive documentation for now.

## Changelog

### master

* Added a Management layer to the QiPkiHttpCa application exposing the Qi4j application structure through JMX
* Added a main program to run qipki-http-ca
* Added a distribution assembly for qipki-htp-ca with startup scripts for Unix and Windows
* Upstream update: Qi4j 1.4-SNAPSHOT
* Completely removed OSGi support

### 1.0-alpha7 - 2011/06/02

* Global package name change from org.codeartisans.* to org.qipki.*
* Fixed entitystore-sql failures exposed by the multithreaded stress test
* Fixed X.509 SubjectKeyIdentifier extension parsing
* Fixed o.q:ca dependencies
* Added @UnitOfWorkRetry concern on CAContext.updateCa
* Added a configurable Random service
* Added symetric ciphering capabilities to o.q.crypto
* Added a JceDetector and a unit test that enforce that the JCE are installed
* Added code documentation
* Upstream update: Qi4j 1.3
* Minor impact upstream update : restlet from 2.0-M? to stable 2.0.7

### 1.0-alpha6 - 2011/02/03

* Deactivated CRL generation as the property was growing infinetely, CRLs ...
* Hacking documentation
* Minor impact upstream update : o.c.j:java-toolbox:1.4

### 1.0-alpha5 - 2010/12/06

* Fixed X.509 KeyUsage extension parsing
* Index rebuild on startup only if the index directory does not exists
* Tightened indexed properties in model to reduce data footprint and speed up runtime
* Unit tests now use persistent Entity Store and Index
* Added good support for distinguished names and oid mapping
* Factor out osgi in the build system to make cleaner releases and global cleanup
* Minor impact upstream update : o.c.j:java-toolbox:1.3

### 1.0-alpha4 - 2011/11/15

* Inconditional index rebuild on startup

### 1.0-alpha1 - 2010/10/30

* Initial release

