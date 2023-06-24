/*
 * Copyright 2023 Craig Gallen, OpenNMS Group
 * Copyright 2009 Toni Menzel.
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

package org.opennms.paxexam.container;

import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.RelativeTimeout;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.TestContainerFactory;
import org.ops4j.pax.exam.container.remote.RBCRemoteContainer;
import org.ops4j.pax.exam.container.remote.RBCRemoteTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opennms.paxexam.container.options.RBCLookupTimeoutOption;
import org.opennms.paxexam.container.options.RBCPortOption;

import java.util.List;
import java.util.stream.Collectors;
import java.rmi.RMISecurityManager;
import java.security.Policy;
import java.util.ArrayList;

public class OpenNMSPluginTestContainerFactory implements TestContainerFactory {

	private static final Logger LOG = LoggerFactory.getLogger(OpenNMSPluginTestContainerFactory.class);

	/**
	 * System property key for RMI registry port.
	 */
	public static final String RMI_PORT_KEY = "pax.swissbox.framework.rmi.port";

	/**
	 * System property key for the name to be used for the remote framework in the
	 * RMI registry.
	 */
	public static final String RMI_NAME_KEY = "pax.swissbox.framework.rmi.name";

	/**
	 * System property key for the framework shutdown timeout (milliseconds in
	 * decimal representation).
	 */
	public static final String TIMEOUT_KEY = "pax.swissbox.framework.timeout";

	/**
	 * hostname for the remote osgi server being tested
	 */
	public static final String HOSTNAME_KEY = "java.rmi.server.hostname";

	@Override
	public TestContainer[] create(final ExamSystem system) {

		RBCPortOption portOption = system.getSingleOption(RBCPortOption.class);
		RBCLookupTimeoutOption timeoutOption = system.getSingleOption(RBCLookupTimeoutOption.class);

		// default values
		String hostname = "localhost";
		Integer port = 55555;
		RelativeTimeout timeout = new RelativeTimeout(10000);

		if (portOption == null) {
			LOG.warn("portOption not set in test so using default values");
		} else {
			hostname = portOption.getHost();
			port = portOption.getPort();
		}

		if (timeoutOption == null) {
			LOG.warn("timeOutOption not set so using default values");
		} else {
			timeout = new RelativeTimeout(timeoutOption.getTimeout());
		}
		
		String name = "PaxExam";
		
		LOG.warn("creating test container configuration : "
				+ "  portOption.getHost() ("+ HOSTNAME_KEY+") = "+hostname
		        + "  portOption.getPort() ("+ RMI_PORT_KEY + ") = " + port 
				+ "  remote bundle context name (" + RMI_NAME_KEY+") = "+name
				+ "  timeOutOption = (" + TIMEOUT_KEY + ") = " + timeout
				);

		Policy.setPolicy(new AllPolicy());

		System.setSecurityManager(new RMISecurityManager());

//        URL location1 = getClass().getProtectionDomain().getCodeSource().getLocation();
//        URL location2 = Bundle.class.getProtectionDomain().getCodeSource().getLocation();
//        URL location3 = ServiceLookup.class.getProtectionDomain().getCodeSource().getLocation();
//        System.setProperty( "java.rmi.server.codebase", location1 + " " + location2 + " "
//                + location3 );



		//System.setProperty("java.rmi.server.hostname", "localhost");

		// String name = "karaf-root"; // karaf-root

		//Integer registry = 55555; // is actually the port
		// Integer registry =44444; // is actually the port
		//RelativeTimeout timeout = new RelativeTimeout(10000);
		
		List<TestContainer> containers = new ArrayList<TestContainer>();

		TestContainer container = new RBCRemoteTargetExtended(name, port, timeout, hostname);

		containers.add(container);

		return containers.toArray(new TestContainer[containers.size()]);
	}

}
