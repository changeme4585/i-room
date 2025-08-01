package com.iroom.dashboard.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import com.iroom.dashboard.dto.request.DangerAreaRequest;
import com.iroom.dashboard.dto.response.DangerAreaResponse;
import com.iroom.dashboard.dto.response.PagedResponse;
import com.iroom.dashboard.entity.DangerArea;
import com.iroom.dashboard.repository.DangerAreaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class DangerAreaServiceTest {

	@Mock
	private DangerAreaRepository dangerAreaRepository;

	@InjectMocks
	private DangerAreaService dangerAreaService;

	private DangerArea dangerArea;
	private Pageable pageable;
	private Page<DangerArea> dangerAreaPage;

	@BeforeEach
	void setUp() {
		dangerArea = DangerArea.builder()
			.blueprintId(1L)
			.location("X:10, Y:20")
			.width(100.0)
			.height(200.0)
			.build();

		DangerArea dangerArea2 = DangerArea.builder()
			.blueprintId(2L)
			.location("X:50, Y:60")
			.width(150.0)
			.height(250.0)
			.build();

		pageable = PageRequest.of(0, 10);
		dangerAreaPage = new PageImpl<>(List.of(dangerArea, dangerArea2), pageable, 2);
	}

	@Test
	@DisplayName("위험구역 등록 성공")
	void createDangerAreaTest() {
		// given
		DangerAreaRequest request = new DangerAreaRequest(1L, "X:10, Y:20", 100.0, 200.0);
		given(dangerAreaRepository.save(any(DangerArea.class))).willReturn(dangerArea);

		// when
		DangerAreaResponse response = dangerAreaService.createDangerArea(request);

		// then
		assertThat(response.blueprintId()).isEqualTo(1L);
		assertThat(response.location()).isEqualTo("X:10, Y:20");
	}

	@Test
	@DisplayName("위험구역 수정 성공")
	void updateDangerAreaTest() {
		// given
		Long id = 1L;
		DangerAreaRequest request = new DangerAreaRequest(1L, "X:99, Y:88", 300.0, 400.0);
		given(dangerAreaRepository.findById(id)).willReturn(Optional.of(dangerArea));

		// when
		DangerAreaResponse response = dangerAreaService.updateDangerArea(id, request);

		// then
		assertThat(response.location()).isEqualTo("X:99, Y:88");
		assertThat(response.width()).isEqualTo(300.0);
		assertThat(response.height()).isEqualTo(400.0);
	}

	@Test
	@DisplayName("위험구역 수정 실패 - 존재하지 않음")
	void updateDangerAreaFail_NotFound() {
		// given
		Long id = 999L;
		DangerAreaRequest request = new DangerAreaRequest(1L, "X:10, Y:10", 100.0, 100.0);
		given(dangerAreaRepository.findById(id)).willReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> dangerAreaService.updateDangerArea(id, request))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 위험구역이 존재하지 않습니다.");
	}

	@Test
	@DisplayName("위험구역 삭제 성공")
	void deleteDangerAreaTest() {
		// given
		Long id = 1L;
		given(dangerAreaRepository.existsById(id)).willReturn(true);

		// when
		dangerAreaService.deleteDangerArea(id);

		// then
		verify(dangerAreaRepository).deleteById(id);
	}

	@Test
	@DisplayName("위험구역 삭제 실패 - 존재하지 않음")
	void deleteDangerAreaFail_NotFound() {
		// given
		Long id = 999L;
		given(dangerAreaRepository.existsById(id)).willReturn(false);

		// when & then
		assertThatThrownBy(() -> dangerAreaService.deleteDangerArea(id))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("해당 위험구역이 존재하지 않습니다.");
	}

	@Test
	@DisplayName("위험구역 전체 조회 성공")
	void getAllDangerAreasTest() {
		// given
		given(dangerAreaRepository.findAll(pageable)).willReturn(dangerAreaPage);

		// when
		PagedResponse<DangerAreaResponse> response = dangerAreaService.getAllDangerAreas(0, 10);

		// then
		assertThat(response.content()).hasSize(2);
		assertThat(response.totalElements()).isEqualTo(2);
		assertThat(response.content().get(0).location()).isEqualTo("X:10, Y:20");
	}
}