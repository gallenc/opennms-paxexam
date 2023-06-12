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