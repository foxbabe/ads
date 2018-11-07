package com.sztouyun.advertisingsystem.viewmodel.message.customer;

public class DistributeCustomerMessageViewModel extends CustomerMessageViewModel {
    private String oldOwnerId;
    private String oldOwnerName;
    private String newOwnerId;
    private String newOwnerName;

    public String getOldOwnerId() {
        return oldOwnerId;
    }

    public void setOldOwnerId(String oldOwnerId) {
        this.oldOwnerId = oldOwnerId;
    }

    public String getOldOwnerName() {
        return oldOwnerName;
    }

    public void setOldOwnerName(String oldOwnerName) {
        this.oldOwnerName = oldOwnerName;
    }

    public String getNewOwnerId() {
        return newOwnerId;
    }

    public void setNewOwnerId(String newOwnerId) {
        this.newOwnerId = newOwnerId;
    }

    public String getNewOwnerName() {
        return newOwnerName;
    }

    public void setNewOwnerName(String newOwnerName) {
        this.newOwnerName = newOwnerName;
    }

    public boolean isNewOwner(){
        return equalsCurrentUser(getNewOwnerId());
    }

    public boolean isOldOwner(){
        return equalsCurrentUser(getOldOwnerId());
    }

    @Override
    public DistributeCustomerMessageViewModel setNames(String creatorName,String customerName,String oldOwnerName,String newOwnerName){
        super.setNames(creatorName,customerName,oldOwnerName,newOwnerName);
        this.oldOwnerName=oldOwnerName;
        this.newOwnerName=newOwnerName;
        return this;
    }

    @Override
    public void setOwnerIds(String oldOwnerId,String newOwnerId){
        setOldOwnerId(oldOwnerId);
        setNewOwnerId(newOwnerId);
    }
}
