package com.github.grommitz.dropwizard.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
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

	@Test
	public void open() throws IOException {

		InheritedResourceConfigurationSourceProvider<ImageFlareWebAppConfig> provider
				= new InheritedResourceConfigurationSourceProvider<>(ImageFlareWebAppConfig.class);

		InputStream is = provider.open("/profiles/conf-dev.yml");

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ImageFlareWebAppConfig v = mapper.readerFor(ImageFlareWebAppConfig.class).readValue(is);

		assertThat(v.getAwsConfig().getRegion(), is("eu-west-2"));

	}
}