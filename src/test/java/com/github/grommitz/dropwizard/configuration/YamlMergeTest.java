package com.github.grommitz.dropwizard.configuration;

//import com.envisional.common.FileUtil;
import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class YamlMergeTest {

	// just load the base config. the polymorphic employee property loads ok.
	@Test
	public void loadTest() throws IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ObjectReader objectReader = mapper.readerFor(Config.class);
		Config myConfig = objectReader.readValue(new File("src/test/resources/yml/base.yml"));

		System.out.println(mapper.writerFor(Config.class).withDefaultPrettyPrinter().writeValueAsString(myConfig));

		assertThat(myConfig.getSchool().getName(), is("The Comp"));
		assertThat(myConfig.getSchool().getLocation(), is("Alsager"));
		assertThat(myConfig.getSchool().getEmployee().getName(), is("Mr Andrews"));
		assertThat(myConfig.getSchool().getEmployee().getAge(), is(60));
	}

	// try to override the base YML. fails due to the polymorphic employee field.
	@Test
	void polymorphicMergeTest() throws IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		Config myConfig = new Config();
		ObjectReader objectReader = mapper.readerForUpdating(myConfig);
		//FileUtil.readResourceTextFile("")
		objectReader.readValue(new File("src/test/resources/yml/base.yml"));
		objectReader.readValue(new File("src/test/resources/yml/override.yml"));

		System.out.println(mapper.writerFor(Config.class).withDefaultPrettyPrinter().writeValueAsString(myConfig));

		assertThat(myConfig.getSchool().getName(), is("The Comp"));
		assertThat(myConfig.getSchool().getLocation(), is("Sandbach"));
		assertThat(myConfig.getSchool().getEmployee().getName(), is("Mrs Catchpole"));
		assertThat(myConfig.getSchool().getEmployee().getAge(), is(31));

	}

	public static class Config {
		@JsonMerge
		private School school;

		public School getSchool() {
			return school;
		}
	}

	public static class School {
		@JsonMerge
		private String name;

		@JsonMerge
		private String location;

		@JsonMerge
		private Employee employee;

		public String getName() {
			return name;
		}

		public String getLocation() {
			return location;
		}

		public Employee getEmployee() {
			return employee;
		}
	}

	@JsonTypeInfo(use= JsonTypeInfo.Id.NAME, property = "type")
	@JsonSubTypes({
			@JsonSubTypes.Type(value = Headmaster.class),
			@JsonSubTypes.Type(value = Teacher.class)
	})
	public static interface Employee {
		String getName();
		int getAge();
	}

	@JsonTypeName("head")
	public static class Headmaster implements Employee {
		@JsonMerge
		private String name;

		@JsonMerge
		private int age;

		public String getName() {
			return name;
		}

		public int getAge() {
			return age;
		}
	}

	@JsonTypeName("teacher")
	public static class Teacher implements Employee {
		@JsonMerge
		private String name;

		@JsonMerge
		private String subject;

		@JsonMerge
		private int age;

		public String getName() {
			return name;
		}

		public int getAge() {
			return age;
		}

		public String getSubject() {
			return subject;
		}
	}


}
