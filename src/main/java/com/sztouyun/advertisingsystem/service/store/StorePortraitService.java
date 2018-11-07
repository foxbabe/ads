package com.sztouyun.advertisingsystem.service.store;

import com.sztouyun.advertisingsystem.mapper.StoreNearByMapper;
import com.sztouyun.advertisingsystem.viewmodel.store.storePortrait.EnvironmentTypeStoreStatistics;
import com.sztouyun.advertisingsystem.model.store.QStorePortrait;
import com.sztouyun.advertisingsystem.model.store.StorePortrait;
import com.sztouyun.advertisingsystem.repository.store.StorePortraitRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StorePortraitService extends BaseService{
    private final QStorePortrait qStorePortrait = QStorePortrait.storePortrait;

    @Autowired
    private StorePortraitRepository storePortraitRepository;

    @Autowired
    private StoreNearByMapper storeNearByMapper;

    public List<StorePortrait> getStorePortrait(String id) {
        return storePortraitRepository.findAll(q->q.selectFrom(qStorePortrait).where(qStorePortrait.storeId.eq(id)));
    }

    public Map<Integer, EnvironmentTypeStoreStatistics> getEnvironmentTypeStoreStatistics() {
        return storeNearByMapper.getEnvironmentTypeStoreStatistics().stream().collect(Collectors.toMap(e -> e.getMainEnvironmentType(), e -> e));
    }
}
