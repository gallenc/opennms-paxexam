
package org.opennms.paxexam.container;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Stack;

import org.ops4j.io.StreamUtils;
import org.ops4j.pax.exam.ProbeInvoker;
import org.ops4j.pax.exam.RelativeTimeout;
import org.ops4j.pax.exam.TestAddress;
import org.ops4j.pax.exam.rbc.client.RemoteBundleContextClient;
import org.ops4j.pax.exam.rbc.internal.NoSuchServiceException;
import org.ops4j.pax.exam.rbc.internal.RemoteBundleContext;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RemoteBundleContextClientImplExtended implements RemoteBundleContextClient {

    /**
     * JCL logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RemoteBundleContextClient.class);

    // TODO duplicate
    public static final String PROBE_SIGNATURE_KEY = "Probe-Signature";

    private RemoteBundleContext remoteBundleContext;

    /**
     * Timeout for looking up the remote bundle context via RMI.
     */
    private final RelativeTimeout rmiLookupTimeout;

    private final Integer registry;

    private final Stack<Long> installed;

    private final String name;

    /**
     * Constructor.
     *
     * @param name
     *            of container
     * @param registry
     *            RMI registry to look at
     * @param timeout
     *            timeout for looking up the remote bundle context via RMI (cannot be null)
     */
    public RemoteBundleContextClientImplExtended(final String name, final Integer registry,
        final RelativeTimeout timeout) {
        assert registry != null : "registry should not be null";

        this.registry = registry;
        this.name = name;
        rmiLookupTimeout = timeout;
        installed = new Stack<>();
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(final Class<T> serviceType, final String filter,
        final RelativeTimeout timeout) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class<?>[] { serviceType }, new InvocationHandler() {

                /**
                 * Delegates the call to remote bundle context.
                 */
                @Override
                public Object invoke(final Object proxy, final Method method, final Object[] params) {
                    try {
                        return getRemoteBundleContext().remoteCall(method.getDeclaringClass(),
                            method.getName(), method.getParameterTypes(), filter, timeout, params);
                    }
                    catch (InvocationTargetException e) {
                        throw new RuntimeException(e.getCause());
                    }
                    catch (RemoteException e) {
                        throw new RuntimeException("Remote exception", e);
                    }
                    catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                    catch (NoSuchServiceException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
    }

    @Override
    public long install(String location, InputStream stream) {
    	LOG.debug("RBC Client installing location="+location);
        // turn this into a local url because we don't want pass the stream any further.
        try {
            // URI location = store.getLocation( store.store( stream ) );
            // pack as bytecode
            byte[] packed = pack(stream);

            long id = getRemoteBundleContext().installBundle(location, packed);
            installed.push(id);
            LOG.debug("RBC Client starting bundle id="+id);
            getRemoteBundleContext().startBundle(id);
            return id;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (BundleException e) {
            throw new RuntimeException("Bundle cannot be installed", e);
        }
    }

    private byte[] pack(InputStream stream) {
        LOG.debug("Packing probe into memory for true RMI. Hopefully things will fill in..");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            StreamUtils.copyStream(stream, out, true);
        }
        catch (IOException e) {

        }
        return out.toByteArray();
    }

    @Override
    public void cleanup() {
    	
    	//TODO REMOVE WAIT
    	try {
    		LOG.info("RBC Client cleanup wait 10000");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
    	LOG.info("RBC Client end of cleanup wait");
    	
        try {
            while (!installed.isEmpty()) {
                Long id = installed.pop();
                LOG.debug("RBC Client cleaning up - uninstall bundleid="+id);
                getRemoteBundleContext().uninstallBundle(id);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (BundleException e) {
            throw new RuntimeException("Bundle cannot be uninstalled", e);
        }

    }

    @Override
    public void setBundleStartLevel(final long bundleId, final int startLevel) {
    	LOG.debug("RBC Client set bundle startLevel="+startLevel+ " bundleid="+bundleId);
        try {
            getRemoteBundleContext().setBundleStartLevel(bundleId, startLevel);
        }
        catch (RemoteException e) {
            throw new RuntimeException("Remote exception", e);
        }
        catch (BundleException e) {
            throw new RuntimeException("Start level cannot be set", e);
        }
    }

    @Override
    public void start() {
    	LOG.debug("RBC Client start system bundle");
        try {
            getRemoteBundleContext().startBundle(0);
        }
        catch (RemoteException e) {
            throw new RuntimeException("Remote exception", e);
        }
        catch (BundleException e) {
            throw new RuntimeException("System bundle cannot be started", e);
        }
    }

    @Override
    public void stop() {
    	LOG.debug("RBC Client stop system bundle");
        try {
            getRemoteBundleContext().stopBundle(0);

        }
        catch (RemoteException e) {
            // If its gone, its gone. Cannot do much about it anyway.
            // throw new RuntimeException( "Remote exception", e );
        }
        catch (BundleException e) {
            throw new RuntimeException("System bundle cannot be stopped", e);
        }
    }

    @Override
    public void waitForState(final long bundleId, final int state, final RelativeTimeout timeout) {
    	LOG.debug("RBC Client waiting for state bundleId="+bundleId	+ " state="+state+ " timeout="+timeout);
        try {
            getRemoteBundleContext().waitForState(bundleId, state, timeout);
        }
        catch (RemoteException e) {
            throw new RuntimeException("waitForState", e);
        }
        catch (BundleException e) {
            throw new RuntimeException("waitForState", e);
        }
    }

    /**
     * Looks up the {@link RemoteBundleContext} via RMI. The lookup will timeout in the specified
     * number of millis.
     *
     * @return remote bundle context
     */
    private synchronized RemoteBundleContext getRemoteBundleContext() {
        if (remoteBundleContext == null) {

            // !! Absolutely necesary for RMI class loading to work
            // TODO maybe use ContextClassLoaderUtils.doWithClassLoader
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            LOG.info("Waiting for remote bundle context.. on " + registry + " name: " + name
                + " timout: " + rmiLookupTimeout);
            // TODO create registry here
            Throwable reason = null;
            long startedTrying = System.currentTimeMillis();

            do {
                try {
                    remoteBundleContext = (RemoteBundleContext) getRegistry(registry).lookup(name);
                }
                catch (RemoteException e) {
                    reason = e;
                }
                catch (NotBoundException e) {
                    reason = e;
                }
            }
            while (remoteBundleContext == null
                && (rmiLookupTimeout.isNoTimeout() || System.currentTimeMillis() < startedTrying
                    + rmiLookupTimeout.getValue()));
            if (remoteBundleContext == null) {
                throw new RuntimeException("Cannot get the remote bundle context", reason);
            }
            LOG.debug("Remote bundle context found after "
                + (System.currentTimeMillis() - startedTrying) + " millis");
        }
        return remoteBundleContext;

    }

    // TODO This utility is copy/pasted in pax-exam-container-forked's
    // ForkedFrameworkFactory, and ideally perhaps should be be put into a
    // shared utility module
    private Registry getRegistry(int port) throws RemoteException {
        Registry reg;
        String hostName = System.getProperty("java.rmi.server.hostname");
        if (hostName != null && !hostName.isEmpty()) {
            reg = LocateRegistry.getRegistry(hostName, port);
        }
        else {
            reg = LocateRegistry.getRegistry(port);
        }
        return reg;
    }

    @Override
    public void call(TestAddress address) {
    	// see ForkedTestContainer.java
    	//String filterExpression = "(&(objectClass=org.ops4j.pax.exam.ProbeInvoker)(Probe-Signature="+ address.root().identifier() + "))";
    	
        String filterExpression = "(" + PROBE_SIGNATURE_KEY + "=" + address.root().identifier() + ")";
        LOG.debug("RBC Client call address="+address+ " filterExpression="+filterExpression);
        ProbeInvoker service = getService(ProbeInvoker.class, filterExpression, rmiLookupTimeout);
        service.call(address.arguments());
    }

    public String getName() {
        return name;
    }

    @Override
    public void uninstall(long bundleId) {
    	LOG.debug("RBC Client uninstalling bundle "+bundleId);

        try {
            getRemoteBundleContext().uninstallBundle(bundleId);
        }
        catch (RemoteException exc) {
            throw new RuntimeException(exc);
        }
        catch (BundleException exc) {
            throw new RuntimeException(exc);
        }
    }
}
