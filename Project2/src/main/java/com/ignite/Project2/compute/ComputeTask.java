package com.ignite.Project2.compute;

import com.ignite.Project2.model.Product;
import com.ignite.Project2.model.common.PriceRange;
import lombok.AllArgsConstructor;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskAdapter;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ComputeTask extends ComputeTaskAdapter<Product, Map<PriceRange, Long>> {

    @Override
    public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> nodes, @Nullable Product product) {
        return nodes.stream().collect(Collectors.toMap(node -> new ComputeJob(), Function.identity()));
    }

    @Nullable
    @Override
    public Map<PriceRange, Long> reduce(List<ComputeJobResult> results) {
        Map<PriceRange, Long> resultMap = new HashMap<>();

        for (ComputeJobResult result : results) {
            Map<PriceRange, Long> rangeMap = result.getData();

            rangeMap.forEach(
                    (key, value) -> resultMap.merge(key, value, Long::sum)
            );
        }

        return resultMap;
    }

}

