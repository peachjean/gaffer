[![Build Status](https://travis-ci.org/peachjean/gaffer.svg)](https://travis-ci.org/peachjean/gaffer)
[![Coverage Status](https://coveralls.io/repos/peachjean/gaffer/badge.svg)](https://coveralls.io/r/peachjean/gaffer)

gaffer
------

"Configuration" means many different things to many different people. There are
a myriad of libraries that handle configuration, and many of them mean something
different. For instance, basic ``Properties`` files provide a somewhat nested
key-value pair style configuration, while
[Apache Commons Configuration][commons-configuration] basically provides
multiple ways to specify hierarchies of properties. On the other hand, if we
look at a project like [tofigurator][tofigurator], we see a strategy to build
up an object graph. We also see similar configuration strategies if we look at
the ``shiro.ini`` file in [Apache Shiro][shiro], or the [Jetty][jetty]
configuration, or most any of the logging frameworks.

``gaffer`` fulfills this second style of configuration, meant to be used to
configure application components that require or allow for complex object graph
instantiation. It provides a more flexible, less verbose, and more intuitive
 syntax than [tofigurator][tofigurator]. Inspiration (and a good bit of code)
 has been drawn from [``logback``'s][logback] groovy configuration mechanism.
 The general idea with ``gaffer`` is to take the ideas in the logback groovy
 configuration and make a set of utilities generic enough to allow for the same
 general style of configuration in any library.

 [logback]: http://logback.qos.ch/ "Logback"
 [shiro]: http://shiro.apache.org/ "Apache Shiro"
 [jetty]: http://eclipse.org/jetty/ "Jetty"
 [commons-configuration]: http://commons.apache.org/proper/commons-configuration/ "Apache Commons Configuration"
 [tofigurator]: https://code.google.com/p/tofigurator/ "Tofigurator"

