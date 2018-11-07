db.getCollection('storeDeviceHeartbeat').mapReduce(
    function() {
        var key =(function(intervalMap,createdDate,createdTime) {
            duration=parseInt((createdTime-createdDate)/1000/60/60);
            for(var key in intervalMap){
                if(duration>= intervalMap[key][0] && duration<intervalMap[key][1])
                    return key;
            }
            return "0";
        })(intervalMap,this.createdDate,this.createdTime)
        var value = {
            interval:key,
            storeId:this.storeId,
            connectionType:this.connectionType,
            storeIdsNetwork:new Array(this.storeId),
            storeIds4G:this.connectionType==4?new Array(this.storeId):new Array(),
            storeIdsWIFI:this.connectionType==1?new Array(this.storeId):new Array()
        };
        emit(key, value);
    }
    ,
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
    },

    {	 query:{"createdDate":1534089600000},
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
            value.networkStoreCount = value.storeIdsNetwork.length;
            value.storeCount4G = value.storeIds4G.length;
            value.storeCountWIFI = value.storeIdsWIFI.length;
            value.storeIdsNetwork =null;
            value.storeIds4G =null;
            value.storeIdsWIFI =null;
            return value;
        }
    })