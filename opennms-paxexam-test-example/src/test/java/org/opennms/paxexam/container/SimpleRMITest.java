package org.opennms.paxexam.container;

import static org.junit.Assert.*;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
//import org.ops4j.pax.swissbox.tracker.ServiceLookup;
import org.ops4j.pax.exam.rbc.internal.RemoteBundleContext;
import org.ops4j.pax.exam.rbc.internal.RemoteBundleContextImpl;
import org.ops4j.pax.exam.RelativeTimeout;

import org.junit.Test;

public class SimpleRMITest {

	//TODO RESTORE TEST
//	@Test
	public void test1() {

		BundleContext bc = new BundleContext() {

			@Override
			public String getProperty(String key) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle getBundle() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle installBundle(String location, InputStream input) throws BundleException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle installBundle(String location) throws BundleException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle getBundle(long id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle[] getBundles() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void addServiceListener(ServiceListener listener, String filter) throws InvalidSyntaxException {
				// TODO Auto-generated method stub

			}

			@Override
			public void addServiceListener(ServiceListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeServiceListener(ServiceListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addBundleListener(BundleListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeBundleListener(BundleListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addFrameworkListener(FrameworkListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeFrameworkListener(FrameworkListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public ServiceRegistration<?> registerService(String[] clazzes, Object service,
					Dictionary<String, ?> properties) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceRegistration<?> registerService(String clazz, Object service,
					Dictionary<String, ?> properties) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <S> ServiceRegistration<S> registerService(Class<S> clazz, S service,
					Dictionary<String, ?> properties) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <S> ServiceRegistration<S> registerService(Class<S> clazz, ServiceFactory<S> factory,
					Dictionary<String, ?> properties) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceReference<?>[] getServiceReferences(String clazz, String filter)
					throws InvalidSyntaxException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceReference<?>[] getAllServiceReferences(String clazz, String filter)
					throws InvalidSyntaxException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceReference<?> getServiceReference(String clazz) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <S> ServiceReference<S> getServiceReference(Class<S> clazz) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <S> Collection<ServiceReference<S>> getServiceReferences(Class<S> clazz, String filter)
					throws InvalidSyntaxException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <S> S getService(ServiceReference<S> reference) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean ungetService(ServiceReference<?> reference) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public <S> ServiceObjects<S> getServiceObjects(ServiceReference<S> reference) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public File getDataFile(String filename) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Filter createFilter(String filter) throws InvalidSyntaxException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle getBundle(String location) {
				// TODO Auto-generated method stub
				return null;
			}
		};

		RemoteBundleContext rbc = new RemoteBundleContextImpl(bc);
		Remote remoteStub;
		Registry registry1 = null;
		try {
			String name = "test";
			registry1 = LocateRegistry.createRegistry(55556);

			remoteStub = UnicastRemoteObject.exportObject(rbc, 0);
			registry1.rebind(name, remoteStub);

			RemoteBundleContext rbc2 = (RemoteBundleContext) registry1.lookup("test");
			if (rbc2 != null)
				System.out.println("rbc2 name: " + name + "\n obj class: " + rbc2.getClass().getName()
						+ "\n obj.toString():  " + rbc2.toString());

			List<String> registryNames = Arrays.asList(registry1.list());
			System.out.println("list of registry names: " + registryNames.size());
			for (String name2 : registryNames) {
				Object obj = registry1.lookup(name2);
				System.out.println("name: " + name2);
				if (obj != null)
					System.out.println("   name: " + name2 + "\n obj class: " + obj.getClass().getName()
							+ "\n obj.toString():  " + obj.toString());
			}

			UnicastRemoteObject.unexportObject(rbc, true);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (registry1 != null)
				try {
					UnicastRemoteObject.unexportObject(registry1, true);
				} catch (NoSuchObjectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}
	//TODO RESTORE TEST
//	@Test
	public void simpletest() {
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry("127.0.0.1", 55555);

			// Registry registry = LocateRegistry.getRegistry("localhost", 44444);
			assertNotNull(registry);

			System.out.println("simple test registry names:");
			List<String> registryNames = Arrays.asList(registry.list());
			for (String name : registryNames) {

				Object obj = registry.lookup(name);
				System.out.println("name: " + name);
				if (obj != null)
					System.out.println("   name: " + name + "\n obj class: " + obj.getClass().getName()
							+ "\n obj.toString():  " + obj.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
//TODO RESTORE TEST
//	@Test
	public void test() {
		try {

			// simple test
			URL location1 = getClass().getProtectionDomain().getCodeSource().getLocation();
			URL location2 = Bundle.class.getProtectionDomain().getCodeSource().getLocation();
			//URL location3 = ServiceLookup.class.getProtectionDomain().getCodeSource().getLocation();
			URL location3=null;
			URL location4 = RemoteBundleContext.class.getProtectionDomain().getCodeSource().getLocation();
			URL location5 = RelativeTimeout.class.getProtectionDomain().getCodeSource().getLocation();
			// System.setProperty( "java.rmi.server.codebase", location1 + " " + location2 +
			// " " + location3 );
			System.out.println("java.rmi.server.codebase \n" + location1 + " \n" + location2 + " \n" + location3 + " \n"
					+ location4 + " \n" + location5);

			Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
			// Registry registry = LocateRegistry.getRegistry("localhost", 44444);
			assertNotNull(registry);

			System.out.println("registry names:");
			List<String> registryNames = Arrays.asList(registry.list());
			for (String name : registryNames) {

				Object obj = registry.lookup(name);
				System.out.println("name: " + name);
				if (obj != null)
					System.out.println("   name: " + name + "\n obj class: " + obj.getClass().getName()
							+ "\n obj.toString():  " + obj.toString());
			}

			Object obj = registry.lookup("PaxExam");
			if (obj != null) {
				System.out.println("\n PaxExam obj.toString():  " + obj.toString());
			} else
				System.out.println("null object");
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
