package org.mism.modelgen.api;

public class ObjectUtil {

	public static boolean equals(Object lhs, Object rhs) {
		return (lhs == rhs) || (lhs != null && rhs != null && lhs.equals(rhs));
	}

	public static HashCode newHash() {
		return new HashCode(37, 73);
	}

}
