package org.opennms.paxexam.container.main;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

// to run this test in container
//java -cp opennms-paxexam-rmi-server-0.0.1-SNAPSHOT.jar:opennms-paxexam-container-remote-0.0.1-SNAPSHOT.jar:pax-exam-container-rbc-onms-4.13.5.jar:pax-exam-4.13.5.jar:org.osgi.core-6.0.0.jar org.opennms.paxexam.container.main.TestRMILookup localhost 55555

public class TestRMILookup {
	
	  public static void main(String[] args) {
		    TestRMILookup test = new TestRMILookup();
		    
		    String address ="127.0.0.1";
		    String port= "55555";

		    if(args.length!=2) {
		    	System.out.println("arguments not supplied using address="+address+ " port="+port);
		    } else {
		    	 address = args[0];
				  port= args[1];
				 System.out.println("using arguments address="+address+ " port="+port);
		    }
		   
		    test.test(address, port);
		  }


	public void test(String address, String port ) {
		System.out.println("TestServiceLookup");

		Registry registry = null;
		try {
			
			int portno = Integer.parseInt(port);
			
			registry = LocateRegistry.getRegistry(address, portno);
			//registry = LocateRegistry.getRegistry("::1", 55555);

			System.out.println("registry:"+registry);

			System.out.println("simple test registry names:");
			String[] regList = registry.list();
			System.out.println("number of registry names:"+regList.length);
			List<String> registryNames = Arrays.asList(regList);
			for (String name : registryNames) {
				System.out.println("name: " + name);
				Object obj = registry.lookup(name);
				if (obj != null)
					System.out.println("   name: " + name + "\n obj class: " + obj.getClass().getName()
							+ "\n obj.toString():  " + obj.toString());
			}



		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
