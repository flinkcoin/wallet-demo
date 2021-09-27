package com.binarybazaar.wallet.main.init;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.glassfish.hk2.api.ServiceLocator;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public final class Hk2JobFactory implements JobFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(Hk2JobFactory.class);

    private final ServiceLocator serviceLocator;

    @Inject
    public Hk2JobFactory(final ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
        // Get the job detail so we can get the job class
        JobDetail jobDetail = triggerFiredBundle.getJobDetail();
        Class jobClass = jobDetail.getJobClass();

        try {
            // Get a new instance of that class from Guice so we can do dependency injection
            return (Job) serviceLocator.createAndInitialize(jobClass);
        } catch (Exception ex) {
            LOGGER.error("No OK!", ex);
            throw new UnsupportedOperationException(ex);
        }
    }
}
