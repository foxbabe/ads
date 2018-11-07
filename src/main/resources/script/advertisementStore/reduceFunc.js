function(key,values) {
    reducedVal = {
        storeId:key,
        totalShareAmount: 0,
        settledAmount:0
    };
    for (var idx = 0; idx < values.length; idx++) {
        reducedVal.totalShareAmount += values[idx].totalShareAmount;
        reducedVal.settledAmount+=values[idx].settledAmount;
    }
    return reducedVal;
}