function() {
    var key = this.partnerId;
    var value = {
        partnerId:key,
        requestAdvertisementCount:1,
        displayAdvertisementCount:(this.finishTime&&this.valid)?1:0,
        requestAdvertisementIds:new Array(this.partnerAdvertisementId),
        displayAdvertisementIds:(this.finishTime&&this.valid)?new Array(this.partnerAdvertisementId):new Array()
    };
    emit(key, value);
}