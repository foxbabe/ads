package com.sztouyun.advertisingsystem.service.commodity;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.commodity.QSupplier;
import com.sztouyun.advertisingsystem.model.commodity.Supplier;
import com.sztouyun.advertisingsystem.repository.commodity.SupplierRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.viewmodel.commodity.SupplierOptionRequest;
import com.sztouyun.advertisingsystem.viewmodel.common.MyPageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SupplierService extends BaseService {

    @Autowired
    private SupplierRepository supplierRepository;

    private static final QSupplier qSupplier = QSupplier.supplier;

    public Page<Supplier> querySupplierOptionInfo(SupplierOptionRequest request) {
        Pageable pageable = new MyPageRequest(request.getPageIndex(), request.getPageSize(),new QSort(qSupplier.id.desc()));
        BooleanBuilder predicate=new BooleanBuilder();
        if (!StringUtils.isEmpty(request.getSupplierName())) {
            predicate.and(qSupplier.name.contains(request.getSupplierName()));
        }
        return supplierRepository.findAll(q -> q.selectFrom(qSupplier).where(predicate), pageable);
    }
    public void update(Supplier supplier){
        supplierRepository.save(supplier);
    }

    public void delete(Long id){
        supplierRepository.delete(id);
    }
}
