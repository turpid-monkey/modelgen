package org.mism.modelgen.api;

public final class HashCode {
	int hashCode;
	final int factor;
	final int nullConstant;

	public HashCode(int factor, int nullConstant) {
		this.factor = factor;
		this.nullConstant = nullConstant;
	}
	
	public HashCode()
	{
		this(31,13);
	}

	public HashCode hash(Object o) {
		if (o == null) {
			return hashNull();
		} else if (o instanceof Iterable) {
			return hash((Iterable<?>) o);
		} else if (o.getClass().isArray()) {
			if (o.getClass().isPrimitive())
				return hashPrimitiveArray(o);
			else
				return hash((Object[]) o);
		} else {
			hashCode *= factor;
			hashCode += o.hashCode();
			return this;
		}
	}

	protected HashCode hashPrimitiveArray(Object object) {
		if (object instanceof long[]) {
			return hash((long[]) object);
		} else if (object instanceof int[]) {
			return hash((int[]) object);
		} else if (object instanceof short[]) {
			return hash((short[]) object);
		} else if (object instanceof char[]) {
			return hash((char[]) object);
		} else if (object instanceof byte[]) {
			return hash((byte[]) object);
		} else if (object instanceof double[]) {
			return hash((double[]) object);
		} else if (object instanceof float[]) {
			return hash((float[]) object);
		} else if (object instanceof boolean[]) {
			return hash((boolean[]) object);
		}
		throw new RuntimeException(
				"Should never reach - check HashCode implementation.");
	}

	protected HashCode hashNull() {
		hashCode *= factor;
		hashCode += nullConstant;
		return this;
	}

	public HashCode hash(Object[] o) {
		if (o == null)
			return hashNull();
		for (Object e : o)
			hash(e);
		return this;
	}

	public HashCode hash(Iterable<?> iterable) {
		for (Object elem : iterable) {
			hash(elem);
		}
		return this;
	}

	public int value() {
		return hashCode;
	}

	public HashCode hash(long value) {
		hashCode *= factor;
		hashCode += ((int) (value ^ (value >> 32)));
		return this;
	}

	public HashCode hash(int value) {
		hashCode = hashCode * factor + value;
		return this;
	}

	public HashCode hash(short value) {
		hashCode = hashCode * factor + value;
		return this;
	}

	public HashCode hash(char value) {
		hashCode = hashCode * factor + value;
		return this;
	}

	public HashCode hash(byte value) {
		hashCode = hashCode * factor + value;
		return this;
	}

	public HashCode hash(double value) {
		return hash(Double.doubleToLongBits(value));
	}

	public HashCode hash(float value) {
		return hash(Float.floatToIntBits(value));
	}

	public HashCode hash(boolean value) {
		if (value)
			return hash(0);
		else
			return hashNull();
	}

	public HashCode hash(long[] array) {
		if (array == null)
			return hashNull();
		for (long e : array)
			hash(e);
		return this;
	}

	public HashCode hash(int[] array) {
		if (array == null)
			return hashNull();
		for (int e : array)
			hash(e);
		return this;
	}

	public HashCode hash(short[] array) {
		if (array == null)
			return hashNull();
		for (short e : array)
			hash(e);
		return this;
	}

	public HashCode hash(char[] array) {
		if (array == null)
			return hashNull();
		for (char e : array)
			hash(e);
		return this;
	}

	public HashCode hash(byte[] array) {
		if (array == null)
			return hashNull();
		for (byte e : array)
			hash(e);
		return this;
	}

	public HashCode hash(double[] array) {
		if (array == null)
			return hashNull();
		for (double e : array)
			hash(e);
		return this;
	}

	public HashCode hash(float[] array) {
		if (array == null)
			return hashNull();
		for (float e : array)
			hash(e);
		return this;
	}

	public HashCode hash(boolean[] array) {
		if (array == null)
			return hashNull();
		for (boolean e : array)
			hash(e);
		return this;
	}

}