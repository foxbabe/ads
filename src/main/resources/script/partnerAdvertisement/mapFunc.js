function() {
    var key = this.partnerAdvertisementId;
    var value = {
        partnerAdvertisementId:key,
        requestTimes: 1,
        partnerId:this.partnerId,
        thirdPartId:this.thirdPartId,
        displayTimes: this.finishTime?1:0,
        validTimes: this.valid?1:0,
        storeId:this.storeId,
        storeIds:new Array(this.storeId)

    };
    emit(key, value);
}