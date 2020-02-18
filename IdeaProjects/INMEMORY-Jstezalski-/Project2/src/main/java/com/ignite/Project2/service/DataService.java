package com.ignite.Project2.service;

import com.ignite.Project2.model.Product;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.StreamSupport;

import static com.ignite.Project2.config.IgniteConfig.CACHE_NAME;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DataService {

    private static final String CSV_FILE_PATH = "jcpenney_com-ecommerce_sample.csv";

    @IgniteInstanceResource
    private Ignite ignite;

    public void loadDataFromCsvFile() {

        try (Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH))) {
            CsvToBean csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Product.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(false)
                    .build();

            IgniteCache<String, Product> cache = ignite.getOrCreateCache(CACHE_NAME);
            try (IgniteDataStreamer<String, Product> streamer = ignite.dataStreamer(cache.getName())) {

                StreamSupport.stream((((Iterable<Product>) csvToBean).spliterator()), false)
                        .forEach(product -> streamer.addData(product.getUniqId(), product));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}