package com.buchgv.rtloadfile.rest;

import java.util.ArrayList;
import java.util.List;

import com.buchgv.rtloadfile.rest.impl.TransformFileServiceImpl;
import com.buchgv.rtloadfile.rest.metadata.Record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for file receiving and further transformation
 * 
 */
@RestController
public class TransformFileService
{
    // The logger
    private static final Logger logger = LoggerFactory.getLogger(TransformFileService.class);
    
    // The handler for file transformation
    @Autowired    
    protected TransformFileServiceImpl tsfImpl;
    
    @PostMapping(value = "/transformFile", consumes = "multipart/form-data")
    @ResponseBody
    public List<Record> transformFileHandler(@RequestParam("file") MultipartFile file) throws Exception {
        if(!file.isEmpty()){
            try{
                byte[] fileBytes = file.getBytes();
                List<Record> records = tsfImpl.transformFile(fileBytes);

                return records;
            }catch(Exception e){
                logger.error("Fatal exception during app running", e);
            }
        }else{
            logger.info("Empty file provided");
        }

        return new ArrayList<Record>();
    }
}