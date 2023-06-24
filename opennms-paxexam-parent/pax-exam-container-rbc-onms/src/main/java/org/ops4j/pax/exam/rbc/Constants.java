/*
 * Copyright 2008 Alin Dreghiciu.
 * Copyright 2023 Craig Gallen, OpenNMS Group
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
package org.ops4j.pax.exam.rbc;

/**
 * Static constants related to remote bundle context.
 * 
 * @author Alin Dreghiciu (adreghiciu@gmail.com)
 * @since 0.3.0, December 11, 2008
 */
public class Constants {

    /**
     * Name of the system (framework) property that specifies the RMI registry port.
     */
    public static final String RMI_PORT_PROPERTY = "org.ops4j.pax.exam.rbc.rmi.port";

    public static final String RMI_HOST_PROPERTY = "org.ops4j.pax.exam.rbc.rmi.host";
    public static final String RMI_NAME_PROPERTY = "org.ops4j.pax.exam.rbc.rmi.name";

    public static final long WAIT_FOREVER = 0;
    
    /** Hidden utility class constructor. */
    private Constants() {
    }
}
