package org.opennms.example.paxexam.paxexamtest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// see https://stackoverflow.com/questions/33921107/pax-exam-execute-command-against-karaf
// see also https://github.com/ANierbeck/Karaf-Cassandra/blob/master/Karaf-Cassandra-ITest/src/test/java/de/nierbeck/cassandra/itest/TestBase.java

@RunWith(PaxExam.class)
@ExamFactory(org.opennms.paxexam.container.OpenNMSPluginTestContainerFactory.class)
public class CommandTest extends TestBase {
	private static Logger LOG = LoggerFactory.getLogger(CommandTest.class);

	@Test
	public void testProvisioning() throws Exception {
		LOG.error("***************** TRYING TO RUN THE TEST");
		LOG.error(executeCommand("bundle:list"));

	}

}
