package com.nokor.frmk.tools.scheduler;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.seuksa.frmk.tools.log.Log;

import com.nokor.frmk.security.context.SecApplicationContext;


/**
 * 
 * @author phirun.kong
 *
 */

public class SchedulerManager {
	
	private static final Log logger = Log.getInstance(SchedulerManager.class);
	
	private final static String ATTR_SCHEDULE_MANAGER = "@schedulerManager@";
	
	private SchedulerFactory schedulerFactory = null;
	
	public SchedulerManager() {
		
	}
	
	public void init(SecApplicationContext secAppContext) throws Exception {
		try {
			logger.debug("Initialize SchedulerManager().");
			if (schedulerFactory == null) {
				schedulerFactory = new StdSchedulerFactory();				
			}
		} catch (Exception e) {
			throw new Exception("Fail to initial scheduler: " + e.getMessage(), e);
		}
	}
	
	public static SchedulerManager getSchedulerManager(SecApplicationContext secAppContext) throws Exception {
		SchedulerManager res = (SchedulerManager) secAppContext.getAttribute(ATTR_SCHEDULE_MANAGER);
		if(res == null) {
			res = new SchedulerManager();
			secAppContext.putAttribute(ATTR_SCHEDULE_MANAGER, res);
		}
		return res;
	}
	
	public void standBy() throws Exception {
		try {
			logger.debug("StandBy SchedulerManager().");
			getScheduler().standby();
		} catch (SchedulerException e) {
			throw new Exception("Fail to stabd-by scheduler: " + e.getMessage(), e);
		}
	}
	
	public void start(String schedulerName) throws Exception {
		try {
			logger.debug("Start SchedulerManager({" + schedulerName + "}).");
			getScheduler().start();
		} catch (SchedulerException e) {
			throw new Exception("Fail to start scheduler: " + e.getMessage(), e);
		}
	}
	
	public void start() throws Exception {
		try {
			logger.debug("Start SchedulerManager().");
			getScheduler().start();
		} catch (SchedulerException e) {
			throw new Exception("Fail to start scheduler: " + e.getMessage(), e);
		}
	}
	
	public void stop() throws Exception {
		try {
			logger.debug("Stop SchedulerManager().");
			getScheduler().shutdown(true);
		} catch (SchedulerException e) {
			throw new Exception("Fail to start scheduler: " + e.getMessage(), e);
		}
	}
	
	public Scheduler getScheduler() throws Exception {
		try {
			return schedulerFactory.getScheduler();
		} catch (SchedulerException e) {
			throw new Exception("Fail to get scheduler: " + e.getMessage(), e);
		}
	}
	
}
