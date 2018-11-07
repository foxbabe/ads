package com.sztouyun.advertisingsystem.model.system;

import com.sztouyun.advertisingsystem.common.ITree;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = {@Index(name = "index_code",columnList = "code")})
public class Organization extends BaseOrganization implements ITree<String> {

}
