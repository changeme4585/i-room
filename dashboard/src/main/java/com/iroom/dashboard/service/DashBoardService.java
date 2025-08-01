package com.iroom.dashboard.service;

import com.iroom.dashboard.dto.response.DashBoardResponse;
import com.iroom.dashboard.entity.DashBoard;
import com.iroom.dashboard.repository.DashBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class DashBoardService {
    private final DashBoardRepository dashBoardRepository;
    public DashBoardResponse getDashBoard(String metricType){

        DashBoard dashBoard= dashBoardRepository.findTopByMetricTypeOrderByIdDesc(metricType);

        DashBoardResponse dashBoardResponse = new DashBoardResponse(
                dashBoard.getMetricType(),
                dashBoard.getMetricValue(),
                dashBoard.getRecordedAt()
        );


        return dashBoardResponse;
    }

}