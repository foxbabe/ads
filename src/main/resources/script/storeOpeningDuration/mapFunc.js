function() {
    var key = {
        date:this._id.createdDate,
        durationType:(function(durationMap,openingTimeDuration) {
            duration=openingTimeDuration/1000/60/60;
            for(var key in durationMap){
                if(duration>= durationMap[key][0] && duration<durationMap[key][1])
                    return key;
            }
        })(durationMap,this.value.openingTimeDuration)

    };
    var value = {
        durationType:key.durationType,
        storeId:this._id.storeId,
        storeCount:0,
        storeIds:new Array(this._id.storeId)
    };
    emit(key, value);
}