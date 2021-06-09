package com.rest.api.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
public class ConcurrencyTestController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    // https://webdevtechblog.com/%EC%9E%90%EB%B0%94-%EB%8F%99%EC%8B%9C%EC%84%B1%EC%9D%84-%ED%99%9C%EC%9A%A9%ED%95%9C-%EC%98%88%EC%95%BD-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EA%B5%AC%ED%98%84-ad39f7625cd9

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationQueryRepository reservationQueryRepository;

    @Resource
    RedisLockUtil redisLockUtil;

    /*
    // 아래 로직은 redisLockUtil 설정 때문인지,, Autowired가 먹지않고 new로 생성해야만 커넥션 이루어짐
    @Autowired
    RestTemplate restTemplate;
    */

    @RequestMapping("/api/test/buy")
    public String buy(@RequestParam String goodId , Reservation reservation) {
        long timeout = 15;

        TimeUnit timeUnit = TimeUnit.SECONDS;
        // UUID as value
        String lockValue = UUID.randomUUID().toString();
        if (redisLockUtil.lock(goodId, lockValue, timeout, timeUnit)) {
            // Business processing
            logger.info("Obtain the lock for business processing");
            try {

                if(reservationQueryRepository.findById(reservation) == 0){
                    reservationService.save2(reservation);
                    logger.info("@@@@@@@@@@@@@@@@@ SUCCESS @@@@@@@@@@@@@@@@@");
                }else{
                    logger.info("@@@@@@@@@@@@@@@@@ FAIL @@@@@@@@@@@@@@@@@");
                }

                // Sleep for 10 seconds
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Release the lock
            if (redisLockUtil.unlock(goodId, lockValue)) {
                logger.error("redis Distributed lock unlock exception key by" + goodId);
            }
            return "Purchase successful";
        }
        return "Please try again later";
    }

    @RequestMapping("/api/test/buybuybuy")
    @RedisLock(key = "lock_key", value = "lock_value")
    public String buybuybuy(@RequestParam(value = "goodId") String goodId) {
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Purchase successful";
    }

   @RequestMapping("/api/test/apply")
    public boolean apply(Reservation reservation) {
        return isAvailableCreate(reservation);
    }

    public synchronized boolean isAvailableCreate(Reservation reservation) {
        boolean result = false;
        try {

            if(reservationQueryRepository.findById(reservation) == 0){
                reservationRepository.save(reservation);
                result = true;
            }else{
                result = false;
            }

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            return result;
        }

    }

    @RequestMapping("/api/test/apply2")
    public Boolean apply2(@RequestBody Map<String, Object> vo) {
        Boolean result = false;
       try {
           Reservation r = new Reservation();
           r.setNo(Long.parseLong(vo.get("no").toString()));
           // result = isAvailableCreate(r);
           result = test(r);
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           return  result;
       }
    }


    @RequestMapping("/api/apply3")
    private Boolean apply3(Reservation reservation) {
        Boolean result = false;
        try {

            Map<String, Object> params = new HashMap<>();
            params.put("id", reservation.getId());
            params.put("no", reservation.getNo());
            RestTemplate restTemplate = new RestTemplate();
            String restApiUrl = "http://localhost:9090/api/test/apply2";
            // https://preamtree.tistory.com/167
            result = restTemplate.postForObject(restApiUrl,params , Boolean.class);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }

    }
    public boolean test(Reservation reservation) {
        try{

            reservationService.save(reservation);
            logger.info("##############################################################");

            Reservation r = new Reservation();
            r.setNo((long) 200);
            reservationService.save2(r);

        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }


}
