package com.github.open_academic_ad_hoc.controller;

import com.github.open_academic_ad_hoc.model.dto.QueryBuilderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryController {

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object selectReport(QueryBuilderDTO request) {
        return null;
    }


}
