/**
 * 
 */
package org.gcube.vremanagement.executor.pluginmanager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.acme.HelloWorldPlugin;
import org.acme.HelloWorldPluginDeclaration;
import org.gcube.common.scope.api.ScopeProvider;
import org.gcube.vremanagement.executor.api.types.LaunchParameter;
import org.gcube.vremanagement.executor.api.types.Scheduling;
import org.gcube.vremanagement.executor.exception.PluginStateNotRetrievedException;
import org.gcube.vremanagement.executor.exception.UnableToInterruptTaskException;
import org.gcube.vremanagement.executor.persistence.SmartExecutorPersistenceConnector;
import org.gcube.vremanagement.executor.persistence.SmartExecutorPersistenceConnectorTest;
import org.gcube.vremanagement.executor.persistence.SmartExecutorPersistenceFactory;
import org.gcube.vremanagement.executor.plugin.PluginState;
import org.gcube.vremanagement.executor.scheduler.SmartExecutorScheduler;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Luca Frosini (ISTI - CNR) http://www.lucafrosini.com/
 */
public class SmartExecutorSchedulerTest {

	private static Logger logger = LoggerFactory.getLogger(SmartExecutorSchedulerTest.class);
	
	public static final String START = "START";
	public static final String END = "END";
	
	public static SmartExecutorPersistenceConnector pc;
	
	@BeforeClass 
	public static void prepare() throws Exception{
		try {
			ScopeProvider.instance.set(SmartExecutorPersistenceConnectorTest.GCUBE_DEVSEC_SCOPE);
			/*
			pc = new JDBCPersistenceConnector(".");
			SmartExecutorPersistenceConnector.setPersistenceConnector(pc);
			*/
			pc = SmartExecutorPersistenceFactory.getPersistenceConnector();
		} catch (Exception e) {
			logger.error("Unable to Create SmartExecutorPersistenceConnector");
			throw e;
		}
	}
	
	@AfterClass
	public static void close() throws Exception{
		try {
			SmartExecutorPersistenceFactory.closePersistenceConnector();
		} catch (Exception e) {
			logger.error("Unable to Close JDBCPersistenceConnector");
			throw e;
		}
	}
	
	public UUID scheduleTest(Scheduling scheduling, Long sleepTime) throws Exception {
		Map<String, Object> inputs = new HashMap<String, Object>();
		if(sleepTime==null){
			sleepTime = new Long(10*1000); // 10 sec = 10 * 1000 millisec
		}
		inputs.put(HelloWorldPlugin.SLEEP_TIME, sleepTime);
		inputs.put("Test UUID", UUID.randomUUID());
		logger.debug("Inputs : {}", inputs);
		
		LaunchParameter parameter = new LaunchParameter(HelloWorldPluginDeclaration.NAME, inputs);
		parameter.setScheduling(scheduling);
		
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		UUID uuid = smartExecutorScheduler.schedule(parameter);
		logger.debug("Scheduled Job with ID {}", uuid);
		
		return uuid;
	}
	
	/* DeprecatedHelloWorldPlugin dependency needed
	@Test
	public void deprecatedConstructorTest() throws Exception {
		Map<String, Object> inputs = new HashMap<String, Object>();
		Long sleepTime = new Long(10*1000); // 10 sec = 10 * 1000 millisec
		inputs.put(DeprecatedHelloWorldPlugin.SLEEP_TIME, sleepTime);
		inputs.put("Test UUID", UUID.randomUUID());
		logger.debug("Inputs : {}", inputs);
		
		LaunchParameter parameter = new LaunchParameter(DeprecatedHelloWorldPluginDeclaration.class.newInstance(), inputs);
		parameter.setScheduling(null);
		
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		UUID uuid = smartExecutorScheduler.schedule(parameter);
		logger.debug("Scheduled Job with ID {}", uuid);
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 12000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		PluginState pluginState = pc.getLastPluginInstanceState(uuid);
		Assert.assertEquals(PluginState.DONE, pluginState);
	}
	*/
	
	@Test
	public void schedulingTest() throws Exception {
		UUID uuid = scheduleTest(null, null);
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 12000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		PluginState pluginState = pc.getLastPluginInstanceState(uuid);
		Assert.assertEquals(PluginState.DONE, pluginState);
	}
	
	@Test
	public void earlyStopTest() throws Exception {
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		UUID uuid = scheduleTest(null, null);
		try {
			smartExecutorScheduler.stop(uuid, true, false);
		} catch(UnableToInterruptTaskException e){
			logger.error("UnableToInterruptTaskException this is the normal behaviour.", e);
			return;
		}
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 12000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		
		try{
			PluginState pluginState = pc.getPluginInstanceState(uuid, 5);
			Assert.assertEquals(PluginState.STOPPED, pluginState);
		}catch(PluginStateNotRetrievedException e){
			// OK
			logger.error("PluginStateNotRetrievedException this can be acceptable in some tests", e);
		}
		
	}
	
	@Test
	public void middleStopTest() throws Exception {
		UUID uuid = scheduleTest(null, null);
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 2000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		
		smartExecutorScheduler.stop(uuid, true, false);
		
		startTime = Calendar.getInstance().getTimeInMillis();
		endTime = startTime;
		while(endTime <=  (startTime + 10000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		PluginState pluginState = pc.getLastPluginInstanceState(uuid);
		Assert.assertEquals(PluginState.STOPPED, pluginState);
	}
	
	@Test
	public void lateStopTest() throws Exception {
		UUID uuid = scheduleTest(null, null);
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 12000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		smartExecutorScheduler.stop(uuid, true, false);
		
		PluginState pluginState = pc.getLastPluginInstanceState(uuid);
		Assert.assertEquals(PluginState.DONE, pluginState);
	}
	
	
	@Test
	public void doubleLaunchfirstStoppedSchedulingTest() throws Exception {
		UUID first = scheduleTest(null, null);
		logger.debug("First scheduled id {}", first);
		UUID second = scheduleTest(null, null);
		logger.debug("Second scheduled id {}", second);
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 2000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		smartExecutorScheduler.stop(first, true, false);
		
		startTime = Calendar.getInstance().getTimeInMillis();
		endTime = startTime;
		while(endTime <=  (startTime + 12000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		
		PluginState pluginState = pc.getLastPluginInstanceState(first);
		Assert.assertEquals(PluginState.STOPPED, pluginState);
		
		pluginState = pc.getLastPluginInstanceState(second);
		Assert.assertEquals(PluginState.DONE, pluginState);
	}
	
	@Test
	public void delayed() throws Exception {
		Scheduling scheduling = new Scheduling(20);
		UUID uuid = scheduleTest(scheduling, new Long(10 * 1000));
		
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 83 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		smartExecutorScheduler.stop(uuid, true, false);
		
		startTime = Calendar.getInstance().getTimeInMillis();
		endTime = startTime;
		while(endTime <=  (startTime + 30 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}

		for(int i=1; i<5; i++){
			PluginState pluginState = pc.getPluginInstanceState(uuid, i);
			Assert.assertEquals(PluginState.DONE, pluginState);
		}
		
		PluginState pluginState = pc.getPluginInstanceState(uuid, 5);
		Assert.assertEquals(PluginState.STOPPED, pluginState);
		
	}
	
	@Test
	public void delayedPreviousMustBeTerminated() throws Exception {
		Scheduling scheduling = new Scheduling(20, true);
		UUID uuid = scheduleTest(scheduling, new Long(22 * 1000));
		
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 80 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		smartExecutorScheduler.stop(uuid, true, false);
		
		startTime = Calendar.getInstance().getTimeInMillis();
		endTime = startTime;
		while(endTime <=  (startTime + 30 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}

		for(int i=1; i<5; i++){
			PluginState pluginState = pc.getPluginInstanceState(uuid, i);
			if(i%2!=0){
				Assert.assertEquals(PluginState.DONE, pluginState);
			}else{
				Assert.assertEquals(PluginState.DISCARDED, pluginState);
			}
		}
		
		PluginState pluginState = pc.getPluginInstanceState(uuid, 5);
		Assert.assertEquals(PluginState.STOPPED, pluginState);
		
	}
	
	@Test
	public void delayedAllPreviousMustBeTerminated() throws Exception {
		Scheduling scheduling = new Scheduling(20, true);
		UUID uuid = scheduleTest(scheduling, new Long(45 * 1000));
		
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 1.5 * 60 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		smartExecutorScheduler.stop(uuid, true, false);
		
		startTime = Calendar.getInstance().getTimeInMillis();
		endTime = startTime;
		while(endTime <=  (startTime + 30 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}

		PluginState[] expectedStates = new PluginState[]{
				PluginState.DONE, 
				PluginState.DISCARDED,
				PluginState.DISCARDED,
				PluginState.STOPPED
		};
		
		for(int i=0; i<expectedStates.length; i++){
			PluginState pluginState = pc.getPluginInstanceState(uuid, i+1);
			Assert.assertEquals(expectedStates[i], pluginState);
		}
		
		PluginState pluginState = pc.getPluginInstanceState(uuid, 5);
		Assert.assertEquals(PluginState.DISCARDED, pluginState);
		
	}
	
	@Test(expected=PluginStateNotRetrievedException.class)
	public void delayedExpMaxtimes() throws Exception {
		Scheduling scheduling = new Scheduling(20, 3);
		UUID uuid = scheduleTest(scheduling, new Long(10 * 1000));
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 1.5 * 60 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		
		for(int i=0; i<3; i++){
			PluginState pluginState = pc.getPluginInstanceState(uuid, i+1);
			Assert.assertEquals(PluginState.DONE, pluginState);
		}
		
		pc.getPluginInstanceState(uuid, 4);

	}
	
	@Test(expected=PluginStateNotRetrievedException.class)
	public void delayedExpTimeLimits() throws Exception {
		
		Calendar firstStartTime = Calendar.getInstance();
		
		Calendar stopTime = Calendar.getInstance();
		stopTime.setTimeInMillis(firstStartTime.getTimeInMillis());
		stopTime.add(Calendar.SECOND, 10);
		firstStartTime.add(Calendar.SECOND, 5);
		
		Scheduling scheduling = new Scheduling(20, 0, firstStartTime, stopTime);
		logger.debug("Scheduling : {}", scheduling);
		
		UUID uuid = scheduleTest(scheduling, new Long(10 * 1000));

		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 30 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		
		PluginState pluginState = pc.getPluginInstanceState(uuid, 1);
		Assert.assertEquals(PluginState.DONE, pluginState);
		
		pc.getPluginInstanceState(uuid, 2);
		
	}
	
	
	
	@Test
	public void cronExp() throws Exception {
		CronExpression cronExpression = new CronExpression("0/20 * * ? * *");
		Scheduling scheduling = new Scheduling(cronExpression);
		UUID uuid = scheduleTest(scheduling, new Long(10 * 1000));
		
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 1.5 * 60 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		smartExecutorScheduler.stop(uuid, true, false);
		
		startTime = Calendar.getInstance().getTimeInMillis();
		endTime = startTime;
		while(endTime <=  (startTime + 30 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}

		for(int i=1; i<5; i++){
			PluginState pluginState = pc.getPluginInstanceState(uuid, i);
			Assert.assertEquals(PluginState.DONE, pluginState);
		}
		
		try{
			PluginState pluginState = pc.getPluginInstanceState(uuid, 5);
			Assert.assertEquals(PluginState.STOPPED, pluginState);
		}catch(PluginStateNotRetrievedException e){
			// OK
		}
	}
	
	
	@Test
	public void cronExpPreviousMustBeTerminated() throws Exception {
		CronExpression cronExpression = new CronExpression("0/20 * * ? * *");
		Scheduling scheduling = new Scheduling(cronExpression, true);
		UUID uuid = scheduleTest(scheduling, new Long(30 * 1000));
		
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 1.5 * 60 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		smartExecutorScheduler.stop(uuid, true, false);
		
		startTime = Calendar.getInstance().getTimeInMillis();
		endTime = startTime;
		while(endTime <=  (startTime + 30 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}

		for(int i=1; i<5; i++){
			PluginState pluginState = pc.getPluginInstanceState(uuid, i);
			if(i%2!=0){
				Assert.assertEquals(PluginState.DONE, pluginState);
			}else{
				Assert.assertEquals(PluginState.DISCARDED, pluginState);
			}
		}
		try{
			PluginState pluginState = pc.getPluginInstanceState(uuid, 5);
			Assert.assertEquals(PluginState.STOPPED, pluginState);
		}catch(PluginStateNotRetrievedException e){
			// OK
		}
		
	}
	
	@Test
	public void cronExpAllPreviousTerminated() throws Exception {
		CronExpression cronExpression = new CronExpression("0/20 * * ? * *");
		Scheduling scheduling = new Scheduling(cronExpression, true);
		UUID uuid = scheduleTest(scheduling, new Long(45 * 1000));
		
		SmartExecutorScheduler smartExecutorScheduler = SmartExecutorScheduler.getInstance();
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime +  1.5 * 60 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		smartExecutorScheduler.stop(uuid, true, false);
		
		startTime = Calendar.getInstance().getTimeInMillis();
		endTime = startTime;
		while(endTime <=  (startTime + 20 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}

		PluginState[] expectedStates = new PluginState[]{
				PluginState.DONE, 
				PluginState.DISCARDED,
				PluginState.DISCARDED,
				PluginState.STOPPED
		};

		
		for(int i=0; i<expectedStates.length; i++){
			PluginState pluginState = pc.getPluginInstanceState(uuid, i+1);
			Assert.assertEquals(expectedStates[i], pluginState);
		}
		
		try{
			PluginState pluginState = pc.getPluginInstanceState(uuid, 5);
			Assert.assertEquals(PluginState.DISCARDED, pluginState);
		}catch(PluginStateNotRetrievedException e){
			// OK
		}
	}
	
	@Test(expected=PluginStateNotRetrievedException.class)
	public void cronExpMaxtimes() throws Exception {
		CronExpression cronExpression = new CronExpression("0/20 * * ? * *");
		Scheduling scheduling = new Scheduling(cronExpression, 3);
		UUID uuid = scheduleTest(scheduling, new Long(10 * 1000));
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 1.5 * 60 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		
		for(int i=0; i<3; i++){
			PluginState pluginState = pc.getPluginInstanceState(uuid, i+1);
			Assert.assertEquals(PluginState.DONE, pluginState);
		}
		
		pc.getPluginInstanceState(uuid, 4);
		
	}
	
	@Test(expected=PluginStateNotRetrievedException.class)
	public void cronExpTimeLimits() throws Exception {
		CronExpression cronExpression = new CronExpression("0/20 * * ? * *");
		Calendar firstStartTime = Calendar.getInstance();
		
		Calendar stopTime = Calendar.getInstance();
		stopTime.setTimeInMillis(firstStartTime.getTimeInMillis());
		stopTime.add(Calendar.SECOND, 20);
		
		Scheduling scheduling = new Scheduling(cronExpression, 0, firstStartTime, stopTime);
		logger.debug("Scheduling : {}", scheduling);
		
		UUID uuid = scheduleTest(scheduling, new Long(10 * 1000));

		long startTime = Calendar.getInstance().getTimeInMillis();
		long endTime = startTime;
		while(endTime <=  (startTime + 30 * 1000)){
			endTime = Calendar.getInstance().getTimeInMillis();
		}
		
		PluginState pluginState = pc.getPluginInstanceState(uuid, 1);
		Assert.assertEquals(PluginState.DONE, pluginState);
		
		pc.getPluginInstanceState(uuid, 2);
		
	}
	
}
