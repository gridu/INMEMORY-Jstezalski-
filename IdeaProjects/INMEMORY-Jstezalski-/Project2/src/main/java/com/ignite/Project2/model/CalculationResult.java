package com.ignite.Project2.model;

import com.ignite.Project2.model.common.PriceRange;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CalculationResult {
    private Map<PriceRange, Long> results;
    private long calculationTimeInMillis;
}
