/*
 * Copyright (C) 2010 Toni Menzel
 * Copyright 2023 Craig Gallen, OpenNMS Group
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
package org.opennms.paxexam.container.test;

import org.junit.Test;
import org.opennms.paxexam.container.Parser;
import org.opennms.paxexam.container.OpenNMSRBCRemoteTargetOptions;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for this modules Options Parser implementation.
 */
public class ParserTest {

    @Test(expected = AssertionError.class)
    public void testDefaults() {
        Option[] options = CoreOptions.options();
        new Parser(options);
    }

    @Test
    public void testParsing() {
        Option[] options = CoreOptions.options(OpenNMSRBCRemoteTargetOptions.location("bee", 1234));

        Parser parser = new Parser(options);

        assertThat(parser.getHost(), is("bee"));
        assertThat(parser.getRMIPort(), is(1234));

    }
}
