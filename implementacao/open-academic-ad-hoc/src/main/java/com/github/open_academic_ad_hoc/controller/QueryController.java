package com.github.open_academic_ad_hoc.controller;

import com.github.open_academic_ad_hoc.model.dto.QueryBuilderDTO;
import com.github.open_academic_ad_hoc.service.QueryBuilderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class QueryController {

    private final QueryBuilderService service;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object selectReport(@RequestBody QueryBuilderDTO request) {

        Pair<String, Object> pair = service.generateReport(request);

        return pair;
    }

}
