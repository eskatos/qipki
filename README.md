# QiPKI ![Project status](http://stillmaintained.com/eskatos/qipki.png)


    An engineer, a chemist, and a standards designer are stranded on a desert
    island with absolutely nothing on it.  One of them finds a can of spam washed
    up by the waves.

    The engineer says "Taking the strength of the seams into account, we can
    calculate that bashing it against a rock with a given force will open it up
    without destroying the contents".

    The chemist says "Taking the type of metal the can is made of into account,
    we can calculate that further immersion in salt water will corrode it enough
    to allow it to be easily opened after a day".

    The standards designer gives the other two a condescending look, gazes into
    the middle distance, and begins "Assuming we have an electric can opener...".

                                                         X.509 Style Guide - October 2000
                                                 Peter Gutmann, pgut001@cs.auckland.ac.nz
                                 http://www.cs.auckland.ac.nz/~pgut001/pubs/x509guide.txt


QiPki is an effort to create Java developper tools to build cryptography into applications and heading towards building
a suite of packaged cryptography services.

This is indeed a technology project. Development tries to follow principles learned from DDD, DCI, ReST and more using
the Qi4j engine and framework.

Each component is built as a library and has a http service as restfull as possible using json format for data structures.

See [http://qipki.org](http://qipki.org)

## Changelog

### master

* KeyStores, EscrowedKeyPairs and CRLs are now stored on the filesystem thanks to the FileConfiguration API
* Distribution packaged as a unix daemon in qipki-main-http-ca
* Added X509Context.renew( PKCS10CertificationRequest pkcs10 ) interaction
* Added EscrowedKeyPairContext.delete() interaction
* Added basic profiles creation scenarii in X509ProfileFactory and X509ProfileListContext
* QiPkiHttpCa can now be told what base url to use to generate links in representations (Usefull when accessed through a reverse proxy for example)
* Management layer in QiPkiHttpCa exposing the Qi4j application structure through JMX
* Simplified bootstrap supporting both embedded and standalone ways
* Build layout review: more straightforward, more simple
* No more OSGi support
* Upstream update: Qi4j 1.4

### 1.0-alpha7 - 2011/06/02

* Global package name change from org.codeartisans.* to org.qipki.*
* Fixed entitystore-sql failures exposed by the multithreaded stress test
* Fixed X.509 SubjectKeyIdentifier extension parsing
* Fixed o.q:ca dependencies
* Added @UnitOfWorkRetry concern on CAContext.updateCa
* Added a configurable Random service
* Added symetric ciphering capabilities to o.q.crypto
* Added a JceDetector and a unit test that enforce that the JCE unlimited policies are installed
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

### 1.0-alpha4 - 2010/11/15

* Inconditional index rebuild on startup

### 1.0-alpha1 - 2010/10/30

* Initial release

