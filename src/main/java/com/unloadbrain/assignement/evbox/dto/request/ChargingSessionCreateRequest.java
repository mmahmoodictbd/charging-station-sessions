package com.unloadbrain.assignement.evbox.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargingSessionCreateRequest {

    @Size(min = 1, max = 255)
    private String stationId;

}
