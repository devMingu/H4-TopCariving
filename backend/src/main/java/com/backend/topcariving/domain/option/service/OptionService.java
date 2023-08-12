package com.backend.topcariving.domain.option.service;

import static com.backend.topcariving.domain.option.entity.CategoryDetail.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.topcariving.domain.archive.entity.MyCar;
import com.backend.topcariving.domain.archive.exception.InvalidAuthorityException;
import com.backend.topcariving.domain.archive.repository.CarArchivingRepository;
import com.backend.topcariving.domain.archive.repository.MyCarRepository;
import com.backend.topcariving.domain.option.dto.request.SelectOptionsRequestDTO;
import com.backend.topcariving.domain.option.dto.response.selection.SelectionDetailDTO;
import com.backend.topcariving.domain.option.dto.response.selection.SelectionResponseDTO;
import com.backend.topcariving.domain.option.dto.response.trim.BasicOptionResponseDTO;
import com.backend.topcariving.domain.option.dto.response.trim.OptionResponseDTO;
import com.backend.topcariving.domain.option.entity.CarOption;
import com.backend.topcariving.domain.option.entity.CategoryDetail;
import com.backend.topcariving.domain.option.exception.InvalidCarOptionIdException;
import com.backend.topcariving.domain.option.repository.CarOptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionService {

	private final CarOptionRepository carOptionRepository;
	private final CarArchivingRepository carArchivingRepository;
	private final MyCarRepository myCarRepository;

	public BasicOptionResponseDTO getBasics() {
		Map<String, List<OptionResponseDTO>> basicOptions = new HashMap<>();
		List<CarOption> carOptions = carOptionRepository.findByCategory(BASIC_OPTION.getName());
		for (CarOption carOption : carOptions) {
			if (!basicOptions.containsKey(carOption.getCategoryDetail())) {
				List<OptionResponseDTO> value = new ArrayList<>();
				basicOptions.put(carOption.getCategoryDetail(), value);
			}
			List<OptionResponseDTO> value =  basicOptions.get(carOption.getCategoryDetail());
			value.add(OptionResponseDTO.from(carOption));
		}

		return BasicOptionResponseDTO.of(basicOptions);
	}

	public List<OptionResponseDTO> getSelections() {
		List<CarOption> selectedOptions = carOptionRepository.findByCategoryDetailAndParentOptionId(SELECTED.getName(), null);

		return selectedOptions.stream()
			.map(OptionResponseDTO::from)
			.collect(Collectors.toList());
	}

	public SelectionResponseDTO getSelectionDetails(Long carOptionId) {
		verifyCarOptionId(SELECTED, carOptionId);

		List<CarOption> childSelectedOptions = carOptionRepository.findByParentOptionId(carOptionId);
		CarOption carOption = carOptionRepository.findByCarOptionId(carOptionId)
			.orElseThrow(InvalidCarOptionIdException::new);
		List<SelectionDetailDTO> selectionDetailDTOs = childSelectedOptions.stream()
			.map(SelectionDetailDTO::from)
			.collect(Collectors.toList());

		return SelectionResponseDTO.of(carOption, selectionDetailDTOs, null);
	}

	public Long saveSelectionOptions(SelectOptionsRequestDTO selectOptionsRequestDTO) {
		Long userId = selectOptionsRequestDTO.getUserId();;
		List<Long> selectedOptionIds = selectOptionsRequestDTO.getIds();
		Long archivingId = selectOptionsRequestDTO.getArchivingId();

		verifyCarArchiving(userId, archivingId);
		verifyCarOptionId(SELECTED, selectedOptionIds);

		selectedOptionIds.stream().forEach(selectedOptionId -> {
			MyCar myCar = MyCar.builder()
				.carOptionId(selectedOptionId)
				.archivingId(archivingId)
				.build();

			myCarRepository.save(myCar);
		});

		return archivingId;
	}

	private void verifyCarArchiving(Long userId, Long archivingId) {
		if (!carArchivingRepository.existsByUserIdAndArchivingId(userId, archivingId)) {
			throw new InvalidAuthorityException();
		}
	}

	private void verifyCarOptionId(CategoryDetail categoryDetail, Long carOptionId) {
		if (!carOptionRepository.existsByCarOptionIdAndCategoryDetail(carOptionId, categoryDetail.getName())) {
			throw new InvalidCarOptionIdException();
		}
	}

	private void verifyCarOptionId(CategoryDetail categoryDetail, List<Long> carOptionIds) {
		carOptionIds.stream().forEach(carOptionId -> {
			if (!carOptionRepository.existsByCarOptionIdAndCategoryDetail(carOptionId, categoryDetail.getName())) {
				throw new InvalidCarOptionIdException();
			}
		});
	}
}
