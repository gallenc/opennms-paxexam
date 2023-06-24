/*
 *    Copyright 2023 Craig Gallen, OpenNMS Group
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.opennms.paxexam.container;

import java.security.Policy;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;

// see https://www.eclipse.org/forums/index.php?t=msg&th=458100&goto=1017091&#msg_1017091 - 
// Re: Default permissions and custom security manager
public class AllPolicy extends Policy {

	/**
	 * Pulled from the servlet bridge FrameworkLauncher class
	 */
	static final PermissionCollection allPermissions = new PermissionCollection() {
		private static final long serialVersionUID = 482874725021998286L;
		// The AllPermission permission
		Permission allPermission = new AllPermission();

		// A simple PermissionCollection that only has AllPermission
		public void add(Permission permission) {
			// do nothing
		}

		public boolean implies(Permission permission) {
			return true;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Enumeration elements() {
			return new Enumeration() {
				int cur = 0;

				public boolean hasMoreElements() {
					return cur < 1;
				}

				public Object nextElement() {
					if (cur == 0) {
						cur = 1;
						return allPermission;
					}
					throw new NoSuchElementException();
				}
			};
		}
	};
	
    /* (non-Javadoc)
     * @see java.security.Policy#getPermissions(java.security.CodeSource)
     */
    @Override
    public PermissionCollection getPermissions(CodeSource codesource) {
    	return allPermissions;
    }
   
}