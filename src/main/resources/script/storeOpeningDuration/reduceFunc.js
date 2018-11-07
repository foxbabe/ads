function(key,values) {
    reducedVal = {
        durationType:key.durationType,
        storeIds:[]
    };
    for (var idx = 0; idx < values.length; idx++) {
        for(var index in values[idx].storeIds){
            var itemValue = values[idx].storeIds[index];
            if(reducedVal.storeIds.indexOf(itemValue)<0){
                reducedVal.storeIds.push(itemValue);
            }
        }
    }
    return reducedVal;
}