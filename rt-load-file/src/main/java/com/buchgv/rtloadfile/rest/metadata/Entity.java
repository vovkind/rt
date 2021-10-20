package com.buchgv.rtloadfile.rest.metadata;

import lombok.Data;

/**
 * Describes record received from FE and not transformed yet
 * 
 */
@Data
public class Entity {
    // length of the substring in the file
    private int length;

    // starting index of the substring in the file
    private int startingIndex;
}
