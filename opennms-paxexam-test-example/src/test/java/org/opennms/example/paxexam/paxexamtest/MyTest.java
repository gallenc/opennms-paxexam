package org.opennms.example.paxexam.paxexamtest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.bootClasspathLibrary;
import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.container.remote.RBCRemoteTargetOptions;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.ops4j.pax.exam.util.PathUtils;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(PaxExam.class)
//@ExamFactory(org.ops4j.pax.exam.container.remote.RBCRemoteContainerFactory.class)
@ExamFactory(org.opennms.paxexam.container.OpenNMSPluginTestContainerFactory.class)

//@ExamReactorStrategy(PerSuite.class)
@ExamReactorStrategy(PerClass.class)
//@ExamReactorStrategy(PerMethod.class)
public class MyTest {
	private static Logger LOG = LoggerFactory.getLogger(MyTest.class);

	//  remove comments  if you want to slow things down
//	{
//		System.out.println("****  bundle sleeping 20000 ms");
//		try {
//			Thread.sleep(20000);
//		} catch (InterruptedException e) {
//			System.out.println("****  bundle awake ");
//		}
//	}

	@Inject
	private BundleContext bc;

	@Configuration
	public Option[] config() {
		return options(systemProperty("log4j.configurationFile").value("file:src/test/resources/log4j2.xml"),
				
				//RBCRemoteTargetOptions.location("localhost", 1234),

				mavenBundle("org.slf4j", "slf4j-api", "1.7.4"),
				mavenBundle().artifactId("pax-logging-log4j2").groupId("org.ops4j.pax.logging").version("2.0.14"),
				mavenBundle().artifactId("pax-logging-api").groupId("org.ops4j.pax.logging").version("2.0.14"),
//            mavenBundle("org.apache.logging.log4j", "log4j-api", "2.11.1"),
//            mavenBundle("org.apache.logging.log4j", "log4j-core", "2.11.1"),
//            mavenBundle("org.apache.logging.log4j", "log4j-slf4j-impl", "2.11.1"),
				junitBundles(),

		// to run forked needs to be on classpath
		// not needed if native as already on classpath
           bootClasspathLibrary("mvn:org.slf4j/slf4j-api/1.7.4"),
           
           bootClasspathLibrary("mvn:org.ops4j.pax.logging/pax-logging-log4j2/2.0.14"),
           bootClasspathLibrary("mvn:org.ops4j.pax.logging/pax-logging-api/2.0.14")
           
        //   bootClasspathLibrary("mvn:org.apache.logging.log4j/log4j-api/2.11.1"),
        //   bootClasspathLibrary("mvn:org.apache.logging.log4j/log4j-core/2.11.1"),
        //   bootClasspathLibrary("mvn:org.apache.logging.log4j/log4j-slf4j-impl/2.11.1")

		);
	}

	
	@Test
	public void printOutData() {
		System.out.println("****  bundle printing test data");
	}
	
	@Test
	public void shouldHaveBundleContext() {
		LOG.error("**** make sure we can log error");
		LOG.info("**** make sure we can log info ");
		LOG.debug("**** make sure we can log debug");
		assertThat(bc, is(notNullValue()));
	}

}
