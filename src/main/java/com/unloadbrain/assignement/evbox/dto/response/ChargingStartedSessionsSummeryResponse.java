package com.unloadbrain.assignement.evbox.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChargingStartedSessionsSummeryResponse {

    private long startedCount;

}
