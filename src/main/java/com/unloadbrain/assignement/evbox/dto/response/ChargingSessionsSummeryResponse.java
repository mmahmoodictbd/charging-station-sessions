package com.unloadbrain.assignement.evbox.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChargingSessionsSummeryResponse {

    private long totalCount;

    private long startedCount;

    private long stoppedCount;

}
