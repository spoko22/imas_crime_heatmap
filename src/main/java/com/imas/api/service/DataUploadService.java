package com.imas.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imas.model.Crime;
import com.imas.util.CrimeSorter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.*;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by spoko on 22.06.16.
 */
@Service
public class DataUploadService {
    @Inject
    CrimeSorter crimeSorter;

    @Async
    public Future<Boolean> saveData(File file){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            StringBuilder wholeContent = new StringBuilder();
            for(String line; (line = br.readLine()) != null;){
                wholeContent.append(line);
            }

            List<Crime> crimes = (new ObjectMapper()).readValue(wholeContent.toString(), new TypeReference<List<Crime>>(){});
            crimes.forEach(crime -> crimeSorter.saveCrime(crime));
            return new AsyncResult<>(true);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new AsyncResult<>(false);
    }
}
