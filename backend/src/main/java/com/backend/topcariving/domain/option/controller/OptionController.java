package com.backend.topcariving.domain.option.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.topcariving.domain.option.dto.request.SelectOptionRequestDTO;
import com.backend.topcariving.domain.option.dto.request.SelectOptionsRequestDTO;
import com.backend.topcariving.domain.option.dto.response.selection.SelectionResponseDTO;
import com.backend.topcariving.domain.option.dto.response.trim.BasicOptionResponseDTO;
import com.backend.topcariving.domain.option.dto.response.trim.OptionResponseDTO;
import com.backend.topcariving.domain.option.service.OptionService;
import com.backend.topcariving.global.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "내 차 만들기 - 기본 포함 품목, 상세 품목, H Genuine, N performance 옵션")
@RequestMapping("/api/options")
@RequiredArgsConstructor
@RestController
public class OptionController {

	private final OptionService optionService;

	@GetMapping("/basics")
	@Operation(summary = "기본 옵션 전체 반환", description = "내 차 만들기에서 기본 옵션 전체를 반환한다")
	public BasicOptionResponseDTO getBasics() {
		return optionService.getBasics();
	}

	@GetMapping("/selections")
	@Operation(summary = "선택 옵션 전체 반환", description = "내 차 만들기에서 선택 옵션 전체를 반환한다")
	public List<OptionResponseDTO> getSelections() {
		return optionService.getSelections();
	}

	@GetMapping("/details/{carOptionId}")
	@Operation(summary = "선택 옵션 디테일 반환", description = "내 차 만들기에서 선택한 옵션의 세부 내역을 반환한다")
	public SelectionResponseDTO getSelectionDetails(@PathVariable Long carOptionId) {
		return optionService.getSelectionDetails(carOptionId);
	}

	@PostMapping("/selections")
	@Operation(summary = "선택 옵션 저장", description = "내 차 만들기에서 선택한 옵션들의 값을 저장하고, 차량 아카이브 PK 값을 반환함")
	@ResponseStatus(HttpStatus.CREATED)
	public SuccessResponse<Long> saveSelectionOptions(@RequestBody SelectOptionsRequestDTO selectOptionsRequestDTO) {
		return new SuccessResponse<>(optionService.saveSelectionOptions(selectOptionsRequestDTO));
	}

	@GetMapping("/accessories")
	@Operation(summary = "H Genuine Accessories 전체 반환", description = "내 차 만들기에서 H Genuine Accessories에 대한 옵션 전체를 반환한다")
	public List<OptionResponseDTO> getAccessories() {

		return null;
	}

	@PostMapping("/accessories")
	@Operation(summary = "H Genuine Accessories 저장", description = "내 차 만들기에서 선택한 옵션들의 값을 저장하고, 차량 아카이브 PK 값을 반환함")
	@ResponseStatus(HttpStatus.CREATED)
	public SuccessResponse<Long> saveAccessories(@RequestBody SelectOptionsRequestDTO requestDTO) {

		return null;
	}

	@GetMapping("/performances")
	@Operation(summary = "N Performance 전체 반환", description = "내 차 만들기에서 N Performance에 대한 옵션 전체를 반환한다")
	public List<OptionResponseDTO> getPerformances() {

		return null;
	}

	@PostMapping("/performances")
	@Operation(summary = "N Performance 저장", description = "내 차 만들기에서 선택한 옵션들의 값을 저장하고, 차량 아카이브 PK 값을 반환함")
	@ResponseStatus(HttpStatus.CREATED)
	public SuccessResponse<Long> savePerformances(@RequestBody SelectOptionRequestDTO selectOptionRequestDTO) {

		return null;
	}
}
