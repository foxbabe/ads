package com.sztouyun.advertisingsystem.repository.adPosition;

import com.sztouyun.advertisingsystem.model.adPosition.AdPosition;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdPositionRepository extends BaseRepository<AdPosition> {
    Page<AdPosition> findAll(Pageable pageable);
}
