package com.imas.api.controller;

import com.imas.api.service.DataUploadService;
import com.imas.model.Crime;
import com.imas.model.CrimeCategory;
import com.imas.repository.CrimeCategoryRepository;
import com.imas.repository.CrimeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by spoko on 22.06.16.
 */
@Controller
public class DataServingController {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @Inject
    CrimeCategoryRepository crimeCategoryRepository;
    @Inject
    CrimeRepository crimeRepository;

    @Inject
    DataUploadService dataUploadService;

    @RequestMapping(value = "/available-categories", method = RequestMethod.GET)
    public ResponseEntity<List<CrimeCategory>> getAllCategories(){
        return new ResponseEntity<>(crimeCategoryRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/upload-data", method = RequestMethod.POST)
    public ResponseEntity<String> uploadData(@RequestParam(value = "file", required = true) MultipartFile multipartFile) throws IOException {
        Date date = new Date();
        File file = File.createTempFile(sdf.format(date), "");
        file.deleteOnExit();
        multipartFile.transferTo(file);
        crimeRepository.deleteAll();
        crimeCategoryRepository.deleteAll();
        dataUploadService.saveData(file);
        return new ResponseEntity<>("File uploaded! Results of reading it should be available soon.", HttpStatus.OK);
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public ResponseEntity<List<Crime>> serveData(@RequestParam(value = "categoriesId", required = false) long[] categoriesId){
        List<Crime> returnedDataset;
        if(categoriesId != null && categoriesId.length > 0)
            returnedDataset = crimeRepository.findByCrimeCategoryIdIn(categoriesId);
        else
            returnedDataset = crimeRepository.findAll();
        return new ResponseEntity<>(returnedDataset, HttpStatus.OK);
    }
}
