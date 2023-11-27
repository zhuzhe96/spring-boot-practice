package com.zhuzhe.quartztasks.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 打印内容Job
 */
public class PrintWordsJob implements Job {

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    var time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    System.out.printf("PrintWordsJob start at: %s , prints: Hello Quartz Job-%s %n", time,
        new Random().nextInt(100));
    System.out.println(jobExecutionContext.getJobDetail().getJobDataMap().get("job"));
  }
}
