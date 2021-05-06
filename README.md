
# Dropwizard Config Extensions

The is to create a Dropwizard ConfigurationSourceProvider  which can implment profiles in the same way as Spring Boot and MicroProfile, with a base config file which is overridden by a profile-specific config.

Jackson provides a way to do this with the ObjectMapper.readerForUpdating() method, however there is currently a problem with polymorphic properties. 