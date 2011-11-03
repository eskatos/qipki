# QiPki Build


## Overview

QiPki use maven as its build system. The project structure is kept as simple as possible in order to prevent maven nightmares : one root pom and several direct submodules using the root pom as parent.

### Modules

All QiPki modules use the `org.codeartisans.qipki` group ID.

 Folder name           | Artifact ID                | Purpose
-----------------------|----------------------------|-------------
 `./site/`             | `qipki-site`               | http://qipki.org
 `./mainsupport/`      | `qipki-mainsupport`        | `main` plumbing
 `./testsupport/`      | `qipki-testsupport`        | Unit tests plumbing
 `./qi4j-lib-uowfile/` | `org.qi4j.library.uowfile` | Qi4j library to bind operations on files to a UnitOfWork
 `./crypto/`           | `qipki-crypto`             | Crypto Engine
 `./commons/`          | `qipki-commons`            | Common code like values and their factories.
 `./core/`             | `qipki-core`               | QiPki Applications plumbing
 `./ca/`               | `qipki-ca`                 | Embeddable CA
 `./ca-http/`          | `qipki-http`               | HTTP based CA server
 `./ca-http-main/`     | `qipki-http-main`          | HTTP based CA server packaged as a UNIX daemon
 `./ca-http-client/`   | `qipki-http-client`        | Client library for the HTTP based CA server
 `./ca-tests/`         | `qipki-ca-tests`           | CA automated tests
 `./web-client/`       | `qipki-web-client`         | GWT based web UI


## Getting started

Start by cloning the git repository:

    git clone https://github.com/eskatos/qipki.git

As the build is maven based, we suggest to read about maven itself. We will only present build sequences used frequently during QiPki development. 

### Complete build

If, for example, you want to write code that use a version you build yourself, do:

    mvn install

This would run a complete build. Once done, jar and other artifacts produced by the build system are installed in your local maven repository. It means you can depend on them easily from other projects, right from your IDE.

### Quick `ca` installation in local maven repository

    mvn -am -pl ca install -DskipTests

This command will build and install the `ca` modules and all it's dependent QiPki modules, not doing `clean` and skipping the tests.


### Run all the `ca-tests`

    mvn -am -pl ca-tests test

This command will build and test all the modules on which `ca-tests` depends and run all the tests presents in it.

### Run the development instance of `ca-http`

TODO Take a look in the `ca-http-main` module if you're interested.

### Run the development instance of `web-client`

    mvn -pl web-client gwt:run

## Profiles

Profiles described here are not activated by default and released versions of QiPki are of course not built using any of them.

Here is an example to activate the `foo` and `bar` profiles:

    mvn clean install -Pfoo -Pbar

### Speed up the build

The `quick` profile can be activated to shorten the build time.

TODO Do nothing for now, need to work on the GWT profiles

### Debug automated tests

If you activate the `test-debug` profile, all tests will produce diagnostic data, either output or files to help you working on them.

For example unit tests involving persistence and query will delete their data after a successful test by default. With the `test-debug` profile activated this data won't be deleted so you can look at it.

When using this profile, it is not guaranteed that the build is "no-clean proof". It means that after running the build once, you will have to run the maven `clean` phase before running `test` again.

## Properties

To set a property, add `-DpropertyName=propertyValue` to your maven command line invocation. Flags properties can simply be activated with `-DpropertyName`.

### Build properties

Property | Effect
---------|-------


### Usefull standard maven properties

Property | Effect
---------|-------
skipTests | Flag to skip all test related build steps. Unit tests are not compiled!



