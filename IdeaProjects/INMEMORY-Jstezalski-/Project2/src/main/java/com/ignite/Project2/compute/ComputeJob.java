package com.ignite.Project2.compute;

import com.ignite.Project2.model.Product;
import com.ignite.Project2.model.common.PriceRange;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.jetbrains.annotations.Nullable;

import javax.cache.Cache;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.ignite.Project2.config.IgniteConfig.CACHE_NAME;

class ComputeJob extends ComputeJobAdapter {

    private static final BigDecimal FIFTY = new BigDecimal("50.00");
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");

    @IgniteInstanceResource
    private Ignite ignite;

    @Nullable
    @Override
    public Map<PriceRange, Long> execute() throws IgniteException {
        IgniteCache<String, Product> productCache = ignite.cache(CACHE_NAME);
        Iterable<Cache.Entry<String, BinaryObject>> entries = productCache.<String, BinaryObject>withKeepBinary().localEntries(CachePeekMode.ALL);

        return StreamSupport.stream(entries.spliterator(), false)
                .map(Cache.Entry::getValue)
                .collect(Collectors.groupingBy(product -> segregate(product.field("listPrice")),
                        Collectors.mapping(product -> product.field("listPrice"),
                                Collectors.counting())));
    }

    private PriceRange segregate(BigDecimal value) {
        if (value == null)
            return PriceRange.NONE;
        else if (value.compareTo(FIFTY) < 0)
            return PriceRange.ONE;
        else if (value.compareTo(ONE_HUNDRED) < 0)
            return PriceRange.TWO;
        else return PriceRange.THREE;
    }

}
