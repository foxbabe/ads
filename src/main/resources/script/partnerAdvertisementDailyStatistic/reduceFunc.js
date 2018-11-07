function(key,values) {
    reducedVal = {
        storeId:key.storeId,
        partnerId:key.partnerId,
        advertisementPositionCategory:key.advertisementPositionCategory,
        displayTimes: 0 ,
        validTimes:0,
        requestDate:key.requestDate
    };
    for (var idx = 0; idx < values.length; idx++) {
        reducedVal.displayTimes += values[idx].displayTimes;
        reducedVal.validTimes+=values[idx].validTimes;
    }
    return reducedVal;
}