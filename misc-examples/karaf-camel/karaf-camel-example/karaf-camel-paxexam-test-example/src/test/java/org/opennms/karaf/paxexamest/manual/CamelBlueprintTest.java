/*
 *    Copyright 2023 Craig Gallen, OpenNMS Group
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.opennms.karaf.paxexamest.manual;

import static org.junit.Assert.*;

import org.junit.After;
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
public class CamelBlueprintTest extends TestBase {
	private static Logger LOG = LoggerFactory.getLogger(CamelBlueprintTest.class);

	public String TEST_FEATURE_REPO = "mvn:org.opennms.karaf.examples/karaf-camel-example-features/0.0.1-SNAPSHOT/xml";
	
	//public String TEST_FEATURE_NAME = "karaf-camel-example-java";
	
	public String TEST_FEATURE_NAME = "karaf-camel-example-blueprint";

	@Test
	public void testProvisioning() throws Exception {
		LOG.warn("***************** TRYING TO INSTALL AND TEST "+TEST_FEATURE_NAME);
		
	   
		LOG.warn("***************** INSTALLING REPO "+TEST_FEATURE_REPO);
		LOG.warn(executeCommand("feature:repo-add "+TEST_FEATURE_REPO));

		LOG.warn("***************** INSTALLING FEATURE "+TEST_FEATURE_NAME);
		
		LOG.warn(executeCommand("feature:install "+TEST_FEATURE_NAME));

		LOG.warn("***************** FINISHED INSTALLING FEATURE "+TEST_FEATURE_NAME);
		
		LOG.warn("***************** TRYING TO RUN BUNDLE LIST COMMAND TEST");
		//LOG.warn(executeCommand("bundle:list"));
		LOG.warn("***************** END OF BUNDLE LIST COMMAND TEST");

	}

	
	@After
	public void removeFeature() throws Exception{
		
		LOG.warn("***************** STATUS TEST FEATURE "+TEST_FEATURE_NAME);
		
		LOG.warn(executeCommand("feature:list | grep "+TEST_FEATURE_NAME));
		
		LOG.warn("***************** STOP  TEST FEATURE "+TEST_FEATURE_NAME);
		LOG.warn(executeCommand("feature:stop "+TEST_FEATURE_NAME));
		
		LOG.warn(executeCommand("feature:list | grep "+TEST_FEATURE_NAME));
		
		LOG.warn("***************** UNINSTALL  TEST FEATURE "+TEST_FEATURE_NAME);
		LOG.warn(executeCommand("feature:uninstall "+TEST_FEATURE_NAME));
		
		LOG.warn(executeCommand("feature:list | grep "+TEST_FEATURE_NAME));

		LOG.warn("***************** FINISHED UNINSTALLING TEST FEATURE "+TEST_FEATURE_NAME);
		
		LOG.warn("***************** REMOVING TEST REPOSITORY "+TEST_FEATURE_REPO);

		LOG.warn(executeCommand("feature:repo-list | grep "+TEST_FEATURE_REPO));

		LOG.warn(executeCommand("feature:repo-remove "+TEST_FEATURE_REPO));

		LOG.warn(executeCommand("feature:repo-list | grep "+TEST_FEATURE_REPO));

		LOG.warn("***************** REMOVED TEST REPOSITORY "+TEST_FEATURE_REPO);
		
	}

}
