function() {
    var key = {
        date:this._id.createdDate,
        durationType:(function(intervalMap,createdDate,openingTimeBegin) {
            var hourDuration=parseInt((openingTimeBegin-createdDate)/(1000*60*60));
            for(var key in intervalMap) {
                if (hourDuration >= intervalMap[key][0] && hourDuration < intervalMap[key][1])
                    return key;
            }
        })(intervalMap,this._id.createdDate,this.value.openingTimeBegin)

    };
    var value = {
        durationType:key.durationType,
        storeId:this._id.storeId,
        storeIds:new Array(this._id.storeId),
        storeCount:0
    };
    emit(key, value);
}