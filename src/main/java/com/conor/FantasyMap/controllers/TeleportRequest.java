package com.conor.FantasyMap.controllers;

import com.conor.FantasyMap.models.IPoint;
import com.conor.FantasyMap.models.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeleportRequest {
    private String destinationName;
}
