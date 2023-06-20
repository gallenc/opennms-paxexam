/*
 * Copyright 2009 Toni Menzel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opennms.paxexam.container;

import org.opennms.paxexam.container.options.RBCLookupTimeoutOption;
import org.opennms.paxexam.container.options.RBCPortOption;

/**
 * @author Toni Menzel
 * @since Jan 25, 2010
 */
public class OpenNMSRBCRemoteTargetOptions {
    
    /** Hidden utility class constructor. */
    private OpenNMSRBCRemoteTargetOptions() {
    }

    /**
     * Creates a {@link org.opennms.paxexam.container.options.RBCLookupTimeoutOption}.
     * 
     * @param timeoutInMillis
     *            timeout in millis
     * 
     * @return timeout option
     */
    public static RBCLookupTimeoutOption waitForRBCFor(final Integer timeoutInMillis) {
        return new RBCLookupTimeoutOption(timeoutInMillis);
    }

    /**
     * Creates a {@link org.opennms.paxexam.container.options.RBCLookupTimeoutOption}.
     * 
     * @param host
     *            to use for rmi rgistry
     * @param port
     *            port where the rbc server is listening at
     * 
     * @return port option
     */
    public static RBCPortOption location(String host, final Integer port) {
        return new RBCPortOption(host, port);
    }
}
