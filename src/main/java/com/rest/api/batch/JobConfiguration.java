package com.rest.api.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JobConfiguration {


    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    private LockTableRepository lockTableRepository;

    private String instanceId = "play-batch";

    @Bean
    public Job job(){
        return jobBuilderFactory.get("job")
                .start(step1()) //STEP 1 실행
                .on("FAILED")// STEP 1 결과가 FAILED 일 경우
                .end()// 종료
                .from(step1()) // STEP1 의 결과로부터
                .on("*") // FAILED 를 제외한 모든 경우
                .to(step2()) // STEP 2로 이동 후 실행
                .on("FAILED")// STEP 2의 결과가 FAILED 일 경우
                .end()// 종료
                .from(step2()) // STEP2 의 결과로부터
                .on("*") // FAILED 를 제외한 모든 경우
                .to(step3()) // STEP 3로 이동 후 실행
                .next(step4()) // STEP 3의 결과에 상관없이 STEP 4 실행
                .on("*") // STEP4 의 모든 결과에 상관없이
                .end() // FLOW 종료
                .end() // JOB 종료
                .build();
    }

    /**
     * 이중화를 대비해서 LOCK TABLE 생성, 사용 여부 체크
     * @return
     */
    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .tasklet((stepContribution, chunkContext) -> {

                    log.info("======= 배치 시작 =======");
                    LockTable lockTable = Optional
                            .ofNullable(lockTableRepository.findByEmail("play-batch"))
                            .orElse(LockTable
                                    .builder()
                                    .email(instanceId)
                                    .useYn(false)
                                    .checkDataTime(LocalDateTime.now())
                                    .build());
                    log.info("======= Lock Table 사용 여부:{},", lockTable.isUseYn());
                    if(lockTable.isUseYn()){
                        log.info("======= Table 사용 중으로 종료 ");
                        stepContribution.setExitStatus(ExitStatus.FAILED);
                    }else{
                        lockTable.setUseYn(true);
                        lockTableRepository.save(lockTable);
                    }
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step step2(){
        return stepBuilderFactory.get("step2")
                .tasklet((stepContribution, chunkContext) -> {
                    int itemCnt = 0;
                    log.info("======= 처리할 Item 개수 : {}", itemCnt);
                    if(0 == itemCnt){
                        log.info("======= 처리할 Item 이 없어 종료 ");
                        LockTable lockTable = lockTableRepository.findByEmail(instanceId);
                        lockTable.setUseYn(false);
                        lockTable.setCheckDataTime(LocalDateTime.now());
                        //stepContribution.setExitStatus(ExitStatus.FAILED);
                    }
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step step3(){
        return stepBuilderFactory.get("step3")
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("## step3 :: ");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step step4(){
        return stepBuilderFactory.get("step4")
                .tasklet((stepContribution, chunkContext) -> {
                    LockTable lockTable = lockTableRepository.findByEmail(instanceId);
                    lockTable.setUseYn(false);
                    lockTable.setCheckDataTime(LocalDateTime.now());
                    lockTableRepository.save(lockTable);
                    log.info("======= 배치 종료 =======");
                    return RepeatStatus.FINISHED;
                }).build();
    }

}