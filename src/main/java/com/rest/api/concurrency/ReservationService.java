package com.rest.api.concurrency;
import com.rest.api.config.RedisConfig;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;

@Service
public class ReservationService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationQueryRepository reservationQueryRepository;

    @Autowired
    private RedisConfig redisConfig;

    @Transactional
    public void save(Reservation reservation){
        boolean result = false;
        if(reservationQueryRepository.findById(reservation) == 0){
            reservationRepository.save(reservation);
            result = true;
        }else{
            result = false;
        }
        if(result){
            logger.info("기본 ----------- >  SUCCESS");
        }else{
            logger.info("기본  ----------- > FAIL");
        }
    }

    @Transactional
    public void save2(Reservation reservation){
        boolean result = false;
        // 레디스 락 데이터 생성 후 2초 대기, 3초후 락 해제
        RLock lock = redisConfig.redissonClient().getLock("sample");
        try {
            boolean isLocked = lock.tryLock(10 , TimeUnit.SECONDS);
            if (!isLocked) {
                // 락 획득에 실패했으므로 예외 처리
                result = false;
            }else{
                result = true;
            }

            if(reservationQueryRepository.findById(reservation) == 0){
                reservationRepository.save(reservation);
                result = true;
            }else{
                result = false;
            }

        } catch (InterruptedException e) {
            result = false;
            e.printStackTrace();
            // 쓰레드가 인터럽트 될 경우의 예외 처리
        } catch (Exception e){
            result = false;
            e.printStackTrace();
        }finally {
            // 락 해제
            lock.unlock();
            if(result){
                logger.info("RLock ----------- > SUCCESS");
            }else{
                logger.info("RLock ----------- >FAIL");
            }
        }
    }

}
