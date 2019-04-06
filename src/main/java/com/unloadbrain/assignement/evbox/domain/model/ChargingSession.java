package com.unloadbrain.assignement.evbox.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@EqualsAndHashCode
public class ChargingSession {

    private String id;
    private String stationId;
    private Date startedAt;
    private Date endedAt;
}
