# 스케줄링 (Scheduling)
---

## 1. Spring Scheduler

스프링 스케줄러는 스프링 프레임워크 3.1부터 기본 내장되어 있어서 스프링부트 2.1.7 (스프링 5.1.9)를 사용하는 BWF에서는 라이브러리 추가가 따로 필요하지 않다.  
@Scheduled 어노테이션 또는 XML 설정을 통한 2가지 방법이 적용 가능하다.

## 2. 설정 방법

### 1) BWF에서 사용하는 스케줄 작업을 직접 추가하는 방법

BWF(Brainz Web Framework)를 개발하면서 실행에 필요한 필수 스케줄링은 코드에 아래 2가지 방법으로 직접 기술하여 스케줄링 할 수 있다.  
기본적으로는 @Scheduled 어노테이션을 이용하여 /src/main/java/com/brainz/framework/scheduling/task/BWFScheduledTask 파일에 작성하는 것을 원칙으로 한다.  
고객사에 따라 주기 등이 변경될 여지가 있는 항목들은 XML을 통해서 관리하도록 한다.

#### 직접 실행 할 메소드에 @Scheduled 어노테이션으로 기술하는 방법

실행 할 메소드에 직접 @Scheduled 어노테이션으로 기술하여 스프링으로 하여금 스케줄 대상 메서드 임을 알게 해준다.  
(application.properties 에 cron 을 기술하여 참고하는 방법도 있다.)

```java
@Component 
public class Scheduler {
    // FixedDelayScheduler() 실행완료 5초 후에 다음 FixedDelayScheduler()를 실행한다.
    @Scheduled(fixedDelay = 5000)
    public void FixedDelayScheduler(){
        System.out.println("스케줄링 동작");
    }
    // FixedRateScheduler() 실행시작 5초 후에 다음 FixedRateScheduler()를 실행한다. 
    @Scheduled(fixedRate = 5000)
    public void FixedRateScheduler(){
        System.out.println("스케줄링 동작");
    }
    // Spring boot 시작 60초 경과 후에 FixedRateScheduler()를 실행한다. 
    @Scheduled(initialDelay = 60000, fixedRate = 5000)
    public void FixedRateScheduler(){
        System.out.println("스케줄링 동작");
    }
    // 매분 0초에 CronScheduler()를 실행한다. zone는 미지정하면 디폴트 timezone을 사용한다. 
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void CronScheduler(){
        System.out.println("스케줄링 동작");
    }
} 
```
#### XML설정 파일에 추가하는 방법

XML 파일에 클래스를 등록하고 해당 클래스에 task scedule을 등록하는 방법으로, 단순히 설정을 변경할때 소스의 컴파일없이 재기동만으로 사용할 수 있다.  

```xml
    <!-- job bean -->
    <bean id="scheduler" class="co.brainz.scheduling.Scheduler" />
    
    <task:scheduled-tasks> <!-- scheduled job list -->
        <task:scheduled ref="scheduler" method="executeJob" cron="0/30 * * * * ?"/>
        <!-- add more job here -->
    </task:scheduled-tasks>
```
```java
@Component 
public class Scheduler {
    public void executeJob(){
        System.out.println("스케줄링 동작");
    }
} 
```

### 2) 어플리케이션에서 사용하는 스케줄링 작업을 DB에 쿼리, 클래스 등을 추가하는 방법

BWF를 이용한 어플리케이션에서 필요한 스케줄러 작업은 아래와 같이 BWF에 준비된 BWF_SCHED_TASK_MST 테이블에 저장되어 사용할 수 있다.
BWF_SCHED_TASK_MST 테이블 컬럼에 대한 세부내용은 아래와 같다.

|컬럼명|논리명|의미|
|--|--|--|
|task_id|작업 아이디|스케줄링 시 사용할 Uniq ID.|
|task_type|작업 유형|스케줄링할 task의 타입. query, class|
|execute_class|실행 클래스|task타입이 class일 경우 실행되는 class명으로 package를 포함해서 등록한다.|
|execute_query|실행 쿼리|task타입이 query일 경우 실행되는 쿼리|
|execute_cycle_type|실행 주기 유형|cron, fixedDelay, fixedRate|
|execute_cycle_period|실행 주기 간격|실행 주기 타입이 fixedDelay, fixedRate 일 경우 사용되는 주기로 millisecond 단위로 등록한다.|
|cron_expression|cron표현식|실행 주기 타입이 cron일 경우 사용되는 cron표현식|

DB 쿼리를 바로 입력하거나 클래스를 등록하여 사용할 수 있으며 클래스일 경우, 아래 예제와 같이 패키지(co.brainz.framework.scheduling.task)에 넣고 Runnable 을 implements 하여 구현한다.  
실제로는 package를 포함한 클래스명을 DB에 등록하기 때문에 위치는 상관없으나 특별한 사유가 없는 한 위의 패키지로 관리하는 것을 원칙으로 한다.  

```java
package co.brainz.framework.scheduling.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SampleTask implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(SampleTask.class);

    @Override
    public void run() {
        logger.info("Sample Task Execute!!");
    }

}
```

## 3. 샘플설명

아래 예제는 스케줄 작업을 화면을 통해서 추가, 변경, 삭제하는 경우를 간단하게 나타내고 있다.  
스케줄 작업에 대한 UI 제공은 BWF를 이용해서 개발되는 어플리케이션의 몫이며 필요한 경우 화면을 제공해야 할 것이다.

```java
package co.brainz.framework.sample.scheduling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.brainz.framework.scheduling.model.ScheduleTask;
import co.brainz.framework.scheduling.repository.ScheduleTaskRepository;
import co.brainz.framework.scheduling.service.ScheduleTaskService;

@RestController
public class SchedulingController {
    
    @Autowired
    ScheduleTaskService scheduleTaskService;
    
    @Autowired
    ScheduleTaskRepository scheduleTaskRepository;
    
    @PostMapping("/sample/scheduling/add")
    public String addTask(@RequestBody ScheduleTask task) {
        ScheduleTask savedTask = this.scheduleTaskRepository.save(task);
        this.scheduleTaskService.addTaskToScheduler(savedTask);
        return "스케줄링 TASK 추가";
    }
    
    @PostMapping("/sample/scheduling/update")
    public String updateTask(@RequestBody ScheduleTask task) {
        ScheduleTask savedTask = this.scheduleTaskRepository.save(task);
        this.scheduleTaskService.removeTaskFromScheduler(savedTask.getTaskId());
        this.scheduleTaskService.addTaskToScheduler(savedTask);
        return "스케줄링  갱신";
    }
    
    @GetMapping("/sample/scheduling/delete/{id}")
    public String removeTask(@PathVariable long id) {
        this.scheduleTaskRepository.deleteById(id);
        this.scheduleTaskService.removeTaskFromScheduler(id);
        return "스케줄링  삭제";
    }
}
```

## 4. CRON 표현식

어노테이션이나 XML에서 TASK의 실행 주기/시간을 설정할 때 사용되는 CRON 표현식은 다음과 같이 사용한다.
```java
cron = "0 0 1 1 * ?"
```

총 7자리로 표현할 수 있으며, 7번째 자리인 '년도'는 옵션으로 사용한다. 왼쪽부터 다음과 같은 의미를 갖는다.

|자릿수 |의미  |사용값|
|--|--|--|
|1|초, SECONDS|0 ~ 59, -, *, /|
|2|분, MINIUTES|0 ~ 59, -, *, /|
|3|시간, HOURS|0 ~ 59, -, *, /|
|4|일, DAY OF MONTH|1 ~ 31, -, *, ?, /, L, W|
|5|월, MONTH|1 ~ 12, JAN ~ DEC, -, *, /|
|6|요일, DAY OF WEEK|1 ~ 7, SUN ~ SAT, -, *, ?, /, L, #|
|7|연도, YEARS (옵션)|1970 ~ 2099, -, *, /|

사용되는 특수문자들은 아래와 같은 의미를 가진다.  

|특수문자 |의미  |
|--|--|
|? | 조건 없음 일, 요일에서만 사용가능|
|* | 모든 값|  
|-| 범위 지정할때 사용 (시작범위-끝범위)|  
|/|시작시간/단위 때까지 [ 0/1 1분마다] | 
|L|일, 요일에서만 사용가능  |
|W|가장 가까운 평일을 찾는다(월~금) 일 에서만 사용 가능 | 


## 5. 참고 사이트

[https://spring.io/guides/gs/scheduling-tasks/](https://spring.io/guides/gs/scheduling-tasks/)

[http://blog.naver.com/PostView.nhn?blogId=lovemema&logNo=140200056062](http://blog.naver.com/PostView.nhn?blogId=lovemema&logNo=140200056062)

[https://www.baeldung.com/spring-scheduled-tasks](https://www.baeldung.com/spring-scheduled-tasks)