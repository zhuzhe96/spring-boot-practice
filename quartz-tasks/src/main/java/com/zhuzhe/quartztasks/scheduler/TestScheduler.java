package com.zhuzhe.quartztasks.scheduler;

import com.zhuzhe.quartztasks.job.PrintWordsJob;
import java.util.concurrent.TimeUnit;
import org.quartz.JobBuilder;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class TestScheduler {

  public static void main(String[] args) {
    var factory = new StdSchedulerFactory();
    try {
      // 创建调度器
      var scheduler = factory.getScheduler();

      // 使用Job构建JobDetail
      var jobDetail = JobBuilder.newJob(PrintWordsJob.class).withIdentity("job1", "group1").build();

      // 创建触发器,定义触发规则
      var trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "triggerGroup1")
          .startNow() //立即起效
          .withSchedule(SimpleScheduleBuilder.simpleSchedule()
              .withIntervalInSeconds(1) //每隔1s执行一次
              .repeatForever() //一直执行
          ).build();

      // 启动调度器
      scheduler.scheduleJob(jobDetail, trigger);
      System.out.println("------------scheduler start------------");
      scheduler.start();

      // 关闭调度器
      TimeUnit.MINUTES.sleep(1);
      scheduler.shutdown();
      System.out.println("-------------scheduler end-------------");
    } catch (SchedulerException | InterruptedException e) {
      throw new RuntimeException(e);
    }

  }

}
