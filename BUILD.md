# QiPki Build


## Overview

QiPki use maven as its build system. The project structure is kept as simple as possible in order to prevent maven nightmares : one root pom and several direct submodules using the root pom as parent.

## What's needed?

In order to build QiPki from the sources you will need <a target="_blank" href="http://help.github.com/set-up-git-redirect">git</a>, <a target="_blank" href="http://www.java.com/download/">java</a> and <a target="_blank"href="http://maven.apache.org/download.html">maven</a>.

### Modules

All QiPki modules use the `org.codeartisans.qipki` group ID.

<table>
    <thead><tr> <td>Folder name</td> <td>Artifact ID</td> <td>Purpose</td> </tr></thead>
    <tbody>
        <tr>
            <td>./site-maven-plugin/</td>
            <td>qipki-site-maven-plugin</td>
            <td>Maven plugin used to build http://qipki.org</td>
        </tr>
        <tr>
            <td>./site/</td>
            <td>qipki-site</td>
            <td>http://qipki.org</td>
        </tr>
        <tr>
            <td>./mainsupport/</td>
            <td>qipki-mainsupport</td>
            <td>main plumbing</td>
        </tr>
        <tr>
            <td>./testsupport/</td>
            <td>qipki-testsupport</td>
            <td>Unit tests plumbing</td>
        </tr>
        <tr>
            <td>./qi4j-lib-uowfile/</td>
            <td>org.qi4j.library.uowfile</td>
            <td>Qi4j library to bind operations on files to a UnitOfWork</td>
        </tr>
        <tr>
            <td>./crypto/</td>
            <td>qipki-crypto</td>
            <td>Crypto Engine</td>
        </tr>
        <tr>
            <td>./commons/</td>
            <td>qipki-commons</td>
            <td>Common code like values and their factories.</td>
        </tr>
        <tr>
            <td>./core/</td>
            <td>qipki-core</td>
            <td>QiPki Applications plumbing</td>
        </tr>
        <tr>
            <td>./ca/</td>
            <td>qipki-ca</td>
            <td>Embeddable CA</td>
        </tr>
        <tr>
            <td>./ca-http/</td>
            <td>qipki-http</td>
            <td>HTTP based CA server</td>
        </tr>
        <tr>
            <td>./ca-http-main/</td>
            <td>qipki-http-main</td>
            <td>HTTP based CA server packaged as a UNIX daemon</td>
        </tr>
        <tr>
            <td>./ca-http-client/</td>
            <td>qipki-http-client</td>
            <td>Client library for the HTTP based CA server</td>
        </tr>
        <tr>
            <td>./ca-tests/</td>
            <td>qipki-ca-tests</td>
            <td>CA automated tests</td>
        </tr>
        <tr>
            <td>./web-client/</td>
            <td>qipki-web-client</td>
            <td>GWT based web UI</td>
        </tr>
    </tbody>
</table>


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

### Full rebuild and deployment of qipki.org

    mvn -pl site-maven-plugin,site install

TODO Document the site generation and deployment

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

<table>
    <thead>
        <tr>
            <td>Property</td>
            <td>Effect</td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td></td>
            <td></td>
        </tr>
    </tbody>
</table>

### Usefull standard maven properties

<table>
    <thead>
        <tr>
            <td>Property</td>
            <td>Effect</td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>skipTests</td>
            <td>Flag to skip all test related build steps. Unit tests are not compiled!</td>
        </tr>
    </tbody>
</table>

