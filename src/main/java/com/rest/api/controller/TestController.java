package com.rest.api.controller;


import javax.servlet.http.HttpServletRequest;

import com.rest.api.domain.CommonCode;
import com.rest.api.repository.CommonCodeQueryRepository;
import com.rest.api.repository.CommonCodeRepository;
import com.rest.api.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TestController {

    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @Autowired
    private CommonCodeQueryRepository commonCodeQueryRepository;

    @PostMapping("/ip")
    public ResponseEntity<String> ip(HttpServletRequest request) {
        // 요청을 보낸 클라이언트의 IP주소를 반환합니다.
        return ResponseEntity.ok(request.getRemoteAddr());
    }

    @GetMapping("/test")
    public ResponseMessage test(HttpServletRequest request) {
        ResponseMessage ms = new ResponseMessage();

        CommonCode c = new CommonCode();
        c.setCodeId("AAAAA");
        c.setCodeName("첫번째 상위코드");

        commonCodeRepository.save(c);

        CommonCode L = commonCodeRepository.findByCodeId(c.getCodeId());

        CommonCode n = new CommonCode();

        List<CommonCode> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CommonCode code = new CommonCode();
            code.setCodeName("AAAAA의 자식 코드 " + i);
            code.setCodeId("AAAAA-BBBBB-" + i);
            code.setParent(L);
            commonCodeRepository.save(code);
            list.add(code);
        }
        L.setChildren(list);
        commonCodeRepository.save(L);

        c = new CommonCode();
        c.setCodeId("BBBBB");
        c.setCodeName("두번째 상위코드");

        commonCodeRepository.save(c);

        L = commonCodeRepository.findByCodeId("AAAAA-BBBBB-2");

        list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CommonCode code = new CommonCode();
            code.setCodeName("AAAAA-BBBBB-2의 자식 코드 " + i);
            code.setCodeId("AAAAA-BBBBB-CCCCC-" + i);
            code.setParent(L);
            commonCodeRepository.save(code);
            list.add(code);
        }
        L.setChildren(list);
        commonCodeRepository.save(L);

        ms.add("result",   commonCodeRepository.findAll().stream().filter(e-> e.getParent() == null).collect(Collectors.toList()));

        /*
        ms.add("result",   commonCodeRepository.findAll().stream().filter(e-> e.getParent() == null).collect(Collectors.toList()));

        or
           ms.add("result", commonCodeQueryRepository.findByAll());

        */

        return ms;

    }
}