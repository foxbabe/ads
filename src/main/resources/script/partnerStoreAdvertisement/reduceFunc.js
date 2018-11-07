function(key,values) {
    reducedVal = {
        partnerAdvertisementId:key,
        partnerId:'',
        thirdPartId:'',
        requestTimes: 0,
        displayTimes: 0 ,
        validTimes:0,
        advertisementStatus:3,
        materialType:0,
        updateTime:0
    };
    for (var idx = 0; idx < values.length; idx++) {
        reducedVal.requestTimes += values[idx].requestTimes;
        reducedVal.displayTimes += values[idx].displayTimes;
        reducedVal.validTimes+=values[idx].validTimes;
        if(reducedVal.advertisementStatus!=1 && values[idx].advertisementStatus==1){
            reducedVal.advertisementStatus=1;
        }
        if(reducedVal.updateTime<values[idx].updateTime){
            reducedVal.updateTime=values[idx].updateTime;
        }
        if(reducedVal.materialType==0){
            reducedVal.materialType=values[idx].materialType;
        }else if(reducedVal.materialType != 4 & reducedVal.materialType != values[idx].materialType){
            reducedVal.materialType = 4;
        }
        if(!reducedVal.partnerId){
            reducedVal.partnerId=values[idx].partnerId;
        }
        if(!reducedVal.thirdPartId){
            reducedVal.thirdPartId=values[idx].thirdPartId;
        }
    }
    return reducedVal;
}