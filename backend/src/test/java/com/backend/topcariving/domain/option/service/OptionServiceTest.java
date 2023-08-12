package com.backend.topcariving.domain.option.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.topcariving.domain.archive.exception.InvalidAuthorityException;
import com.backend.topcariving.domain.archive.repository.CarArchivingRepository;
import com.backend.topcariving.domain.option.dto.request.SelectOptionsRequestDTO;
import com.backend.topcariving.domain.option.entity.CategoryDetail;
import com.backend.topcariving.domain.option.exception.InvalidCarOptionIdException;
import com.backend.topcariving.domain.option.repository.CarOptionRepository;
import com.sun.jdi.connect.Connector;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

	@Mock
	private CarOptionRepository carOptionRepository;

	@Mock
	private CarArchivingRepository carArchivingRepository;

	@InjectMocks
	private OptionService optionService;
	@Test
	void 상세_품목의_자식_옵션_조회_시_상세_품목이_아닌_옵션을_선택하면_에러가_발생한다() {
		// given
		given(carOptionRepository.existsByCarOptionIdAndCategoryDetail(1L, "상세 품목"))
			.willReturn(false);

		// when, then
		Assertions.assertThatThrownBy(() -> optionService.getSelectionDetails(1L))
			.isInstanceOf(InvalidCarOptionIdException.class);
	}

	@Test
	void 상세_품목_선택_시_내_차가_아닌_차에_옵션을_추가하면_에러가_발생한다() {
		// given
		given(carArchivingRepository.existsByUserIdAndArchivingId(1L, 22L))
			.willReturn(false);
		final SelectOptionsRequestDTO selectOptionsRequestDTO = new SelectOptionsRequestDTO(1L, List.of(103L, 110L), 22L);

		// when, then
		Assertions.assertThatThrownBy(() -> optionService.saveSelectionOptions(selectOptionsRequestDTO))
			.isInstanceOf(InvalidAuthorityException.class);
	}

	@Test
	void 상세_품목_선택_시_상세_품목이_아닌_옵션을_선택하면_에러가_발생한다() {
		// given
		given(carArchivingRepository.existsByUserIdAndArchivingId(1L, 22L))
			.willReturn(true);
		given(carOptionRepository.existsByCarOptionIdAndCategoryDetail(1L, CategoryDetail.SELECTED.getName()))
			.willReturn(false);
		final SelectOptionsRequestDTO selectOptionsRequestDTO = new SelectOptionsRequestDTO(1L, List.of(1L), 22L);

		// when, then
		Assertions.assertThatThrownBy(() -> optionService.saveSelectionOptions(selectOptionsRequestDTO))
			.isInstanceOf(InvalidCarOptionIdException.class);
	}

}