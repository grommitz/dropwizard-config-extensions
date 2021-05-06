package com.github.grommitz.dropwizard.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InheritedResourceConfigurationSourceProviderTest {

	@BeforeEach
	public void setUp() throws Exception {
	}

	// fails due to the polymorphic connectorFactory field
	@Test
	public void loadDropWizardConfig() throws IOException {

		InheritedResourceConfigurationSourceProvider<MyDropwizardConfiguration> provider
				= new InheritedResourceConfigurationSourceProvider<>(MyDropwizardConfiguration.class);

		InputStream is = provider.open("/config-prod.yml");

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		MyDropwizardConfiguration v = mapper.readerFor(MyDropwizardConfiguration.class).readValue(is);

		assertThat(v.getAwsConfig().getRegion(), is("eu-west-2"));

	}
}