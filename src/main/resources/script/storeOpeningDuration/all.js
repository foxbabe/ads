db.getCollection('storeDeviceDailyStatisticResult').mapReduce(
    function() {
        var key = {
            date:this._id.createdDate,
            durationType:(function(durationMap,openingTimeDuration) {
                duration=openingTimeDuration/1000/60/60;
                for(var key in durationMap){
                    if(duration>= durationMap[key][0] && duration<durationMap[key][1])
                        return key;
                }
                return '';
            })(durationMap,this.value.openingTimeDuration)
        };
        var value = {
            durationType:key.durationType,
            storeId:this._id.storeId,
            storeIds:new Array(this._id.storeId)
        };
        emit(key, value);
    }
    ,
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
    } ,
    {
        query:{"_id.createdDate":1534089600000},
        out:{inline:1},
        scope:
            {
                "durationMap":{
                    '0 - 4h':[0, 4],
                    '6 - 8h':[6, 8],
                    '8 - 10h':[8, 10],
                    '10 - 12h':[10, 12],
                    '12h以上':[12, 24]

                }

            },
        finalize:function(key, value) {
            value.storeCount = value.storeIds.length;
            value.storeIds =null;
            return value;
        }
    })