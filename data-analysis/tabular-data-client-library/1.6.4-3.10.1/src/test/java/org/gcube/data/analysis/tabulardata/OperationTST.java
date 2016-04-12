package org.gcube.data.analysis.tabulardata;

import static org.gcube.data.analysis.tabulardata.clientlibrary.plugin.AbstractPlugin.operation;

import java.util.List;

import org.gcube.common.scope.api.ScopeProvider;
import org.gcube.data.analysis.tabulardata.clientlibrary.proxy.OperationManagerProxy;
import org.gcube.data.analysis.tabulardata.commons.webservice.types.operations.OperationDefinition;
import org.junit.Assert;
import org.junit.Test;

public class OperationTST {

	private OperationManagerProxy operationProxy = operation().build();
	
	@Test
	public void getCapabilityById() throws Exception{
		ScopeProvider.instance.set("/gcube/devsec");
		
		List<OperationDefinition> operations = operationProxy.getCapabilities();
		
		OperationDefinition selectedOp = null ;
		
		for (OperationDefinition op : operations)
			selectedOp = op;
		
		Assert.assertNotNull(selectedOp);
		
		OperationDefinition retrievedOp = operationProxy.getCapabilities(selectedOp.getOperationId());
		
		Assert.assertEquals(selectedOp, retrievedOp);
	}
	
}
