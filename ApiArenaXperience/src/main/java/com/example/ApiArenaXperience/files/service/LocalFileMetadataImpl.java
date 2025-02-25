package com.example.ApiArenaXperience.files.service;

import com.example.ApiArenaXperience.files.model.AbstractFileMetadata;
import com.example.ApiArenaXperience.files.model.FileMetadata;
import lombok.experimental.SuperBuilder;


@SuperBuilder
public class LocalFileMetadataImpl extends AbstractFileMetadata {

    public static FileMetadata of(String filename) {
        return LocalFileMetadataImpl.builder()
                .id(filename)
                .filename(filename)
                .build();
    }

}
