package org.gcube.contentmanagement.timeseries.geotools.test;

import java.util.ArrayList;

import org.gcube.contentmanagement.timeseries.geotools.engine.TSGeoToolsConfiguration;
import org.gcube.contentmanagement.timeseries.geotools.engine.TimeSeriesGISConverter;
import org.gcube.contentmanagement.timeseries.geotools.filters.AFilter;
import org.gcube.contentmanagement.timeseries.geotools.filters.SpaceFilter;
import org.gcube.contentmanagement.timeseries.geotools.filters.SpeciesFilter;
import org.gcube.contentmanagement.timeseries.geotools.gisconnectors.GISInformation;

public class RegressionTestCountriesConversionGeoNetwork {


	public static void main(String[] args) {
		TSGeoToolsConfiguration configuration = new TSGeoToolsConfiguration();
		configuration.setConfigPath("./cfg/");
		
		configuration.setTimeSeriesDatabase("jdbc:postgresql://localhost/testdb");
		configuration.setTimeSeriesUserName("gcube");
		configuration.setTimeSeriesPassword("d4science2");

		configuration.setGeoServerDatabase("jdbc:postgresql://geoserver-test.d4science-ii.research-infrastructures.eu/timeseriesgisdb");
		configuration.setGeoServerUserName("postgres");
		configuration.setGeoServerPassword("d4science2");
		
		configuration.setAquamapsDatabase("jdbc:postgresql://geoserver-test.d4science-ii.research-infrastructures.eu/essentialaquamaps");
		configuration.setAquamapsUserName("postgres");
		configuration.setAquamapsPassword("d4science2");
		
		GISInformation gisInfo = new GISInformation();
		gisInfo.setGeoNetworkUrl("http://geoserver-dev.d4science-ii.research-infrastructures.eu/geonetwork");
		gisInfo.setGeoNetworkUserName("admin");
		gisInfo.setGeoNetworkPwd("admin");
		
		gisInfo.setGisDataStore("timeseriesgisdb");
		gisInfo.setGisPwd("gcube@geo2010");
		gisInfo.setGisWorkspace("aquamaps");
		gisInfo.setGisUrl("http://geoserver-dev.d4science-ii.research-infrastructures.eu/geoserver");
		gisInfo.setGisUserName("admin");
		
		String timeSeriesName = "ts_8433a550_e2b5_11e0_be86_9878022f54d6";
		String informationColumn = "field0";
		String quantitiesColumn = "field4";
				
		AFilter filter = new SpaceFilter(timeSeriesName,informationColumn,quantitiesColumn);
		
		ArrayList<AFilter> filters = new ArrayList<AFilter>();
		filters.add(filter);
		
		try{
			TimeSeriesGISConverter converter = new TimeSeriesGISConverter(configuration);
			long t0 = System.currentTimeMillis();
			converter.TimeSeriesToGIS(filters,gisInfo,false);
			long t1 = System.currentTimeMillis();
			System.out.println("ELAPSED TIME : "+(t1-t0));
		}catch(Exception e){e.printStackTrace();}
	}

}
