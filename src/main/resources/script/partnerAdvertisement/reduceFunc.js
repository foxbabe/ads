function(key,values) {
    reducedVal = { partnerAdvertisementId:key,requestTimes: 0, partnerId:'',thirdPartId:'',displayTimes: 0 ,validTimes:0,storeIds:[]};
    for (var idx = 0; idx < values.length; idx++) {
        reducedVal.requestTimes += values[idx].requestTimes;
        reducedVal.displayTimes += values[idx].displayTimes;
        reducedVal.validTimes+=values[idx].validTimes;
        if(!reducedVal.partnerId){
            reducedVal.partnerId=values[idx].partnerId;
        }
        if(!reducedVal.thirdPartId){
            reducedVal.thirdPartId=values[idx].thirdPartId;
        }
        for(var index in values[idx].storeIds){
            var itemValue = values[idx].storeIds[index];
            if(reducedVal.storeIds.indexOf(itemValue)<0){
                reducedVal.storeIds.push(itemValue);
            }
        }

    }
    return reducedVal;
}