package com.rest.api.concurrency;

import com.rest.api.config.RedisConfig;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@RestController
public class ConcurrencyTestController {

    // https://webdevtechblog.com/%EC%9E%90%EB%B0%94-%EB%8F%99%EC%8B%9C%EC%84%B1%EC%9D%84-%ED%99%9C%EC%9A%A9%ED%95%9C-%EC%98%88%EC%95%BD-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EA%B5%AC%ED%98%84-ad39f7625cd9

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationQueryRepository reservationQueryRepository;

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private RedisConfig redisConfig;

   @PostMapping("/apply")
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

    @PostMapping("/apply2")
    public Boolean apply2(@RequestBody Map<String, Object> vo) {
        Boolean result = false;
       try {
           Reservation r = new Reservation();
           r.setNo(Long.parseLong(vo.get("no").toString()));
           result = isAvailableCreate(r);

       }catch (Exception e){
           e.printStackTrace();
       }finally {
           return  result;
       }
    }

    @GetMapping("/test")
    public ResponseEntity<HashMap<String, Object>> getTicketList(@RequestBody Map<String, Object> vo) {
        HashMap<String,Object> data = new HashMap<>();
        data.put("key1","value");
        return new ResponseEntity<HashMap<String,Object>>((HashMap<String, Object>) data, HttpStatus.OK);
    }


    @PostMapping("/apply3")
    private Boolean apply3(Reservation reservation) {
        Boolean result = false;
        try {

            Map<String, Object> params = new HashMap<>();
            params.put("id", reservation.getId());
            params.put("no", reservation.getNo());
            RestTemplate restTemplate = restTemplateBuilder.build();
            String restApiUrl = "http://localhost:9090/apply2";
            result = restTemplate.postForObject(restApiUrl,params , Boolean.class);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }

    }


    // https://kkambi.tistory.com/196
    // redis 임시주석

    public boolean test(Reservation reservation) {
        boolean result = false;
        // 레디스 락 데이터 생성 후 2초 대기, 3초후 락 해제

        RLock lock = redisConfig.redissonClient().getLock("sample");
        try {
            boolean isLocked = lock.tryLock(2, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                // 락 획득에 실패했으므로 예외 처리
                //throw new Error("FAIL");
                return false;
            }

            if(reservationQueryRepository.findById(reservation) == 0){
                reservationRepository.save(reservation);
                result = true;
            }else{
                result = false;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            // 쓰레드가 인터럽트 될 경우의 예외 처리
        } finally {
            // 락 해제
            lock.unlock();
            return result;
        }

    }

}
