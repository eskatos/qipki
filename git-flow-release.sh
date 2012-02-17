#!/bin/sh
set -e

# Simple script to perform a maven release while following the git flow conventions
# ---------------------------------------------------------------------------------
# Based on https://gist.github.com/1043970

version=$1
if [ -z $version ]
then
    echo "Usage: git-flow-release.sh VERSION"
    exit 0
fi

echo "This will release qipki-$version."
echo "Hit Ctrl-C do stop, Enter to continue."

read
echo "Working ..."
sleep 1
echo "Done!"
exit 0

# branch from develop to a new release branch
git checkout develop
git checkout -b "release/$version"

# perform a maven release, which will tag this branch and deploy artifacts
mvn release:prepare
mvn release:perform

# merge the version changes (pom.xml edits) back into develop so that folks
# are working against the new release
git checkout develop
git merge --no-ff "release/$version"

# housekeeping -- rewind the release branch by one commit to fix its version at "0.0.2"
# excuse the force push, it's because maven will have already pushed '0.0.3-SNAPSHOT'
# to origin with this branch, and I don't want that version (or a diverging revert commit)
# in the release or master branches.
git checkout "release/$version"
git reset --hard HEAD~1
git push --force origin "release/$version"
git checkout develop

# finally, if & when the code gets deployed to production
git checkout master
git merge --no-ff "release/$version"
git branch -d "release/$version"

echo
echo
echo
echo
echo "qipki-$version released"

