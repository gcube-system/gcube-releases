package org.gcube.application.aquamaps.ecomodelling.generators.processing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.gcube.application.aquamaps.ecomodelling.generators.abstracts.AbstractGenerationAlgorithm;
import org.gcube.application.aquamaps.ecomodelling.generators.aquamapsorg.AquamapsAlgorithm;
import org.gcube.application.aquamaps.ecomodelling.generators.configuration.EngineConfiguration;
import org.gcube.application.aquamaps.ecomodelling.generators.connectors.GenerationModel;
import org.gcube.application.aquamaps.ecomodelling.generators.connectors.RemoteHspecInputObject;
import org.gcube.application.aquamaps.ecomodelling.generators.connectors.RemoteHspecOutputObject;
import org.gcube.application.aquamaps.ecomodelling.generators.connectors.livemonitor.ResourceLoad;
import org.gcube.application.aquamaps.ecomodelling.generators.connectors.livemonitor.Resources;
import org.gcube.application.aquamaps.ecomodelling.generators.utils.DatabaseFactory;
import org.gcube.contentmanagement.graphtools.utils.HttpRequest;
import org.gcube.contentmanagement.lexicalmatcher.utils.AnalysisLogger;
import org.hibernate.SessionFactory;

public class DistributionGenerator {

	// STRUCTURES AND VARIABLES
	List<Object> selectedSpecies;
	List<Object> csquares;
	List<Object> speciesInfo;
	List<Object> speciesObservations;
	HashMap<String, String> currentSpeciesBoundingBoxInfo;
	String currentFAOAreas;
	String generationType;
	String currentSpeciesId;
	int csquaresNumber;
	String dynamicWriteQuery;
	String dynamicCSquareQuery;
	String dynamicHSpenQuery;
	String dynamicHSpenMinmaxQuery;
	boolean threadActivity[];
	AbstractGenerationAlgorithm generationAlgorithm;
	GenerationModel generatorAlgorithm = GenerationModel.AQUAMAPS;
	RemoteGenerationManager remoteGenerationManager;
	boolean useDB = true;
	private boolean interruptProcessing;
	double status;
	double globalstatus;
	private ExecutorService executorService;
	boolean isRemoteGeneration;
	int processedRecordsCounter;
	long tstart;
	int numbOfProcessedSpecies;
	// DB SESSION
	protected SessionFactory vreConnection;

	// USED FOR SPECIES SUITABLE GENERATION
	StringBuffer probsClause;

	// USED FOR SPECIES NATIVE GENERATION
	StringBuffer speciesCriteria1;
	StringBuffer speciesCriteria2;
	StringBuffer speciesCriteria3;
	boolean nativegeneration = false;

	// PARAMETERS
	static int chunksize = 5000;
	int numberOfthreads = 16;
	String defaultDatabaseFile = "DestinationDBHibernate.cfg.xml";
	String defaultLogFile = "ALog.properties";
	String default_hspec_suitable_table = "hspec_suitable_gp2";
	String default_hcaf_table = "hcaf_d";
	String default_hspen_table = "hspen";
	String default_hspen_maxminlat = "maxminlat_hspen";
	String default_species_list = "selectedSpecies.txt";
	String default_remote_calculator = "http://node1.d.venusc.research-infrastructures.eu:5941/api/";
	String default_service_userName = "unknown";
	String default_remote_environment = "windows azure";
	HashMap<String, String> default_remote_properties;

	String default_cache_path = "./cfg/";
	String default_summaryLog = null;

	// PARAMETRIC QUERIES
	// static String csquareCodeQuery = "select s.csquarecode,depthmean,depthmax,depthmin, sstanmean,sbtanmean,salinitymean,salinitybmean, primprodmean,iceconann,landdist,oceanarea,centerlat,centerlong,faoaream from %1$s d join hcaf_s s on s.csquarecode = d.csquarecode and s.oceanarea>0";
	static String csquareCodeQuery = "select csquarecode,depthmean,depthmax,depthmin, sstanmean,sbtanmean,salinitymean,salinitybmean, primprodmean,iceconann,landdist,oceanarea,centerlat,centerlong,faoaream,eezall,lme from %1$s d where oceanarea>0";
	static String selectSpeciesQuery = "select depthmin,meandepth,depthprefmin,pelagic,depthprefmax,depthmax,tempmin,layer,tempprefmin,tempprefmax,tempmax,salinitymin,salinityprefmin,salinityprefmax,salinitymax,primprodmin,primprodprefmin,primprodprefmax,primprodmax,iceconmin,iceconprefmin,iceconprefmax,iceconmax,landdistyn,landdistmin,landdistprefmin,landdistprefmax,landdistmax,nmostlat,smostlat,wmostlong,emostlong,faoareas from %HSPEN% where speciesid = '%1$s';";
	static String selectAllSpeciesQuery = "select depthmin,meandepth,depthprefmin,pelagic,depthprefmax,depthmax,tempmin,layer,tempprefmin,tempprefmax,tempmax,salinitymin,salinityprefmin,salinityprefmax,salinitymax,primprodmin,primprodprefmin,primprodprefmax,primprodmax,iceconmin,iceconprefmin,iceconprefmax,iceconmax,landdistyn,landdistmin,landdistprefmin,landdistprefmax,landdistmax,nmostlat,smostlat,wmostlong,emostlong,faoareas,speciesid from %HSPEN%;";
	// static String selectSpeciesObservationQuery = "SELECT DISTINCT Max(hcaf_s.CenterLat) AS maxCLat, Min(hcaf_s.CenterLat) AS minCLat FROM occurrencecells INNER JOIN hcaf_s ON occurrencecells.CsquareCode = hcaf_s.CsquareCode WHERE (((hcaf_s.oceanarea > 0))) AND occurrencecells.SpeciesID = '%1$s' AND occurrencecells.GoodCell <> 0;";
	static String selectAllSpeciesObservationQuery = "SELECT speciesid,maxclat,minclat from %HSPEN%;";
	static String valuesTemplate = "('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s')";
	static String insertionStatement = "insert into %TABLENAME% values %1$s;";
	// original creation
	// static String createTableStatement = "CREATE TABLE %1$s ( speciesid character varying, csquarecode character varying, probability real, boundboxyn smallint, faoareayn smallint, faoaream integer, eezall character varying, lme integer) WITH (OIDS=FALSE ); ALTER TABLE %1$s OWNER TO %2$s; CREATE INDEX CONCURRENTLY %1$s_idx ON %1$s USING btree (speciesid, csquarecode, faoaream, eezall, lme);";
	static String createTableStatement = "CREATE TABLE %1$s ( speciesid character varying, csquarecode character varying, probability real, boundboxyn smallint, faoareayn smallint, faoaream integer, eezall character varying, lme integer) WITH (OIDS=FALSE ); ALTER TABLE %1$s OWNER TO %2$s;";
	static String createTableStatementPartitioned = "CREATE TABLE %1$s ( speciesid character varying, csquarecode character varying, probability real, boundboxyn smallint, faoareayn smallint, faoaream integer, eezall character varying, lme integer)  WITH (OIDS=FALSE ) TABLESPACE %3$s; ALTER TABLE %1$s OWNER TO %2$s; ";
	static String createIndexStatement = "CREATE INDEX CONCURRENTLY %1$s_idx ON %1$s USING btree (speciesid, csquarecode, faoaream, eezall, lme);";
	// for test purposes
	// static String createTableStatement = "CREATE TABLE %1$s ( speciesid character varying, csquarecode character varying, probability real, boundboxyn smallint, faoareayn smallint, faoaream integer, eezall character varying, lme integer) WITH (OIDS=FALSE ); ALTER TABLE %1$s OWNER TO %2$s; CREATE INDEX %1$s_speciesidx ON %1$s USING btree (speciesid); CREATE INDEX %1$s_csquarecodeidx ON %1$s USING btree (csquarecode);  CREATE INDEX %1$s_faoareamidx ON %1$s USING btree (faoaream);CREATE INDEX %1$s_eezallidx ON %1$s USING btree (eezall); CREATE INDEX %1$s_lmeidx ON %1$s USING btree (lme); CREATE INDEX %1$s_probabilityidx ON %1$s USING btree (probability); );";
	static String speciesListQuery = "select distinct speciesid from %1$s;";

	// USED ONLY WITH THE REMOTE GENERATION
	String dbjdbcUrl;
	String dbuserName;
	String dbpassword;
	private double remoteMultiplier;
	private double remoteShift;

	public double getStatus() {
		if (isRemoteGeneration) {
//			 System.out.println("COMPLETION:"+remoteGenerationManager.retrieveCompletion());
			try {
				if (status == 100)
					return 100;
				else
				return remoteShift + remoteGenerationManager.retrieveCompletion()*(remoteMultiplier);
			} catch (Exception e) {
				return 0.0;
			}
		} else
			return globalstatus;
	}

	// get the file name for the computation summary
	public String getSummaryComputationFile() {
		return default_summaryLog;
	}

	// get the Species Load, that is the number of processed species
	public String getSpeciesLoad() {
		String returnString = "";
		try {
			if (isRemoteGeneration) {
				RemoteHspecOutputObject rhoo = remoteGenerationManager.retrieveCompleteStatus();
				if (rhoo.metrics.throughput.size() > 1) {
					ResourceLoad rs = new ResourceLoad(rhoo.metrics.throughput.get(0), rhoo.metrics.throughput.get(1));
					returnString = rs.toString();
				}
			} else {
				long tk = System.currentTimeMillis();
				double activity = numbOfProcessedSpecies;
				ResourceLoad rs = new ResourceLoad(tk, activity);
				returnString = rs.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			long tk = System.currentTimeMillis();
			returnString = new ResourceLoad(tk, 0).toString();
		}
		return returnString;
	}

	private int lastProcessedRecordsNumber;
	private long lastTime;

	// get the Resource Load, that is the number of hspec records for second
	public String getResourceLoad() {
		String returnString = "";
		try {
			if (isRemoteGeneration) {
				try {
					RemoteHspecOutputObject rhoo = remoteGenerationManager.retrieveCompleteStatus();
					if (rhoo.metrics.throughput.size() > 1) {
						ResourceLoad rs = new ResourceLoad(rhoo.metrics.throughput.get(0), rhoo.metrics.throughput.get(1));
						returnString = rs.toString();
					}

				} catch (Exception e) {

				}
			} else {
				long tk = System.currentTimeMillis();
				// double activity = Double.valueOf(processedRecordsCounter)*1000.00/Double.valueOf(tk-tstart);
				double activity = Double.valueOf(processedRecordsCounter - lastProcessedRecordsNumber) * 1000.00 / Double.valueOf(tk - lastTime);
				lastTime = tk;
				lastProcessedRecordsNumber = processedRecordsCounter;

				ResourceLoad rs = new ResourceLoad(tk, activity);
				returnString = rs.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			long tk = System.currentTimeMillis();
			returnString = new ResourceLoad(tk, 0).toString();
		}
		writeSummaryLog(returnString);
		return returnString;
	}

	private void writeSummaryLog(String summaryString) {
		if ((default_summaryLog != null) && (default_summaryLog.length() > 0)) {
			FileWriter fw = null;
			try {
				fw = new FileWriter(default_summaryLog, true);
				fw.write(summaryString + System.getProperty("line.separator"));
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
				AnalysisLogger.getLogger().trace("writeSummaryLog-> an error has occurred with the file ", e);
				try {
					fw.close();
				} catch (Exception e2) {
				}
			}
		}
	}

	// this methods gets information about the threads or the machines which are running the computation
	public String getResources() {
		Resources res = new Resources();
		try {
			if (isRemoteGeneration) {
				try {
					RemoteHspecOutputObject rhoo = remoteGenerationManager.retrieveCompleteStatus();
					res.list = rhoo.metrics.load;
				} catch (Exception e) {
				}
			} else {
				for (int i = 0; i < numberOfthreads; i++) {
					try {
						double value = (threadActivity[i]) ? 100.00 : 0.00;
						res.addResource("Thread_" + (i + 1), value);
					} catch (Exception e1) {
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ((res != null) && (res.list != null))
			return HttpRequest.toJSon(res.list).replace("resId", "resID");
		else
			return "";
	}

	public GenerationModel getGenerationAlgorithm() {
		return generatorAlgorithm;
	}

	// populates the selectedSpecies variable by reading species from file
	private void populateSelectedSpecies() {
		BufferedReader br = null;
		if (selectedSpecies == null) {
			try {
				br = new BufferedReader(new FileReader(default_species_list));
				selectedSpecies = new ArrayList<Object>();
				String line = br.readLine();
				while (line != null) {
					selectedSpecies.add(line.trim());
					line = br.readLine();
				}

			} catch (Exception e) {
				AnalysisLogger.getLogger().trace("Distribution Generator - SELECTED SPECIES - FILE NOT FOUND - POPULATING FROM DB");
				populateSelectedSpeciesByDB();
			} finally {
				try {
					br.close();
				} catch (Exception e) {
				}
			}
		}
	}

	// populates the selectedSpecies variable by getting species from db
	private void populateSelectedSpeciesByDB() {
		String query = String.format(speciesListQuery, default_hspen_table);
		AnalysisLogger.getLogger().trace("Distribution Generator ->getting all species list from DB");
		if (useDB) {
			List<Object> allspecies = DatabaseFactory.executeSQLQuery(query, vreConnection);
			selectedSpecies = allspecies;
		}
	}

	// populates the selectedSpecies variable from outside
	public void setSelectedSpecies(ArrayList<String> selectedSpecies) {
		this.selectedSpecies = new ArrayList<Object>();
		for (String species : selectedSpecies) {
			this.selectedSpecies.add(species);
		}
	}

	// get all csquares information
	private void getCsquares() {
		csquares = new ArrayList<Object>();
		long t0 = System.currentTimeMillis();
		if (useDB)
			csquares = DatabaseFactory.executeSQLQuery(dynamicCSquareQuery, vreConnection);
		long t1 = System.currentTimeMillis();
		AnalysisLogger.getLogger().trace("Distribution Generator-> CSQUARES QUERY - EXECUTION TIME : " + (t1 - t0));
	}

	// get all information about a single species
	private void getSpeciesInfo(String speciesid) {
		speciesInfo = null;
		speciesObservations = null;
		speciesInfo = allSpeciesHspen.get(speciesid);
		speciesObservations = allSpeciesObservations.get(speciesid);

		currentSpeciesId = speciesid;
	}

	HashMap<String, List<Object>> allSpeciesHspen;
	HashMap<String, List<Object>> allSpeciesObservations;

	// gets hspen and observation information
	private void getAllSpeciesInfo() {
		allSpeciesHspen = new HashMap<String, List<Object>>();
		allSpeciesObservations = new HashMap<String, List<Object>>();
		if (useDB) {
			long t0 = System.currentTimeMillis();
			List<Object> SpeciesInfo = DatabaseFactory.executeSQLQuery(dynamicHSpenQuery, vreConnection);
			List<Object> SpeciesObservations = DatabaseFactory.executeSQLQuery(dynamicHSpenMinmaxQuery, vreConnection);

			int lenSpecies = SpeciesInfo.size();
			int lenObservations = SpeciesObservations.size();

			for (int i = 0; i < lenSpecies; i++) {
				Object[] speciesArray = (Object[]) SpeciesInfo.get(i);
				String speciesid = (String) speciesArray[33];
				List<Object> singleSpeciesInfo = new ArrayList<Object>();
				singleSpeciesInfo.add(speciesArray);
				allSpeciesHspen.put((String) speciesid, singleSpeciesInfo);
			}
			for (int i = 0; i < lenObservations; i++) {
				Object[] maxminArray = (Object[]) SpeciesObservations.get(i);
				String speciesid = (String) maxminArray[0];
				List<Object> maxminInfo = new ArrayList<Object>();
				maxminInfo.add(maxminArray);
				allSpeciesObservations.put((String) speciesid, maxminInfo);
			}
			long t1 = System.currentTimeMillis();

			AnalysisLogger.getLogger().trace("ElapsedTime for HSpen information population : " + (t1 - t0) + "ms " + " number of species :" + lenSpecies);
		}
	}

	// initializes write buffers
	private void initBuffers() {
		speciesCriteria1 = null;
		speciesCriteria2 = null;
		speciesCriteria3 = null;
		probsClause = null;
		speciesCriteria1 = new StringBuffer();
		speciesCriteria2 = new StringBuffer();
		speciesCriteria3 = new StringBuffer();
		probsClause = new StringBuffer();
	}

	// performs a bulk insertion on the DB
	private void insertValues(String clauses) throws Throwable {
		if (useDB)
			DatabaseFactory.executeSQLUpdate(String.format(dynamicWriteQuery, clauses), vreConnection);
	}

	// cleans the table on which generation has to be performed
	private void deleteValues() throws Exception {
		String query = "delete from " + default_hspec_suitable_table;
		if (useDB)
			DatabaseFactory.executeSQLUpdate(query, vreConnection);
	}

	// checks out the insertion type to perform
	private void insertCriteria() {

		StringBuffer criteria = null;
		if (speciesCriteria1.length() > 0)
			criteria = speciesCriteria1;
		else if (speciesCriteria2.length() > 0)
			criteria = speciesCriteria2;
		else if (speciesCriteria3.length() > 0)
			criteria = speciesCriteria3;
		else if (probsClause.length() > 0)
			criteria = probsClause;

		bulkInsert(criteria);
	}

	// performs a bulk insert for several rows
	private void bulkInsert(StringBuffer criteria) {

		// eliminate last comma
		if (criteria != null) {
			int lastcomma = criteria.length();
			if (lastcomma > 5000000) {
				AnalysisLogger.getLogger().trace("BulkInsert-> Separating criteria elementsfor insert");
				String[] subcriteria = criteria.toString().split("\\),");
				int fulllen = subcriteria.length;
				int len = fulllen / 2;
				StringBuffer subcrit = new StringBuffer();
				AnalysisLogger.getLogger().trace("BulkInsert-> First chunk : till " + len);
				for (int i = 0; i < len; i++) {
					subcrit.append(subcriteria[i] + "),");
				}
				int sublen = subcrit.length();
				subcrit = subcrit.replace(sublen - 1, sublen, "");
				try {
					AnalysisLogger.getLogger().trace("BulkInsert-> Inserting values for chunk");
					insertValues(subcrit.toString());
				} catch (Throwable e2) {
					e2.printStackTrace();
				}

				AnalysisLogger.getLogger().trace("BulkInsert-> Second chunk: till " + fulllen);

				subcrit = null;
				subcrit = new StringBuffer();
				for (int i = len; i < fulllen; i++) {
					subcrit.append(subcriteria[i] + "),");
				}
				int restlen = subcrit.length();
				subcrit = subcrit.replace(restlen - 1, restlen, "");
				try {
					insertValues(subcrit.toString());
				} catch (Throwable e2) {
					e2.printStackTrace();
				}

				subcrit = null;
			} else if (lastcomma > 0) {
				criteria = criteria.replace(lastcomma - 1, lastcomma, "");
				try {
					insertValues(criteria.toString());
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

	// waits for thread to be free
	private void wait4Thread(int index) {

		// wait until thread is free
		while (threadActivity[index]) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	// initializes threads activity status
	public void initializeThreads(int numberOfThreadsToUse) {
		// initialize threads and their activity state
		executorService = Executors.newFixedThreadPool(numberOfThreadsToUse);

		threadActivity = new boolean[numberOfThreadsToUse];
		// initialize to false;
		for (int j = 0; j < threadActivity.length; j++) {
			threadActivity[j] = false;
		}

	}

	// Calculate Presence in a Bounding Box for a csquare
	public HashMap<String, Integer> calculateBoundingBox(Object[] csquarecode) {
		HashMap<String, Integer> boundingInfo = generationAlgorithm.calculateBoundingBox("" + csquarecode[0], currentSpeciesBoundingBoxInfo.get("$pass_NS"), currentSpeciesBoundingBoxInfo.get("$pass_N"), currentSpeciesBoundingBoxInfo.get("$pass_S"), AquamapsAlgorithm.getElement(csquarecode, 12),// centerlat
				AquamapsAlgorithm.getElement(csquarecode, 13),// centerlong
				AquamapsAlgorithm.getElement(csquarecode, 14),// faoaream
				currentSpeciesBoundingBoxInfo.get("$paramData_NMostLat"), currentSpeciesBoundingBoxInfo.get("$paramData_SMostLat"), currentSpeciesBoundingBoxInfo.get("$paramData_WMostLong"), currentSpeciesBoundingBoxInfo.get("$paramData_EMostLong"), currentFAOAreas, currentSpeciesBoundingBoxInfo.get("$northern_hemisphere_adjusted"), currentSpeciesBoundingBoxInfo.get("$southern_hemisphere_adjusted"));

		return boundingInfo;
	}

	// for external usage
	public double calculateModelProbability(Object[] hspen, Object[] csquareinfo) {
		return generationAlgorithm.getSpeciesProb(hspen, csquareinfo);
	}

	// calculates probability for suitable hspec
	void calcProb(String speciesid, int startindex) throws Exception {
		int max = Math.min(startindex + chunksize, csquaresNumber);
		for (int i = startindex; i < max; i++) {

			Object[] csquarecode = (Object[]) csquares.get(i);
			double prob = generationAlgorithm.getSpeciesProb((Object[]) speciesInfo.get(0), csquarecode);
			HashMap<String, Integer> boundingInfo = calculateBoundingBox(csquarecode);
			if (prob >= 0.01) {
				probsClause.append(String.format(valuesTemplate, currentSpeciesId, "" + csquarecode[0], prob, boundingInfo.get("$InBox"), boundingInfo.get("$InFAO"), "" + csquarecode[14], "" + csquarecode[15], "" + csquarecode[16]) + ",");
				// probsClause.append(",");
			}
			// increment overall processed records
			processedRecordsCounter++;
		}
	}

	// calculates probability for native hspec
	void calcProbNative(String speciesid, int startindex) throws Exception {
		int max = Math.min(startindex + chunksize, csquaresNumber);
		// long t0c = System.currentTimeMillis();
		for (int i = startindex; i < max; i++) {
			// long t0 = System.currentTimeMillis();
			Object[] csquarecode = (Object[]) csquares.get(i);

			double prob = generationAlgorithm.getSpeciesProb((Object[]) speciesInfo.get(0), csquarecode);
			// long t1m = System.currentTimeMillis()-t0;
			// if (t1m>0) AnalysisLogger.getLogger().trace("elapsed on micro calculation: "+t1m+" ms");

			HashMap<String, Integer> boundingInfo = calculateBoundingBox(csquarecode);
			if (prob >= 0.01) {
				Integer Inbox = boundingInfo.get("$InBox");
				Integer InFAO = boundingInfo.get("$InFAO");
				String values = String.format(valuesTemplate, currentSpeciesId, "" + csquarecode[0], prob, boundingInfo.get("$InBox"), boundingInfo.get("$InFAO"), "" + csquarecode[14], "" + csquarecode[15], "" + csquarecode[16]);
				if ((Inbox == 1) && (InFAO == 1)) {
					speciesCriteria1.append(values + ",");
				} else if ((Inbox == 0) && (InFAO == 1)) {
					speciesCriteria2.append(values + ",");
				} else if ((Inbox == 1) && (InFAO == 0)) {
					speciesCriteria3.append(values + ",");
				}
			}

			// increment overall processed records
			processedRecordsCounter++;

			// long t1 = System.currentTimeMillis()-t0;
			// if (t1>0) AnalysisLogger.getLogger().trace("elapsed on single calculation: "+t1+" ms");
		}
		// long t1c = System.currentTimeMillis()-t0c;
		// AnalysisLogger.getLogger().trace("elapsed on complete calculation: "+t1c+" ms");

	}

	// initializes DB session
	public void initDBSession(EngineConfiguration engineConf) throws Exception {
		if ((engineConf != null) && (engineConf.getConfigPath() != null)) {
			defaultDatabaseFile = engineConf.getConfigPath() + defaultDatabaseFile;
			dbjdbcUrl = engineConf.getDatabaseURL();
			dbuserName = engineConf.getDatabaseUserName();
			dbpassword = engineConf.getDatabasePassword();
		}
		if (useDB)
			vreConnection = DatabaseFactory.initDBConnection(defaultDatabaseFile, engineConf);
	}

	// shutdown db connection
	public void shutdownConnection() {
		vreConnection.close();
	}

	public DistributionGenerator(EngineConfiguration engine) throws Exception {
		if (engine != null) {
			useDB = engine.useDB();
			// init DB session
			initDBSession(engine);
			// init logger
			if (engine.getConfigPath() != null) {
				defaultLogFile = engine.getConfigPath() + defaultLogFile;
				default_species_list = engine.getConfigPath() + default_species_list;
			}
			AnalysisLogger.setLogger(defaultLogFile);
			// setup native generation flag
			nativegeneration = engine.isNativeGeneration();
			// if necessary set up Destination Table
			if (engine.getDistributionTable() != null)
				default_hspec_suitable_table = engine.getDistributionTable();

			if (engine.getCachePath() != null)
				default_cache_path = engine.getCachePath();

			if (engine.getWriteSummaryLog())
				default_summaryLog = default_cache_path + "distributionProcessSummary_" + UUID.randomUUID() + ".log";

			if (engine.getMaxminLatTable() != null)
				default_hspen_maxminlat = engine.getMaxminLatTable();

			// setup the dynamic insertion query
			dynamicWriteQuery = insertionStatement.replace("%TABLENAME%", default_hspec_suitable_table);
			// setup the type of generation
			if (engine.isType2050())
				generationType = "2050";
			else
				generationType = "";

			if (engine.getServiceUserName() != null)
				default_service_userName = engine.getServiceUserName();

			if (engine.getRemoteCalculator() != null)
				default_remote_calculator = engine.getRemoteCalculator();

			if (engine.getHcafTable() != null)
				default_hcaf_table = engine.getHcafTable();
			if (engine.getHspenTable() != null)
				default_hspen_table = engine.getHspenTable();
			if (engine.getRemoteEnvironment() != null)
				default_remote_environment = engine.getRemoteEnvironment();
			if (engine.getGeneralProperties() != null)
				default_remote_properties = engine.getGeneralProperties();

			// setup of the number of threads
			if (engine.getNumberOfThreads() != null)
				numberOfthreads = engine.getNumberOfThreads();
			
			if (engine.getGenerator() != null)
				generatorAlgorithm = engine.getGenerator();
			
			// create remote table
			if (engine.createTable()) {
				try {
					if (useDB) {
						String createPartitionTableStatement = "";
						if (engine.getTableStore()!= null)
							createPartitionTableStatement = String.format(createTableStatementPartitioned, default_hspec_suitable_table, engine.getDatabaseUserName(), engine.getTableStore());
						else
							createPartitionTableStatement = String.format(createTableStatement, default_hspec_suitable_table, engine.getDatabaseUserName());
						
						if (("" + generatorAlgorithm).startsWith("REMOTE")){
							DatabaseFactory.executeSQLUpdate(createPartitionTableStatement, vreConnection);
							AnalysisLogger.getLogger().debug(createPartitionTableStatement);
							DatabaseFactory.executeUpdateNoTransaction(String.format(createIndexStatement, default_hspec_suitable_table), engine.getDatabaseDriver(), engine.getDatabaseUserName(), engine.getDatabasePassword(), engine.getDatabaseURL(), true);
						}
						else{
							DatabaseFactory.executeSQLUpdate(createPartitionTableStatement, vreConnection);
							AnalysisLogger.getLogger().debug(String.format(createIndexStatement, default_hspec_suitable_table));
							// DatabaseFactory.executeUpdateNoTransaction(String.format(createIndexStatement,default_hspec_suitable_table), vreConnection,true);
							DatabaseFactory.executeUpdateNoTransaction(String.format(createIndexStatement, default_hspec_suitable_table), engine.getDatabaseDriver(), engine.getDatabaseUserName(), engine.getDatabasePassword(), engine.getDatabaseURL(), true);
						}
						
					}
				} catch (Exception e) {
					AnalysisLogger.getLogger().trace("Distribution Generator->could not create table");
					e.printStackTrace();
					// System.err.println("DistributionGeneratorFile->TABLE NOT CREATED");e.printStackTrace();
				}
				if (useDB) {
					AnalysisLogger.getLogger().trace("Distribution Generator->deleting values");
					deleteValues();
				}
			}

			
		}

		initEngine();
	}

	// interrupts the processing
	public void stopProcess() {
		interruptProcessing = true;
	}

	// init engine
	private void initEngine() {

		if (generatorAlgorithm == GenerationModel.AQUAMAPS)
			generationAlgorithm = new AquamapsAlgorithm();
		// initialize status
		status = 0;
		interruptProcessing = false;
//		remoteMultiplier = 0.75; //to be re added for the vacuum process
		remoteMultiplier = 1;
		remoteShift = 0;
		dynamicWriteQuery = insertionStatement.replace("%TABLENAME%", default_hspec_suitable_table);
		dynamicCSquareQuery = String.format(csquareCodeQuery, default_hcaf_table);
		dynamicHSpenQuery = selectAllSpeciesQuery.replace("%HSPEN%", default_hspen_table);
		dynamicHSpenMinmaxQuery = selectAllSpeciesObservationQuery.replace("%HSPEN%", default_hspen_maxminlat);

		// AnalysisLogger.getLogger().warn("Distribution Generator ->Engine Initialized");
	}

	// initializes currentFAOAreas and currentSpeciesBoundingBoxInfo
	public void getBoundingBoxInformation(Object[] speciesInfoRow, Object[] speciesObservations) {
		Object[] row = speciesInfoRow;
		String $paramData_NMostLat = AquamapsAlgorithm.getElement(row, 28);
		String $paramData_SMostLat = AquamapsAlgorithm.getElement(row, 29);
		String $paramData_WMostLong = AquamapsAlgorithm.getElement(row, 30);
		String $paramData_EMostLong = AquamapsAlgorithm.getElement(row, 31);
		currentFAOAreas = AquamapsAlgorithm.getElement(row, 32);
		// adjust FAO areas
		currentFAOAreas = generationAlgorithm.procFAO_2050(currentFAOAreas);
		// get Bounding Box Information
		currentSpeciesBoundingBoxInfo = generationAlgorithm.getBoundingBoxInfo($paramData_NMostLat, $paramData_SMostLat, $paramData_WMostLong, $paramData_EMostLong, speciesObservations, generationType);
		// end of get BoundingBoxInformation
	}

	// Dispatches the request remotely or locally
	public void generateHSPEC() throws Exception {

		// check if the model is suitable for the current implementations
		if (("" + generatorAlgorithm).startsWith("REMOTE")) {
			generatorAlgorithm = GenerationModel.valueOf(("" + generatorAlgorithm).replace("REMOTE_", ""));
			AnalysisLogger.getLogger().warn("Required Algorithm is of Remote Type ");
			isRemoteGeneration = true;
			try {
				shutdownConnection();
			} catch (Exception e) {
			}
			generateRemoteHSPEC();
		} else {
			AnalysisLogger.getLogger().warn("Required Algorithm is of Standalone Type ");
			isRemoteGeneration = false;
			generateStandaloneHSPEC();
		}

	}

	// REMOTE GENERATION
	public void generateRemoteHSPEC() throws Exception {
		try{
		RemoteHspecInputObject rhio = new RemoteHspecInputObject();
		rhio.userName = default_service_userName;
		// added information 04/07/11
		rhio.environment = default_remote_environment;
		rhio.configuration = default_remote_properties;

		rhio.generativeModel = "" + generatorAlgorithm;
		String jdbcUrl = dbjdbcUrl;
		String userName = dbuserName;
		String password = dbpassword;

		jdbcUrl += ";username=" + userName + ";password=" + password;
		// jdbc:sqlserver://localhost;user=MyUserName;password=*****;

		rhio.hcafTableName.tableName = default_hcaf_table;
		rhio.hcafTableName.jdbcUrl = jdbcUrl;

		rhio.hspecDestinationTableName.tableName = default_hspec_suitable_table;
		rhio.hspecDestinationTableName.jdbcUrl = jdbcUrl;

		rhio.hspenTableName.tableName = default_hspen_table;
		rhio.hspenTableName.jdbcUrl = jdbcUrl;

		rhio.occurrenceCellsTable.tableName = "maxminlat_" + default_hspen_table;
		rhio.occurrenceCellsTable.jdbcUrl = jdbcUrl;

		rhio.is2050 = generationType.equals("2050") ? true : false;
		rhio.isNativeGeneration = nativegeneration;
		rhio.nWorkers = numberOfthreads;
		// create and call the remote generator

		remoteGenerationManager = new RemoteGenerationManager(default_remote_calculator);
		AnalysisLogger.getLogger().trace("REMOTE PROCESSING SUBMITTING JOB");
		remoteGenerationManager.submitJob(rhio);
		AnalysisLogger.getLogger().trace("REMOTE PROCESSING STARTED");
		boolean finish = false;

		while (!finish) {
			RemoteHspecOutputObject oo = remoteGenerationManager.retrieveCompleteStatus();
			if ((interruptProcessing) || (oo.status.equals("DONE")) || (oo.status.equals("ERROR"))) {
				finish = true;
				AnalysisLogger.getLogger().trace("REMOTE PROCESSING FINISHED");
				if (oo.status.equals("ERROR"))
					throw new Exception("REMOTE ERROR REPORTED");
			}
			Thread.sleep(250);
//			Thread.sleep(5000);
		}
		AnalysisLogger.getLogger().trace("->END OF PROCEDURE");
		// modification of 23/11/2011
		// APPLY VACUUM OPERATION
		//TO BE READDED
		/*
		if (!interruptProcessing) {
			
			rhio.remoteOperation = "VACUUM";
			RemoteGenerationManager remoteGenerationManagerVacuum = new RemoteGenerationManager(default_remote_calculator);
			remoteGenerationManagerVacuum.submitJob(rhio);
			// WAIT FOR VACUUM TO FINISH
			finish = false;
			while (!finish) {
				RemoteHspecOutputObject oo = remoteGenerationManagerVacuum.retrieveCompleteStatus();
				if ((interruptProcessing) || (oo.status.equals("DONE")) || (oo.status.equals("ERROR"))) {
					finish = true;
					AnalysisLogger.getLogger().trace("->REMOTE VACUUM  PROCEDURE FINISHED");
				}
				Thread.sleep(250);
//				Thread.sleep(5000);
			}
			remoteShift = 25.0;
		}
	*/
	}catch(Exception e){
		AnalysisLogger.getLogger().debug("ERROR: "+e.getLocalizedMessage());	
		throw e;
		}
		finally{
			shutdownConnection();
			status = 100f;
			AnalysisLogger.getLogger().debug("COMPUTATION FINISHED"+ status);	
		}
	}

	// PROBABILITIES GENERATION
	public void generateStandaloneHSPEC() throws Exception {

		// INITIALIZATION
		AnalysisLogger.getLogger().trace("Distribution Generator->populating species");
		populateSelectedSpecies();
		AnalysisLogger.getLogger().trace("Distribution Generator->getting squares");
		// get all csquares
		getCsquares();
		AnalysisLogger.getLogger().trace("Distribution Generator->getting all species information");
		// get all species info
		getAllSpeciesInfo();

		csquaresNumber = csquares.size();
		// calculate number of chunks to take into account
		int numOfChunks = csquaresNumber / chunksize;
		if ((csquaresNumber % chunksize) != 0) {
			numOfChunks += 1;
		}
		AnalysisLogger.getLogger().trace("Distribution Generator->GENERATION STARTED");
		// initialize threads
		initializeThreads(numberOfthreads);

		tstart = System.currentTimeMillis();

		// END INITIALIZATION
		// overall chunks counter
		int overallcounter = 0;
		int numOfSpecies = selectedSpecies.size();
		numbOfProcessedSpecies = 0;
		// SPECIES CALCULATION
		// cycle throw the species to generate
		for (Object species : selectedSpecies) {
			// initialize writing buffer
			initBuffers();

			long t0 = System.currentTimeMillis();
			// get speciesID
			String speciesid = (String) species;
			// get species information from DB
			getSpeciesInfo(speciesid);
			// calculates BoundingBox Information from species Information
			if (((speciesObservations == null) || speciesObservations.size() == 0)) {
				Object[] defaultmaxmin = { "90", "-90" };
				speciesObservations = new ArrayList<Object>();
				speciesObservations.add(defaultmaxmin);
			}
			getBoundingBoxInformation((Object[]) speciesInfo.get(0), (Object[]) speciesObservations.get(0));

			// calculation on multiple threads
			AnalysisLogger.getLogger().trace("Distribution Generator->ANALIZING SPECIES: " + speciesid);
			// thread selection index
			int currentThread = 0;
			// global chunks counter
			int globalcounter = 0;
			// take time
			long computationT0 = System.currentTimeMillis();
			// CALCULATION CORE
			for (int k = 0; k < numOfChunks; k++) {
				// get the starting index
				int start = k * chunksize;
				// wait for thread to be free
				wait4Thread(currentThread);
				// start species information calculation on the thread
				startNewTCalc(currentThread, speciesid, start);
				// increment thread selection index
				currentThread++;
				// reset current thread index
				if (currentThread >= numberOfthreads)
					currentThread = 0;
				// report probability
				status = (double) ((int) (((double) globalcounter * 100f / (double) numOfChunks) * 100f)) / 100f;
				globalstatus = ((double) overallcounter / ((double) (numOfSpecies * numOfChunks))) * 100.00;
				// AnalysisLogger.getLogger().trace("STATUS->"+status+"%");
				// increment global counter index
				globalcounter++;
				overallcounter++;
			}
			// END OF CALCULATION CORE

			// wait for last threads to finish
			for (int i = 0; i < numberOfthreads; i++) {
				// free previous calculation
				wait4Thread(i);
			}

			long computationT1 = System.currentTimeMillis();
			AnalysisLogger.getLogger().trace("Species Computation Finished in " + (computationT1 - computationT0) + " ms");

			// perform overall insert
			insertCriteria();

			// increment the count of processed species
			numbOfProcessedSpecies++;
			// REPORT ELAPSED TIME
			long t1 = System.currentTimeMillis();
			long t3 = (t1 - t0);
			AnalysisLogger.getLogger().trace("\nDistribution Generator->Species Computation and Writing Finished in: " + t3 + " ms");
			// System.exit(0);
			if (interruptProcessing)
				break;
		}
		// REPORT OVERALL ELAPSED TIME
		long tend = System.currentTimeMillis();
		long ttotal = tend - tstart;
		globalstatus = 100.00;
		AnalysisLogger.getLogger().warn("\nDistribution Generator->Algorithm finished in: " + ((double) ttotal / (double) 60000) + " min\n");
		// shutdown threads
		executorService.shutdown();
		// shutdown connection
		shutdownConnection();
	}

	// END OF PROBABILITIES GENERATION PROCEDURE

	// THREAD SECTION
	// definition of the Thread
	private class ThreadCalculator implements Callable<Integer> {
		int index;
		int startindex;
		String species;
		DistributionGenerator aag;

		public ThreadCalculator(DistributionGenerator aag, int index, String species, int start) {
			this.index = index;
			this.species = species;
			this.startindex = start;
			this.aag = aag;
		}

		public Integer call() {

			try {
				if (nativegeneration)
					aag.calcProbNative(species, startindex);
				else
					aag.calcProb(species, startindex);

			} catch (Exception e) {
				AnalysisLogger.getLogger().trace("" + e);
				e.printStackTrace();
			}
			threadActivity[index] = false;
			return 0;
		}
	}

	// end Definition of the Thread
	// activation
	private void startNewTCalc(int index, String species, int start) {
		threadActivity[index] = true;
		ThreadCalculator tc = new ThreadCalculator(this, index, species, start);
		executorService.submit(tc);
	}

	// END OF THREAD SECTION

	public static void main(String[] args) throws Exception {
		EngineConfiguration e = new EngineConfiguration();
		e.setConfigPath("./cfg");
		DistributionGenerator aag = new DistributionGenerator(null);
		aag.generateHSPEC();
	}

}
