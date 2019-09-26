package co.brainz.framework.scheduling.task

import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled

@Component
public class BWFScehduledTask_Kotlin {

	/*
	 * // FixedDelayScheduler() 실행완료 5초 후에 다음 FixedDelayScheduler()를 실행한다.
	 * 
	 * @Scheduled(fixedDelay = 5000) public fun FixedDelayScheduler() : Unit{
	 * println("스케줄링 동작"); } // FixedRateScheduler() 실행시작 5초 후에 다음
	 * FixedRateScheduler()를 실행한다.
	 * 
	 * @Scheduled(fixedRate = 5000) public fun FixedRateScheduler() :Unit{
	 * println("스케줄링 동작"); }
	 * 
	 * // Spring boot 시작 60초 경과 후에 FixedRateScheduler()를 실행한다.
	 * 
	 * @Scheduled(initialDelay = 60000, fixedRate = 5000) public fun
	 * FixedRateScheduler() : Unit{ println("스케줄링 동작"); }
	 * 
	 * // 매분 0초에 CronScheduler()를 실행한다. zone는 미지정하면 디폴트 timezone을 사용한다.
	 * 
	 * @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul") public fun
	 * CronScheduler() : Unit{ println("스케줄링 동작"); }
	 */

}
