# Setup script for QiPki development
# Run using 'source ./profile.sh'.

script_name='profile.sh'
if [ ! -f $script_name ] ; then
    echo
    echo "Please change to the root directory of QiPki sources"
    echo "and type 'source ./${script_name}'."
    echo
elif [ `basename "$0"` == $script_name ] ; then
    echo
    echo "Please use 'source ./${script_name}'."
    echo
else

export QIPKI_BASEDIR=`pwd`
export PATH="${QIPKI_BASEDIR}/scripts:${PATH}"

if [[ $- = *i* ]] ; then
    # Shell is interactive
    if [ -n "${BASH_VERSION}" ] ; then
        export PS1='(QIPKI) \[\033[01;32m\]\u@\h\[\033[00;34m\] \W \[\033[01;32m\]\$\[\033[00m\] '
    else
        export PS1="(QIPKI) ${USER:-$(type whoami >/dev/null && whoami)}@$(type uname >/dev/null && uname -n) \$ "
    fi
    echo
    echo "QiPki development environment set to '${QIPKI_BASEDIR}'"
    echo
    fi

    export QIPKI_ENV_LOADED=1

fi
unset script_name
