package org.gcube.data.spd.layer;

import org.gcube.common.core.contexts.GCUBEServiceContext;
import org.gcube.common.core.contexts.GCUBEStatefulPortTypeContext;
import org.gcube.data.spd.Constants;
import org.gcube.data.spd.context.ServiceContext;

public class LayersPTContext extends GCUBEStatefulPortTypeContext{

	/** Singleton instance. */
	protected static LayersPTContext cache = new LayersPTContext();

	/** Creates an instance . */
	private LayersPTContext(){}
	
	/** Returns a context instance.
	 * @return the context.*/
	public static LayersPTContext getContext() {return cache;}
	
	@Override
	public String getJNDIName() {
		return Constants.OCCURRENCES_PT_NAME;
	}

	@Override
	public String getNamespace() {
		return Constants.NS;
	}

	@Override
	public GCUBEServiceContext getServiceContext() {
		return ServiceContext.getContext();
	}

}
