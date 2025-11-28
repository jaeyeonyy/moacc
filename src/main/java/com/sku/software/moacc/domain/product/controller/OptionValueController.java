package com.sku.software.moacc.domain.product.controller;

import com.sku.software.moacc.domain.product.dto.request.OptionValueActiveRequest;
import com.sku.software.moacc.domain.product.dto.request.OptionValueRequest;
import com.sku.software.moacc.domain.product.dto.response.OptionValueResponse;
import com.sku.software.moacc.domain.product.service.OptionValueService;
import com.sku.software.moacc.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "OptionValue", description = "옵션 값 관리 API")
@RestController
@RequestMapping("/api/categories/{categoryCode}/option-types/{optionCode}/values")
@RequiredArgsConstructor
public class OptionValueController {

    private final OptionValueService optionValueService;

    @Operation(summary = "옵션 값 생성")
    @PostMapping
    public ResponseEntity<BaseResponse<OptionValueResponse>> createOptionValue(
            @PathVariable String categoryCode,
            @PathVariable String optionCode,
            @RequestBody @Valid OptionValueRequest request
    ) {
        OptionValueResponse response = optionValueService.create(categoryCode, optionCode, request);
        return ResponseEntity.ok(BaseResponse.success("옵션 값 생성에 성공했습니다.", response));
    }

    @Operation(summary = "옵션 값 목록 조회")
    @GetMapping
    public ResponseEntity<BaseResponse<List<OptionValueResponse>>> listOptionValues(
            @PathVariable String categoryCode,
            @PathVariable String optionCode
    ) {
        List<OptionValueResponse> responses = optionValueService.listByOption(categoryCode, optionCode);
        return ResponseEntity.ok(BaseResponse.success("옵션 값 목록 조회에 성공했습니다.", responses));
    }

    @Operation(summary = "옵션 값 활성/비활성 변경")
    @PatchMapping("/{valueCode}/active")
    public ResponseEntity<BaseResponse<OptionValueResponse>> setOptionValueActive(
            @PathVariable String categoryCode,
            @PathVariable String optionCode,
            @PathVariable String valueCode,
            @RequestBody @Valid OptionValueActiveRequest request
    ) {
        OptionValueResponse response = optionValueService.setActive(categoryCode, optionCode, valueCode, request.getActive());
        return ResponseEntity.ok(BaseResponse.success("옵션 값 활성 상태 변경에 성공했습니다.", response));
    }

}
