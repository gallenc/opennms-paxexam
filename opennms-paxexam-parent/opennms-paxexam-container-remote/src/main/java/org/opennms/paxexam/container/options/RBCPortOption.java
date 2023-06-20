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
package org.opennms.paxexam.container.options;

import org.ops4j.pax.exam.Option;

/**
 * @author Toni Menzel
 * @since Jan 25, 2010
 */
public class RBCPortOption implements Option {

    private Integer port;
    private String host;

    public RBCPortOption(String host, Integer port) {
        assert host != null : "Host should never be null.";
        assert port != null : "Port should never be null.";

        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

	@Override
	public String toString() {
		return "RBCPortOption [port=" + port + ", host=" + host + "]";
	}
    
}
