package org.opennms.paxexam.container.tests;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

//  java -cp ./paxexamtest-remote-0.0.1-SNAPSHOT.jar org.opennms.paxexam.container.tests.TestRMILookup horizon 55555
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

}
