package com.github.grommitz.dropwizard.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.ConfigurationSourceProvider;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ExtendableResourceConfigurationSourceProvider<T extends Configuration> implements ConfigurationSourceProvider {

	private Class<T> configType;

	public ExtendableResourceConfigurationSourceProvider(Class<T> configType) {
		this.configType = configType;
	}

	@Override
	public InputStream open(String path) throws IOException {
		InputStream result = getResourceAsStream(path);
		if (result == null && path.startsWith("/")) {
			path = path.substring(1);
			result = getResourceAsStream(path);
		}

		String fn = new File(path).getCanonicalFile().getName();
		System.out.println("loading " + fn);

		if (fn.contains("-")) {
			String fn0 = fn.substring(0, fn.indexOf("-"));
			String profile = fn.substring(fn.indexOf("-")+1, fn.indexOf("."));
			String extn = fn.substring(fn.indexOf(".")+1);

			String pathOnly = path.substring(0, path.lastIndexOf("/"));
			String base = pathOnly + "/" + fn0 + "." + extn;

			System.out.println("fn0 = " + fn0);
			System.out.println("profile = " + profile);
			System.out.println("extn = " + extn);
			System.out.println("base = " + base);

			T config = null;
			try {
				config = configType.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IOException(e);
			}

			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			ObjectReader objectReader = mapper.readerForUpdating(config);
			//FileUtil.readResourceTextFile("")

			objectReader.readValue(getResourceAsStream(base));
			objectReader.readValue(result);
			System.out.println(mapper.writerFor(configType)
					.withDefaultPrettyPrinter().writeValueAsString(config));

			String mergedYaml = mapper.writerFor(configType).writeValueAsString(config);

			result =  new ByteArrayInputStream(mergedYaml.getBytes());

		} else {
			System.out.println("no profile.");

		}


		return result;

	}

	private InputStream getResourceAsStream(String path) {
		return getClass().getClassLoader().getResourceAsStream(path);
	}
}
