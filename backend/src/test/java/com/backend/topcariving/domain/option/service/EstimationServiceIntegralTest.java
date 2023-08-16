package com.backend.topcariving.domain.option.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.backend.topcariving.config.TestSupport;
import com.backend.topcariving.domain.archive.entity.MyCar;
import com.backend.topcariving.domain.archive.repository.MyCarRepository;
import com.backend.topcariving.domain.option.dto.request.esitmation.EstimationChangeRequestDTO;
import com.backend.topcariving.domain.option.dto.response.estimation.OptionSummaryDTO;
import com.backend.topcariving.domain.option.dto.response.estimation.SummaryResponseDTO;

@SpringBootTest
@Transactional
public class EstimationServiceIntegralTest extends TestSupport {

	@Autowired
	private EstimationService estimationService;

	@Autowired
	private MyCarRepository myCarRepository;

	@Nested
	@DisplayName("내 차량 요약 보기")
	class Summary {
		@Test
		void 요약_보기_결과가_제대로_나와야_한다() {
			// given
			Long userId = 1L;
			Long archivingId = 1L;

			// when
			final SummaryResponseDTO summary = estimationService.summary(userId, archivingId);

			// then
			final Map<String, List<OptionSummaryDTO>> options = summary.getOptions();
			final List<OptionSummaryDTO> colors = options.get("색상");
			final List<OptionSummaryDTO> model = options.get("모델");
			final List<OptionSummaryDTO> trims = options.get("트림");
			final List<OptionSummaryDTO> selected = options.get("선택품목");

			softAssertions.assertThat(colors).as("색상은 외장, 내장 색상이 있어야함").hasSize(2);
			softAssertions.assertThat(model).as("모델은 하나여야만 함").hasSize(1);
			softAssertions.assertThat(trims).as("트림의 갯수는 최대 3개일 수 있음 (모델 제외)").hasSize(3);
			softAssertions.assertThat(selected).as("해당 유저가 선택한 선택옵션은 2개임").hasSize(2);
		}

		@Test
		void 요약보기에서_변경된_결과가_제대로_반영이_되어야한다() {
			// given
			final EstimationChangeRequestDTO request = EstimationChangeRequestDTO.builder()
				.archivingId(1L)
				.userId(1L)
				.optionIds(List.of(105L, 106L, 107L))
				.build();

			// when
			final Long archivingId = estimationService.changeOptions(request);

			// then
			softAssertions.assertThat(archivingId).as("Archiving ID의 반환값 확인").isEqualTo(1L);

			final Optional<MyCar> findCar1 = myCarRepository.findByArchivingIdAndCarOptionId(1L,
				105L);
			softAssertions.assertThat(findCar1).as("105번이 제대로 저장이 되어야한다").isPresent();
			final Optional<MyCar> findCar2 = myCarRepository.findByArchivingIdAndCarOptionId(1L,
				106L);
			softAssertions.assertThat(findCar2).as("106번이 제대로 저장이 되어야한다").isPresent();
			final Optional<MyCar> findCar3 = myCarRepository.findByArchivingIdAndCarOptionId(1L,
				107L);
			softAssertions.assertThat(findCar3).as("107번이 제대로 저장이 되어야한다").isPresent();

			final Optional<MyCar> findCar4 = myCarRepository.findByArchivingIdAndCarOptionId(1L,
				103L);
			softAssertions.assertThat(findCar4).as("103번은 기존값으로 제대로 삭제 되어야한다").isEmpty();
		}

	}
}