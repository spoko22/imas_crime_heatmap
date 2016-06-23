package com.imas.repository;

import com.imas.BaseSpringTestConfig;
import com.imas.model.Crime;
import com.imas.model.CrimeCategory;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by spoko on 22.06.16.
 */
public class CrimeRepositoryTest extends BaseSpringTestConfig{
    @Inject
    CrimeRepository crimeRepository;
    @Inject
    CrimeCategoryRepository crimeCategoryRepository;

    CrimeCategory cc1,cc2,cc3;

    @Before
    public void setup(){
        cc1 = new CrimeCategory();
        cc1.setCrimeName("cc1");
        cc2 = new CrimeCategory();
        cc2.setCrimeName("cc2");
        cc3 = new CrimeCategory();
        cc3.setCrimeName("cc3");

        crimeCategoryRepository.save(cc1);
        crimeCategoryRepository.save(cc2);
        crimeCategoryRepository.save(cc3);
    }

    @Test
    public void testSavingCrimes(){
        Crime c1 = new Crime();
        c1.setCrimeCategory(cc1);
        Crime c2 = new Crime();
        c2.setCrimeCategory(cc1);
        Crime c3 = new Crime();
        c3.setCrimeCategory(cc2);
        Crime c4 = new Crime();
        c4.setCrimeCategory(cc3);

        crimeRepository.save(c1);
        crimeRepository.save(c2);
        crimeRepository.save(c3);
        crimeRepository.save(c4);
        List<Crime> allCrimes = crimeRepository.findAll();
        assertEquals(4, allCrimes.size());
    }

    @Test
    public void testGettingCrimesFilteredByCategories(){
        Crime c1 = new Crime();
        c1.setCrimeCategory(cc1);
        Crime c2 = new Crime();
        c2.setCrimeCategory(cc1);
        Crime c3 = new Crime();
        c3.setCrimeCategory(cc2);
        Crime c4 = new Crime();
        c4.setCrimeCategory(cc3);

        crimeRepository.save(c1);
        crimeRepository.save(c2);
        crimeRepository.save(c3);
        crimeRepository.save(c4);

        List<Crime> filteredCrimes = crimeRepository.findCrimesWithCategories(new String[]{"cc2", "cc3"});
        assertEquals(2, filteredCrimes.size());
        filteredCrimes = crimeRepository.findCrimesWithCategories(new String[]{"cc1"});
        assertEquals(2, filteredCrimes.size());
        filteredCrimes = crimeRepository.findCrimesWithCategories(new String[]{"cc10"});
        assertEquals(0, filteredCrimes.size());
    }
}
