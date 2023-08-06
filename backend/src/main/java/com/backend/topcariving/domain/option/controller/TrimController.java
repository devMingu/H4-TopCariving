package com.backend.topcariving.domain.option.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.topcariving.domain.option.dto.TrimResponseDTO;
import com.backend.topcariving.domain.option.dto.ModelResponseDTO;
import com.backend.topcariving.domain.option.service.TrimService;
import com.backend.topcariving.global.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="내 차 만들기 - 옵션")
@RequestMapping("/api/options")
@RestController
@RequiredArgsConstructor
public class TrimController {

	private final TrimService trimService;

	@GetMapping("/trim/model")
	@Operation(summary = "트림 옵션 전체 반환", description = "내 차 만들기에서 트림 옵션 전체를 반환한다.")
	public List<ModelResponseDTO> getModels() {
		return trimService.getModels();
	}

	// TODO: 선택한 옵션 넣기
	@PostMapping("/trim")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponse(responseCode = "201", description = "성공하면, 아카이빙 PK 값을 반환함")
	@Operation(summary = "트림 옵션 저장", description = "내 차 만들기에서 트림을 선택한 값을 저장하고, 아카이빙 PK 값을 반환한다")
	@Parameter(name = "carOptionId", description = "사용자가 선택한 모델의 ID 값")
	public SuccessResponse<Long> saveTrim(@RequestParam Long carOptionId) {

		return null;
	}

	@GetMapping("/trims/engines")
	@Operation(summary = "엔진 옵션 전체 반환", description = "내 차 만들기에서 모든 엔진 옵션을 반환한다")
	public List<TrimResponseDTO> getEngines() {

		return null;
	}

	@PostMapping("/trims/engines")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "엔진 옵션 저장", description = "내 차 만들기에서 엔진을 선택한 값을 저장하고, 차량 아카이빙 PK 값을 반환한다")
	@Parameter(name = "carOptionId", description = "사용자가 선택한 엔진의 ID값")
	public SuccessResponse<Long> saveEngine(@RequestParam Long carOptionId) {

		return null;
	}

	@GetMapping("/trims/body-types")
	@Operation(summary = "바디 타입 전체 반환", description = "내 차 만들기에서 바디 타입 반환")
	public List<TrimResponseDTO> getBodyTypes() {

		return null;
	}

	@PostMapping("/trims/body-types")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "바디 타입 저장", description = "내 차 만들기에서 바디 타입 선택한 값을 저장하고, 차량 아카이빙 PK 값을 반환한다")
	@Parameter(name = "carOptionId", description = "사용자가 선택한 bodyType의 ID값")
	public SuccessResponse<Long> saveBodyTypes(@RequestParam Long carOptionId) {

		return null;
	}

	@GetMapping("/trims/driving-methods")
	@Operation(summary = "구동 방식 전체 반환", description = "내 차 만들기에서 구동 방식 반환")
	public List<TrimResponseDTO> getDrivingMethods() {

		return null;
	}

	@PostMapping("/trims/driving-methods")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "구동 방식 저장", description = "내 차 만들기에서 구동 방식 선택한 값을 저장하고, 차량 아카이빙 PK 값을 반환한다")
	@Parameter(name = "carOptionId", description = "사용자가 선택한 구동 방식의 ID값")
	public SuccessResponse<Long> saveDrivingMethod(@RequestParam Long carOptionId) {

		return null;
	}
}
