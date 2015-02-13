package com.nokor.frmk.tools.scheduler;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.seuksa.frmk.tools.log.Log;

import com.nokor.frmk.security.context.SecApplicationContext;

/**
 * 
 * @author phirun.kong
 *
 */
public class NkrJobFactory implements JobFactory {

	private static final Log logger = Log.getInstance(NkrJobFactory.class);
	
	private SecApplicationContext secAppContext = null;

	NkrJobFactory(SecApplicationContext secAppContext) {
		this.secAppContext = secAppContext;
	}

	@Override
	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
		JobDetail jobDetails = bundle.getJobDetail();
		Job job = null;
		try {
			job = jobDetails.getJobClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("Fail to create job instance", e);
			throw new SchedulerException("Fail to create job instance", e);
		}
		if (job instanceof NkrJob) {
			@SuppressWarnings("rawtypes")
			NkrJob nkrJob = (NkrJob) job;
			nkrJob.setSecAppContext(secAppContext);
		} else {
			logger.warn("Job {" + job.toString() + "} is not instance of NkrJob.");
		}
		return job;
	}

}
