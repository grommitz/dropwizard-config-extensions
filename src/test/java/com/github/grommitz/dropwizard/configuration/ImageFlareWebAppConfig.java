package com.github.grommitz.dropwizard.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class ImageFlareWebAppConfig extends Configuration {

	@JsonProperty
	private String name;

	@JsonProperty
	private AwsConfig awsConfig;

	public String getName() {
		return name;
	}

	public AwsConfig getAwsConfig() {
		return awsConfig;
	}

	public static class AwsConfig {

		@JsonProperty
		private String region;

		public String getRegion() {
			return region;
		}
	}

}
