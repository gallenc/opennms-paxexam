/*
 * Copyright 2023 Craig Gallen, OpenNMS Group
 * Copyright 2009 Toni Menzel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opennms.paxexam.container;

import static org.ops4j.pax.exam.OptionUtils.filter;

import org.opennms.paxexam.container.options.RBCLookupTimeoutOption;
import org.opennms.paxexam.container.options.RBCPortOption;
import org.ops4j.pax.exam.Option;

/**
 * Minimal parser for the rbcremote fragment.
 */
public class Parser {

    private String host;

    private Integer port;

    private long timeout;

    public Parser(Option[] options) {
        extractArguments(filter(RBCPortOption.class, options));
        extractArguments(filter(RBCLookupTimeoutOption.class, options));
        assert port != null : "Port should never be null.";
        assert host != null : "Host should never be null.";

    }

    private void extractArguments(RBCLookupTimeoutOption[] options) {
        for (RBCLookupTimeoutOption op : options) {
            timeout = op.getTimeout();
        }
    }

    private void extractArguments(RBCPortOption[] rbcPortOptions) {
        for (RBCPortOption op : rbcPortOptions) {
            host = op.getHost();
            port = op.getPort();
        }
    }

    public String getHost() {
        return host;
    }

    public Integer getRMIPort() {
        return port;
    }

    public long getRMILookupTimpout() {
        return timeout;
    }

    public Integer getPort() {
        return port;
    }
}
