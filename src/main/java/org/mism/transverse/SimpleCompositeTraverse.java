package org.mism.transverse;

public class SimpleCompositeTraverse implements Traverse {
	protected Traverselet[] traverselets;
	protected TraverseHook hook;

	public SimpleCompositeTraverse(Traverselet... traverselets) {
		this.traverselets = traverselets;
	}

	public SimpleCompositeTraverse(TraverseHook hook, Traverselet... traverselets) {
		this(traverselets);
		this.hook = hook;
	}

	/* (non-Javadoc)
	 * @see org.mism.transverse.Traverse#traverse(java.lang.Object)
	 */
	@Override
	public void traverse(Object in) {
		if (in == null)
			return;
		if (hook != null)
			hook.handle(in);
		for (Traverselet t : traverselets) {
			try {
				t.traverse(in, this);
			} catch (ClassCastException e) {
				// nope
			}
		}
	}
}