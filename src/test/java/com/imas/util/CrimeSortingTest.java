package com.imas.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imas.BaseSpringTestConfig;
import com.imas.model.Crime;
import com.imas.model.CrimeCategory;
import com.imas.repository.CrimeCategoryRepository;
import com.imas.repository.CrimeRepository;
import com.imas.util.CrimeSorter;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by spoko on 22.06.16.
 */
public class CrimeSortingTest extends BaseSpringTestConfig{
    String inputString = "[{\"lat\": 53.51, \"lng\": -2.25, \"count\": 5, \"crimeType\":\"a\"}, {\"lat\": 53.45, \"lng\": -2.27, \"count\": 1, \"crimeType\":\"c\"}, " +
                          "{\"lat\": 53.47, \"lng\": -2.54, \"count\": 1, \"crimeType\":\"c\"}, {\"lat\": 53.45, \"lng\": -2.24, \"count\": 2, \"crimeType\":\"b\"}, " +
                          "{\"lat\": 53.48, \"lng\": -2.16, \"count\": 1, \"crimeType\":\"c\"}, {\"lat\": 53.37, \"lng\": -2.11, \"count\": 1, \"crimeType\":\"c\"}, " +
                          "{\"lat\": 53.54, \"lng\": -2.38, \"count\": 22,\"crimeType\":\"a\"},{\"lat\": 53.39, \"lng\": -2.37, \"count\": 1, \"crimeType\":\"c\"}, " +
                          "{\"lat\": 53.51, \"lng\": -2.24, \"count\": 5, \"crimeType\":\"c\"}, {\"lat\": 53.59, \"lng\": -2.24, \"count\": 1, \"crimeType\":\"z\"}]";

    @Inject
    CrimeSorter crimeSorter;
    @Inject
    CrimeRepository crimeRepository;
    @Inject
    CrimeCategoryRepository crimeCategoryRepository;

    private ObjectMapper objectMapper;

    @Before
    public void setup(){
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testParsingJson() throws IOException {
        List<Crime> list = objectMapper.readValue(inputString, new TypeReference<List<Crime>>(){});
        assertEquals(10, list.size());
        assertEquals(Crime.class, list.get(0).getClass());
    }

    @Test
    public void testSortingAndSavingCrimes() throws IOException {
        List<Crime> list = objectMapper.readValue(inputString, new TypeReference<List<Crime>>(){});
        list.forEach(crime -> {
            Crime savedCrime = crimeSorter.saveCrime(crime);
            assertNotNull(savedCrime);
            assertTrue(savedCrime.getId() > 0);
            assertNotNull(savedCrime.getCrimeCategory());
        });

        List<Crime> savedCrimes = crimeRepository.findAll();
        assertEquals(10, savedCrimes.size());
        List<CrimeCategory> savedCategories = crimeCategoryRepository.findAll();
        assertEquals(4, savedCategories.size());
    }

}
