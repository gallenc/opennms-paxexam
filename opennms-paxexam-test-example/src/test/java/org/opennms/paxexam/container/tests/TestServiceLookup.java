package org.opennms.paxexam.container.tests;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.ops4j.pax.exam.ProbeInvoker;
import org.ops4j.pax.exam.RelativeTimeout;
import org.ops4j.pax.exam.TestAddress;
import org.ops4j.pax.exam.rbc.client.RemoteBundleContextClient;
import org.ops4j.pax.exam.rbc.client.intern.RemoteBundleContextClientImpl;
import org.ops4j.pax.exam.rbc.internal.NoSuchServiceException;
import org.ops4j.pax.exam.rbc.internal.RemoteBundleContext;
import org.ops4j.pax.exam.spi.intern.DefaultTestAddress;

public class TestServiceLookup {

	@Test
	public void test() {
		System.out.println("TestServiceLookup");

		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry("127.0.0.1", 55555);
			//registry = LocateRegistry.getRegistry("::1", 55555);

			// Registry registry = LocateRegistry.getRegistry("localhost", 44444);
			assertNotNull(registry);

			System.out.println("simple test registry names:");
			String[] regList = registry.list();
			System.out.println("number of registry names:"+regList.length);
			List<String> registryNames = Arrays.asList(regList);
			for (String name : registryNames) {

				Object obj = registry.lookup(name);
				System.out.println("name: " + name);
				if (obj != null)
					System.out.println("   name: " + name + "\n obj class: " + obj.getClass().getName()
							+ "\n obj.toString():  " + obj.toString());
			}

			RemoteBundleContext remoteBundleContext = (RemoteBundleContext) registry.lookup("PaxExam");
			assertNotNull(remoteBundleContext);

			System.out.println("trying to access rbc client");
			String name = "PaxExam";

			Integer registryPort = 55555;
			RelativeTimeout timeout = new RelativeTimeout(20000);

			RemoteBundleContextClientImplExtended rbcClient = new RemoteBundleContextClientImplExtended(name,
					registryPort, timeout);
			
			System.out.println("rbcClient="+rbcClient);

			// TestAddress testAddress = new DefaultTestAddress("testAddress", "arg1");

			try {
				System.out.println("trying to invoke rbcClient=");
				String testAddressRootIdentifier = "PaxExam-8612631b-14e2-4c2a-b333-9ed480ef87e0";

				String filterExpression = "(" + rbcClient.PROBE_SIGNATURE_KEY + "=" + testAddressRootIdentifier + ")";
				ProbeInvoker service = rbcClient.getService(ProbeInvoker.class, filterExpression, timeout);
				System.out.println("   service: " + service);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// TestAddress testAddress = new DefaultTestAddress("testAddress", "arg1");
			// rbcClient.call(testAddress );

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
