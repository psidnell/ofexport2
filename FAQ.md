[Home](README.md) | [Release Notes](RELEASE-NOTES.md) | [Support](SUPPORT.md) | [Documentation](DOCUMENTATION.md)

# FAQ

## Exception in thread "main" java.lang.UnsupportedClassVersionError

Caused by having the wrong or multiple versions of java.

Try adding the following to your .bash_profile file:

    export JAVA_HOME=`/usr/libexec/java_home -v 1.8`

Details [here](https://github.com/psidnell/ofexport2/issues/4).

## java -version shows an older version of java

Caused by having the wrong or multiple versions of java.

Try adding the following to your .bash_profile file:

    export JAVA_HOME=`/usr/libexec/java_home -v 1.8`

Details [here](https://github.com/psidnell/ofexport2/issues/4).
