db.getCollection('storeDeviceDailyStatisticResult').mapReduce(
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
    },

    {	 query:{"_id.createdDate":1534089600000},
        scope:{
            "intervalMap":{
                "0":[0,3],
                "3":[3,6],
                "6":[6,9],
                "9":[9,12],
                "12":[12,15],
                "15":[15,18],
                "18":[18,21],
                "21":[21,24]
            }

        },
        out:{ inline:1},
        finalize:function(key, value) {
            value.storeCount = value.storeIds.length;
            value.storeIds =null;
            return value;
        }
    })