function(key,values) {
    reducedVal = {
        interval:key,
        storeIdsNetwork:new Array(),
        storeIds4G:new Array(),
        storeIdsWIFI:new Array()
    };
    for (var idx = 0; idx < values.length; idx++) {
        arrayAppend(reducedVal.storeIdsNetwork,values[idx].storeIdsNetwork);
        arrayAppend(reducedVal.storeIds4G,values[idx].storeIds4G);
        arrayAppend(reducedVal.storeIdsWIFI,values[idx].storeIdsWIFI)
    }
    function arrayAppend(targetArr,appendArr){
        for(var index in appendArr){
            var itemValue = appendArr[index];
            if(targetArr.indexOf(itemValue)<0){
                targetArr.push(itemValue);
            }
        }
    }
    return reducedVal;
}