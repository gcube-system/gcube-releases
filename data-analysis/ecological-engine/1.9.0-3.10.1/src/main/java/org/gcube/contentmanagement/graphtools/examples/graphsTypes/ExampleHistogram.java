package org.gcube.contentmanagement.graphtools.examples.graphsTypes;

import org.gcube.contentmanagement.graphtools.core.StatisticsGenerator;
import org.gcube.contentmanagement.graphtools.plotting.graphs.HistogramGraph;
import org.gcube.portlets.user.timeseries.charts.support.types.GraphGroups;

public class ExampleHistogram {

	
	public static void main(String[] args) throws Exception{
		String table = "ts_161efa00_2c32_11df_b8b3_aa10916debe6";
		String xDimension = "field5";
		String yDimension = "field6";
		String groupDimension = "field1";
		String speciesColumn = "field3";
		String filter2 = "Brown seaweeds";
		String filter1 = "River eels";
//		String filter2 = "Osteichthyes";
		StatisticsGenerator stg = new StatisticsGenerator();
		
		stg.init("./cfg/");
		
		GraphGroups gg = stg.generateGraphs(200, table, xDimension, yDimension, groupDimension, speciesColumn, filter1, filter2);
		
		HistogramGraph series = new HistogramGraph("");
		series.renderGraphGroup(gg);
	}
	
}
