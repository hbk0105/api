package com.rest.api;

import com.rest.api.concurrency.Reservation;
import com.rest.api.concurrency.ReservationQueryRepository;
import com.rest.api.concurrency.ReservationRepository;
import com.rest.api.concurrency.ReservationService;
import com.rest.api.config.RedisConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.time.LocalTime;
import java.util.concurrent.*;

@SpringBootTest
public class ConcurrentTest {

    private Logger log = LoggerFactory.getLogger(ConcurrentTest.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationQueryRepository reservationQueryRepository;

    @Autowired
    private ReservationService reservationService;

    private static final int THREAD_POOL_SIZE = 5;

    @Test
    public void test_가자() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            service.execute(() -> {

                Reservation reservation = new Reservation();
                reservation.setNo((long) 1111);
                reservationService.save(reservation);

                reservation = new Reservation();
                reservation.setNo((long) 9999);
                reservationService.save2(reservation);

            });
        }

        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    @Test
    public void test() throws ExecutionException, InterruptedException {

        /**
         * 스레드풀에 스레드를 많이 만들어 봤자
         * 하드웨어가 지원하는 CPU의 개수보다는 더 많이 돌릴 수 없음
         * 그래서 스레드풀의 스레드에는 CPU 개수만큼만 만들기
         */
        ExecutorService service = Executors.newFixedThreadPool(8);

        // 15개의 스레드를 관리하는 latch
        CountDownLatch latch = new CountDownLatch(15);

        for(int index = 1; index <= 15; index++){
            service.execute(new Runnable(){
                @Override
                public void run(){
                    //비즈니스 로직
                    latch.countDown(); //latch 하나씩 내려주기, 이걸 하지 않으면 작업이 안끝남
                }
            });
        }


        latch.await(); //모든 latch가 다끝날때까지 기다리기
        service.shutdown(); // 스레드 풀 종료

    }

    public void log(String msg) {
        System.out.println(LocalTime.now() + " ("
                + Thread.currentThread().getName() + ") " +  msg);
    }

}
