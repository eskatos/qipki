#!/bin/sh
set -e

## resolve links - $0 may be a symlink
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
  PRG="$link"
  else
  PRG=`dirname "$PRG"`"/$link"
  fi  
done
QIPKI_HOME=`dirname "$PRG"`
# make it fully qualified
QIPKI_HOME=`cd "${QIPKI_HOME}" && pwd`

mvn -o clean
mvn -N install
mvn -o site
mvn -o site:stage -DstagingDirectory="${QIPKI_HOME}/target/qipki-site-www/"

exit 0
