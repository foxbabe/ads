function(key, value) {
    value.requestAdvertisementCount = value.requestAdvertisementIds.length;
    value.displayAdvertisementCount = value.displayAdvertisementIds.length;
    value.requestAdvertisementIds =null;
    value.displayAdvertisementIds =null;
    return value;
}