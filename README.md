[![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0)
[![Build Status](https://travis-ci.org/dellemc-symphony/common-messaging-parent.svg?branch=master)](https://travis-ci.org/dellemc-symphony/common-messaging-parent)
[![Slack](http://community.codedellemc.com/badge.svg)](https://codecommunity.slack.com/messages/symphony)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.dell.cpsd/common-messaging-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.dell.cpsd/common-messaging-parent)
[![Semver](http://img.shields.io/SemVer/2.0.0.png)](http://semver.org/spec/v2.0.0.html)

# common-messaging-parent

## Description

This repository contains the source code for common-testing, common-rabbit and common-enricher.

- Common Enricher is userd for enricher is for Java Shared Data(JSD) files to help with the generation of classes from the API JSD files.

- Common Testing is a tiny articact to be used as test dependency that would provide small utilities for JUnit testing purposes (e.g message file reader function)
The import of the testing dependency should use the scope import shown below.
<scope>import</scope>

- Common Rabbit is a library of AMQP related functions but is not limited to:
 - message aggregator.
 - base Spring configuration.
 - basic messaging validation checks.
 - default retry policy.
 - basic error handling.


## Documentation
You can find additional documentation for Project Symphony at [dellemc-symphony.readthedocs.io](https://dellemc-symphony.readthedocs.io).

## Before you begin
Verify that the following tools are installed:

* Apache Maven 3.0.5+
* Java Development Kit (version 8)

## Building
Run the following command to build this project:
```bash
mvn clean install
```
## Contributing
Project Symphony is a collection of services and libraries housed at [GitHub][github].

Contribute code and make submissions at the relevant GitHub repository level. See [our documentation][contributing] for details on how to contribute.

## Community
Reach out to us on the Slack [#symphony][slack] channel by requesting an invite at [{code}Community][codecommunity].

You can also join [Google Groups][googlegroups] and start a discussion.
 
[slack]: https://codecommunity.slack.com/messages/symphony
[googlegroups]: https://groups.google.com/forum/#!forum/dellemc-symphony
[codecommunity]: http://community.codedellemc.com/
[contributing]: http://dellemc-symphony.readthedocs.io/en/latest/contributingtosymphony.html
[github]: https://github.com/dellemc-symphony
[documentation]: https://dellemc-symphony.readthedocs.io/en/latest/
