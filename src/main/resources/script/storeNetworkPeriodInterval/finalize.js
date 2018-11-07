function(key, value) {
    value.networkStoreCount = value.storeIdsNetwork.length;
    value.storeCount4G = value.storeIds4G.length;
    value.storeCountWIFI = value.storeIdsWIFI.length;
    value.storeIdsNetwork =null;
    value.storeIds4G =null;
    value.storeIdsWIFI =null;
    return value;
}