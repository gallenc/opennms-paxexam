package org.opennms.integrate.paxexam.rmitestserver;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;

public class PaxexamRMIServerSocketFactory extends RMISocketFactory implements RMIServerSocketFactory, RMIClientSocketFactory, Serializable {

    private static final long serialVersionUID = 2980115395787642955L;

    private String m_host;

    public PaxexamRMIServerSocketFactory() {
    }

    public PaxexamRMIServerSocketFactory(final String host) {
        m_host = host;
    }

    @Override
    public ServerSocket createServerSocket(final int port) throws IOException {
        final InetAddress address;
        if ("localhost".equals(m_host)) {
            address = InetAddress.getLoopbackAddress();
        } else {
            address = InetAddress.getByName(m_host);
        }
        return new ServerSocket(port, -1, address);
    }

    @Override
    public Socket createSocket(final String host, final int port) throws IOException {
        return RMISocketFactory.getDefaultSocketFactory().createSocket(host, port);
    }

    public String getHost() {
        return m_host;
    }
    public void setHost(final String host) {
        m_host = host;
    }
    
    public boolean equals (Object that) {
    	return that !=null && that.getClass() == this.getClass();
    }
}