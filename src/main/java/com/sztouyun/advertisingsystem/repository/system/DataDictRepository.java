package com.sztouyun.advertisingsystem.repository.system;

import com.sztouyun.advertisingsystem.model.system.DataDict;
import com.sztouyun.advertisingsystem.repository.BaseRepository;

import java.util.List;

public interface DataDictRepository extends BaseRepository<DataDict> {

    List<DataDict> findAllByTypeOrderById(Integer type);
}
