package com.ignite.Project2.service;

import com.ignite.Project2.compute.ComputeTask;
import com.ignite.Project2.model.CalculationResult;
import com.ignite.Project2.model.common.PriceRange;
import lombok.AllArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ComputeService {

    @IgniteInstanceResource
    private Ignite ignite;

    private ComputeTask computeTask;

    public CalculationResult compute() {

        Instant start = Instant.now();
        Map<PriceRange, Long> results = ignite.compute().execute(computeTask, null);
        Instant end = Instant.now();

        return CalculationResult.builder()
                .results(results)
                .calculationTimeInMillis(Duration.between(start, end).toMillis())
                .build();
    }
}
