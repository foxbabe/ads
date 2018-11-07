function(key,values) {
    reducedVal = {
        partnerId:key,
        requestAdvertisementIds:new Array(),
        displayAdvertisementIds:new Array()
    };
    for (var idx = 0; idx < values.length; idx++) {
        for(var index in values[idx].requestAdvertisementIds){
            var itemValue = values[idx].requestAdvertisementIds[index];
            if(reducedVal.requestAdvertisementIds.indexOf(itemValue)<0){
                reducedVal.requestAdvertisementIds.push(itemValue);
            }
        }
        for(var index in values[idx].displayAdvertisementIds){
            var itemValue = values[idx].displayAdvertisementIds[index];
            if(reducedVal.displayAdvertisementIds.indexOf(itemValue)<0){
                reducedVal.displayAdvertisementIds.push(itemValue);
            }
        }
    }
    return reducedVal;
}