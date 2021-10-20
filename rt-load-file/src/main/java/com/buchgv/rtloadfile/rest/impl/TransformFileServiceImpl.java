package com.buchgv.rtloadfile.rest.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.buchgv.rtloadfile.rest.metadata.Entity;
import com.buchgv.rtloadfile.rest.metadata.Record;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The class responsible to handle transformation
 * of received file to the format required for further
 * representation to user
 * 
 */
@Component
public class TransformFileServiceImpl {
    
    // The logger
    private static final Logger logger = LoggerFactory.getLogger(TransformFileServiceImpl.class);
    
    public List<Record> transformFile(byte[] fileBytes) throws Exception{
        String data = new String(fileBytes);
        String[] rows = data.split("\n");
        List<Record> recordsList = new ArrayList<Record>();

        List<Entity> entityConfigList = initiateEntityConfig();

        logger.info("received input:", (Object)rows);

        for (int i = 0; i < rows.length; i++) {
            String row = rows[i].trim();

            if(row.length() != 38){
                logger.error("bad length record", row);
                continue;
            }

            Record record = new Record();
            record.setDocType(buildStringValue(entityConfigList.get(0), row));
            record.setCompanyID(buildIntValue(entityConfigList.get(1), row));
            record.setDate(buildDateValue(entityConfigList.get(2), row));
            record.setDocID(buildIntValue(entityConfigList.get(3), row));
            record.setSign((buildStringValue(entityConfigList.get(4), row)));
            record.setAmount(buildIntValue(entityConfigList.get(5), row));
            record.setId(i);

            recordsList.add(record);
        }

        return recordsList;
    }

    /**
     * Create transformation data types
     * 
     * @return
     */
    private List<Entity> initiateEntityConfig() {
        List<Entity> entityConfigList = new ArrayList<Entity>();

        Entity docTypeEnt = new Entity();
        docTypeEnt.setLength(1);
        docTypeEnt.setStartingIndex(0);

        Entity companyIDEnt = new Entity();
        companyIDEnt.setLength(9);
        companyIDEnt.setStartingIndex(docTypeEnt.getStartingIndex()+docTypeEnt.getLength());

        Entity dateEnt = new Entity();
        dateEnt.setLength(8);
        dateEnt.setStartingIndex(companyIDEnt.getStartingIndex()+companyIDEnt.getLength());

        Entity docIDEnt = new Entity();
        docIDEnt.setLength(9);
        docIDEnt.setStartingIndex(dateEnt.getStartingIndex()+dateEnt.getLength());

        Entity signEnt = new Entity();
        signEnt.setLength(1);
        signEnt.setStartingIndex(docIDEnt.getStartingIndex()+docIDEnt.getLength());

        Entity amountEnt = new Entity();
        amountEnt.setLength(10);
        amountEnt.setStartingIndex(signEnt.getStartingIndex()+signEnt.getLength());

        entityConfigList.add(docTypeEnt);
        entityConfigList.add(companyIDEnt);
        entityConfigList.add(dateEnt);
        entityConfigList.add(docIDEnt);
        entityConfigList.add(signEnt);
        entityConfigList.add(amountEnt);

        return entityConfigList;
    }
    
    private String buildStringValue(Entity entityConfig, String row){
        String value = row.substring(entityConfig.getStartingIndex(), entityConfig.getStartingIndex() + entityConfig.getLength());

        return value;
    }

    private Integer buildIntValue(Entity entityConfig, String row){
        String value = row.substring(entityConfig.getStartingIndex(), entityConfig.getStartingIndex() + entityConfig.getLength());

        return Integer.parseInt(value);
    }

    private LocalDate buildDateValue(Entity entityConfig, String row){
        String value = row.substring(entityConfig.getStartingIndex(), entityConfig.getStartingIndex() + entityConfig.getLength());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return LocalDate.parse(value, formatter);
    }
}
