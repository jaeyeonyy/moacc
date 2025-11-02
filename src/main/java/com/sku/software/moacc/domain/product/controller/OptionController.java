package com.sku.software.moacc.domain.product.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Option", description = "옵션 관리 API")
@RestController
@RequestMapping("/api/categories/options")
@RequiredArgsConstructor
public class OptionController {

}
