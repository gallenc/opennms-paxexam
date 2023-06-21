/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opennms.integration.paxexam.rmitestserver;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URL;
import org.osgi.framework.Bundle;
import org.ops4j.pax.swissbox.framework.RemoteFrameworkImpl;
import org.ops4j.pax.swissbox.tracker.ServiceLookup;
import org.slf4j.MDC;

public class Activator implements BundleActivator {
	private static Logger LOG = LoggerFactory.getLogger(Activator.class);

	public static final String PREFIX_KEY = "prefix";
	private static final String LOGGING_PREFIX = "onms-paxexam";

	private Registry paxexamRmiRegistry = null;
	private PaxexamRMIServerSocketFactory paxexamRMIServerSocketFactory = null;

	// org.ops4j.pax.exam.rbc.rmi.host= localhost
	// org.ops4j.pax.exam.rbc.rmi.name= PaxExam
	// org.ops4j.pax.exam.rbc.rmi.port= 1099

	// same as org.ops4j.pax.exam.rbc.Constants
	public static final String PAX_RMI_PORT_PROPERTY = "org.ops4j.pax.exam.rbc.rmi.port";
	public static final String PAX_RMI_HOST_PROPERTY = "org.ops4j.pax.exam.rbc.rmi.host";
	public static final String PAX_RMI_NAME_PROPERTY = "org.ops4j.pax.exam.rbc.rmi.name";

	public void start(BundleContext context) {
		// setting up separate logging for paxexam - note probably doesn't work in Activator under felix as using jul logging
		Map<String, String> originalMDC = MDC.getCopyOfContextMap();
		MDC.put(PREFIX_KEY, LOGGING_PREFIX);

		String portStr = System.getProperty(PAX_RMI_PORT_PROPERTY);
		String hostStr = System.getProperty(PAX_RMI_HOST_PROPERTY);
		String nameStr = System.getProperty(PAX_RMI_NAME_PROPERTY);

		try {

			// TODO MAKE INFO
			LOG.warn("Starting paxexam test RMI server: PAX_RMI_HOST_PROPERTY="+hostStr
					+ " PAX_RMI_NAME_PROPERTY="+nameStr
					+ " PAX_RMI_PORT_PROPERTY=" + portStr);

			Integer port = Integer.valueOf(portStr);
			
			// paxexamRMIServerSocketFactory = new PaxexamRMIServerSocketFactory(hostStr);
			// setting 0.0.0.0 allows loopback
			paxexamRMIServerSocketFactory = new PaxexamRMIServerSocketFactory("0.0.0.0");
			
			paxexamRmiRegistry = LocateRegistry.createRegistry(port, paxexamRMIServerSocketFactory, paxexamRMIServerSocketFactory);
			LOG.warn("new registry Created " + paxexamRmiRegistry);

		} catch (Exception e) {
			LOG.error("problem creating paxexamRmiRegistry port=" + portStr, e);
		} finally {
			MDC.setContextMap(originalMDC);
		}

	}

	public void stop(BundleContext context) {
		// setting up separate logging for paxexam - note probably doesn't work in Activator under felix
		Map<String, String> originalMDC = MDC.getCopyOfContextMap();
		MDC.put(PREFIX_KEY, LOGGING_PREFIX);
		
		try {
			LOG.warn("Stopping paxexam test RMI server");
			if (paxexamRmiRegistry != null)
				try {
					UnicastRemoteObject.unexportObject(paxexamRmiRegistry, true);
				} catch (NoSuchObjectException e) {
					LOG.error("problem unexporting paxexamRmiRegistry", e);
				}

		} finally {
			MDC.setContextMap(originalMDC);
		}
	}

}