function() {
    var key = {
        storeId:this.storeId,
        createdDate:this.createdDate
    };
    var value = {
        storeId:this.storeId,
        createdDate:this.createdDate,
        createdTime:this.createdTime,
        openingTimeBegin:this.createdTime,
        openingTimeEnd:this.createdTime,
        openingTimeDuration:0
    };
    emit(key, value);
}