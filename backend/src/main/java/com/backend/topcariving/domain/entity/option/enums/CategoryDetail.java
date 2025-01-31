package com.backend.topcariving.domain.entity.option.enums;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CategoryDetail {

	MODEL("모델"),
	ENGINE("엔진"),
	BODY_TYPE("바디타입"),
	DRIVING_METHOD("구동방식"),

	EXTERIOR_COLOR("외장색상"),
	INTERIOR_COLOR("내장색상"),
	INTERIOR_COLOR_IMAGE("내장색상-이미지"),

	POWER_TRAIN("파워트레인/성능"),
	SMART_SAFE_TECHNOLOGY("지능형 안전기술"),
	SAFETY("안전"),
	EXTERIOR("외관"),
	INTERIOR("내장"),
	SEAT("시트"),
	CONVENIENCE("편의"),
	MULTI_MEDIA("멀티미디어"),

	SELECTED("상세 품목"),
	H_GENUINE_ACCESSORIES("H Genuine Accessories"),
	N_PERFORMANCE("N performance"),

	EMPTY("empty");

	private final String name;

	CategoryDetail(final String name) {
		this.name = name;
	}

	public static CategoryDetail valueOfName(String name) {
		return Arrays.stream(CategoryDetail.values())
			.filter(categoryDetail -> Objects.equals(name, categoryDetail.getName()))
			.findFirst()
			.orElse(EMPTY);
	}

	@JsonValue
	public String getName() {
		return name;
	}
}
