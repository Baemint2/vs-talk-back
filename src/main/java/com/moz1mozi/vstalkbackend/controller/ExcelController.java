package com.moz1mozi.vstalkbackend.controller;

import com.moz1mozi.vstalkbackend.ApiResponse;
import com.moz1mozi.vstalkbackend.service.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/excel")
public class ExcelController {

    private final ExcelService excelService;
    @RequestMapping("/upload")
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file,
                                      @RequestParam("type") String type) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        excelService.process(file, type, username);
        return ApiResponse.ok("업로드 완료");
    }
}
