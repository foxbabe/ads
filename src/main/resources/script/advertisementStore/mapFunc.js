function() {
    var key = this.storeId;
    var value = {
        storeId:key,
        totalShareAmount: this.shareAmount,
        settledAmount:this.settled?this.shareAmount:0
    };
    emit(key, value);
}