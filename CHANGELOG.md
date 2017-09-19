# Changelog
All notable changes to this project are documented in this file.
 
The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [2.1.1] - 2017-09-19

### Added
 - Configuration bean classes that can be activated and consumed using spring boot common-rabbitmq-starter project
 	ConsumerConfig - Beans for default ```simpleMessageListenerContainer``` and ```rabbitListenerContainerFactory```
	RabbitConfig - Beans for default ```rabbitTemplete```, ```rabbitAdmin``` and ```messageConverter```

## [2.1.0] - 2017-09-19
 - Depricated following methods in ```IRabbitMqPropertiesConfig``` and its default implementation.
 ```Java
    String trustStorePassphrase();
    String keyStorePassPhrase();
    String keyStorePath();
    String trustStorePath();
 ```
 - TLS is enabled by default with TLSv1.2 in ```IRabbitMqPropertiesConfig```
 - Depricated ```TLSConnectionFactory``` class.
 - Added ```RabbitMQTLSFactoryBean``` class replacing ```TLSConnectionFactory``` as the default for rabbit connection with TLS.
 - ```RabbitMqProductionConfig``` changed to accept new TLS changes.

## [Unreleased]

### Added
 - Log file name to failing startup message for Docker compose rule.
 - Docker compose rule health check information for scaleio-adapter service.

### Changed
 - Default timeout value for containers' health checks from 1 hour to 10 minutes.

## [2.0.0] - 2017-08-18

### Added
 - Docker compose rule health check information for govcsim and system-definition service.
 
### Changed
 - The GroupID identifier to be consistent with the naming convention com.dell.cpsd.

## 1.5.0 - 2017-07-17

### Added
 - The initial common-messaging-parent binary files.

[2.0.0]: https://github.com/dellemc-symphony/common-messaging-parent/compare/1.5.0...2.0.0
