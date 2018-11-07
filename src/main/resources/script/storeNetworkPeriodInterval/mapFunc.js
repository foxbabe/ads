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