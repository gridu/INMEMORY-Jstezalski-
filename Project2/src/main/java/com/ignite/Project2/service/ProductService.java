package com.ignite.Project2.service;

import com.ignite.Project2.model.CalculationResult;
import com.ignite.Project2.model.common.PriceRange;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    private DataService dataService;

    private ComputeService computeService;

    @PostConstruct
    public void start() {

        System.out.println("Data entry started");

        dataService.loadDataFromCsvFile();

        System.out.println("Data entered");

        System.out.println("Commuting started");

        CalculationResult compute = computeService.compute();

        System.out.println("Time of computing " + compute.getCalculationTimeInMillis() + " millis.");

        System.out.println("Results:" +
                "\n0-49.99  = " + compute.getResults().get(PriceRange.ONE) +
                "\n50-99.99 = " + compute.getResults().get(PriceRange.TWO) +
                "\n100+     = " + compute.getResults().get(PriceRange.THREE));

    }

}
