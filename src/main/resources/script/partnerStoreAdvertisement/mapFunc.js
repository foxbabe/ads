function() {
    var key = this.partnerAdvertisementId;
    var value = {
        partnerAdvertisementId:key,
        partnerId:this.partnerId,
        thirdPartId:this.thirdPartId,
        requestTimes: 1,
        displayTimes: this.finishTime?1:0,
        validTimes: this.valid?1:0,
        materialType:this.materialType,
        updateTime:this.status==1?this.requestTime:(this.status==3?this.finishTime:this.takeOffTime),
        advertisementStatus: this.status==1?1:3
    };
    emit(key, value);
}