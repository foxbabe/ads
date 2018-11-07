function() {
    var key = {
            partnerId:this.partnerId,
            storeId:this.storeId,
            advertisementPositionCategory:this.advertisementPositionCategory,
            requestDate:this.requestDate
    };
    var value = {
        partnerId:this.partnerId,
        advertisementPositionCategory:this.advertisementPositionCategory,
        displayTimes: this.finishTime?1:0,
        validTimes: this.valid?1:0,
        storeId:this.storeId,
        requestDate:this.requestDate
    };
    emit(key, value);
}