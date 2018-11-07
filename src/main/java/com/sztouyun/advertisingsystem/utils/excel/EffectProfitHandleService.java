package com.sztouyun.advertisingsystem.utils.excel;

import com.mongodb.DBObject;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.mapper.StoreInfoMapper;
import com.sztouyun.advertisingsystem.utils.MapUtil;
import com.sztouyun.advertisingsystem.utils.SpringUtil;
import com.sztouyun.advertisingsystem.utils.dataHandle.AbstractDataHandle;
import com.sztouyun.advertisingsystem.viewmodel.adProfitShare.StoreValidInfoRequest;
import com.sztouyun.advertisingsystem.viewmodel.customerStore.StoreInvalidTypeEnum;
import com.sztouyun.advertisingsystem.viewmodel.store.StoreValidInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.CollectionUtils;

import java.io.BufferedInputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by fengwen on 22/08/2018.
 */
@Data
@NoArgsConstructor
public class EffectProfitHandleService extends AbstractDataHandle<EffectProfitHandleInput,Workbook,EffectProfitHandleOutput> {
    public  Workbook preProcess(EffectProfitHandleInput in) {
        return ExcelConversion.createWookbook(in.getIn());
    }

    @Override
    public  void validate(EffectProfitHandleInput in, Workbook vi) {
        BufferedInputStream inputStream=in.getIn();
        ExcelConvertConfig config=in.getConvertConfig();
        Sheet sheet=getSheet(inputStream,vi,config);
        String[] headerNames=config.getHeaderNames();
        if(headerNames.length>0){
            ExcelConversion.validateHeader(inputStream,sheet.getRow(0),headerNames);
        }
    }

    @Override
    public EffectProfitHandleOutput process(EffectProfitHandleInput in, Workbook wb) {
        EffectProfitHandleOutput output=loadDataToMongo(in, wb);
        return output;
    }

    private EffectProfitHandleOutput loadDataToMongo(EffectProfitHandleInput in, Workbook wb) {
        Map<String,Integer> ids =new HashMap<>();
        ExcelConvertConfig config=in.getConvertConfig();
        MongoTemplate mongoTemplate=SpringUtil.getBean(MongoTemplate.class);
        List<DBObject> list=new ArrayList<>();
        Sheet sheet = wb.getSheetAt(0);
        Integer rowCount=0;
        Short firstCellIndex=null;
        Short lastCellIndex=null;
        String[] headerNames=config.getHeaderNames();
        for (int i = config.getStartRowIndex(); i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);
            if(row==null)
                continue;
            if(firstCellIndex==null){
                firstCellIndex=row.getFirstCellNum();
                lastCellIndex=row.getLastCellNum();
            }
            if(headerNames.length>0 && lastCellIndex>headerNames.length){
                lastCellIndex=(short)headerNames.length;
            }
            DBObject object= ExcelConversion.readRowToDbObject(config, firstCellIndex, lastCellIndex, row);
            if(object!=null){
                String storeNo=(String) object.get("storeNo");
                object.put("settled",false);
                MapUtil.putOrIncrease(ids, storeNo);
                if(ids.containsKey(storeNo) && Integer.valueOf(ids.get(storeNo))>1){
                    object.put("validType",StoreInvalidTypeEnum.DuplicateDelivery.getValue());
                }
                list.add(object);
                rowCount++;
            }
            if(i>config.getStartRowIndex() && list.size()==Constant.MONGODB_PAGESIZE){
                updateStoreInvalidType(list,config.getBid());
                mongoTemplate.insert(list, Constant.IMPORT_DATA_COLLECTION);
                list=new ArrayList<>();
            }
        }
        if(!CollectionUtils.isEmpty(list)){
            updateStoreInvalidType(list,config.getBid());
            mongoTemplate.insert(list, Constant.IMPORT_DATA_COLLECTION);
        }
        EffectProfitHandleOutput output=new EffectProfitHandleOutput(rowCount,ids);
        return output;
    }

    private void updateStoreInvalidType(List<DBObject> list,String advertisementId){
        Iterator<DBObject> iterator=list.iterator();
        List<String> storeNos=list.stream().map(a->a.get("storeNo").toString()).collect(Collectors.toList());
        List<StoreValidInfo> validInfos=SpringUtil.getBean(StoreInfoMapper.class).getStoreValidInfo(new StoreValidInfoRequest(advertisementId,storeNos));
        Map<String,StoreValidInfo> validInfoMap=validInfos.stream().collect(Collectors.toMap(a->a.getStoreNo(), a->a));
        while (iterator.hasNext()){
            DBObject dbObject=iterator.next();
            String storeNo=dbObject.get("storeNo").toString();
            if(!Integer.valueOf(dbObject.get("validType").toString()).equals(StoreInvalidTypeEnum.DuplicateDelivery.getValue())){
                if(validInfoMap.containsKey(storeNo)){
                    StoreValidInfo storeValidInfo =validInfoMap.get(storeNo);
                    dbObject.put("storeId", storeValidInfo.getStoreId());
                }else{
                    dbObject.put("validType",StoreInvalidTypeEnum.NotDelivery.getValue());
                }
            }
        }
    }

    @Override
    public void after(EffectProfitHandleInput in, Workbook po, EffectProfitHandleOutput r) {
        ExcelConversion.closeInputStream(in.getIn());
    }

    @Override
    public EffectProfitHandleOutput execute(EffectProfitHandleInput in){
        EffectProfitHandleOutput result=super.execute(in);
        return result;
    }

    private Sheet getSheet(BufferedInputStream inputStream,Workbook vi, ExcelConvertConfig config){
        Sheet sheet=vi.getSheetAt(0);
        if(sheet==null || config.getStartRowIndex()>sheet.getLastRowNum()){
            ExcelConversion.closeInputStream(inputStream);
            throw new BusinessException("处理Excel异常");
        }
        return sheet;
    }
}
