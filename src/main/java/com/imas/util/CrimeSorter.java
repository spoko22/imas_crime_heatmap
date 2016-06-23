package com.imas.util;

import com.imas.model.Crime;
import com.imas.model.CrimeCategory;
import com.imas.repository.CrimeCategoryRepository;
import com.imas.repository.CrimeRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by spoko on 22.06.16.
 */
@Component
public class CrimeSorter {
    @Inject
    CrimeCategoryRepository crimeCategoryRepository;
    @Inject
    CrimeRepository crimeRepository;

    private Map<String, CrimeCategory> existingCategories;

    private void createExistingCategories(){
        List<CrimeCategory> categories = crimeCategoryRepository.findAll();
        existingCategories = new HashMap<>();
        categories.forEach(category -> existingCategories.put(category.getCrimeName(), category));
    }

    public Crime saveCrime(Crime crime){
        if(existingCategories == null)
            createExistingCategories();

        CrimeCategory crimeCategory;
        if(existingCategories.containsKey(crime.getCrimeType()))
            crimeCategory = existingCategories.get(crime.getCrimeType());
        else {
            crimeCategory = new CrimeCategory();
            crimeCategory.setCrimeName(crime.getCrimeType());
            crimeCategory = crimeCategoryRepository.save(crimeCategory);
            existingCategories.put(crimeCategory.getCrimeName(), crimeCategory);
        }

        crime.setCrimeCategory(crimeCategory);
        return crimeRepository.save(crime);
    }

}
