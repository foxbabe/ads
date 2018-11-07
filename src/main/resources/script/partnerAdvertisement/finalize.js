function(key, value) {
    value.storeCount = value.storeIds.length;
    value.storeIds =null;
    return value;
}