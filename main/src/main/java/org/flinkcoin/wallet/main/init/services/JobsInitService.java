package org.flinkcoin.wallet.main.init.services;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flinkcoin.wallet.main.init.AppInit;
import org.quartz.Scheduler;
import org.flinkcoin.wallet.main.init.Hk2JobFactory;
import org.quartz.SchedulerException;

@Singleton
public class JobsInitService implements AppInit {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobsInitService.class);

    private static final String SERVICE_NAME = "Jobs init";

    private final Scheduler scheduler;
    private final Hk2JobFactory hk2JobFactory;

    @Inject
    public JobsInitService(Scheduler scheduler, Hk2JobFactory hk2JobFactory) {
        this.scheduler = scheduler;
        this.hk2JobFactory = hk2JobFactory;
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    @Override
    public void exec() throws SchedulerException {
        scheduler.setJobFactory(hk2JobFactory);
    }

}
