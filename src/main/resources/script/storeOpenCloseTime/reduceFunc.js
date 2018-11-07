function(key,values) {
    reducedVal = {
        storeId:key.storeId,
        createdDate:key.createdDate,
        openingTimeBegin:0,
        openingTimeEnd:0,
        openingTimeDuration:0
    };
    for (var idx = 0; idx < values.length; idx++) {
        reducedVal.openingTimeBegin=(function(openingTimeBegin,createdTime){
            return openingTimeBegin>0? (openingTimeBegin>createdTime?createdTime:openingTimeBegin):createdTime
        })(reducedVal.openingTimeBegin,values[idx].createdTime);
        reducedVal.openingTimeEnd=(function(openingTimeEnd,createdTime){
            return openingTimeEnd>0? (openingTimeEnd>createdTime?openingTimeEnd:createdTime):createdTime
        })(reducedVal.openingTimeEnd,values[idx].createdTime);

    }
    reducedVal.openingTimeDuration=reducedVal.openingTimeEnd-reducedVal.openingTimeBegin
    return reducedVal;
}