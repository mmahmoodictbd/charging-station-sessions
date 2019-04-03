package com.unloadbrain.assignement.evbox.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChargingStoppedSessionsSummeryResponse {

    private long stoppedCount;

}
