package com.codegym.service;

import com.codegym.model.Tags;

public interface TagService {
    void save(Tags tags);

    Tags findByName(String name);

    Boolean existsByName(String name);
}
