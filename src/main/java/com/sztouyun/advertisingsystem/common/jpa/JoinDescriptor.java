package com.sztouyun.advertisingsystem.common.jpa;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.JPAQueryBase;
import org.springframework.security.access.method.P;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Riber on 2017/8/21 0021.
 */
public class JoinDescriptor {

    private List<JoinInfo> joinInfos = new ArrayList<>();

    public JoinDescriptor innerJoin(EntityPath path){
        joinInfos.add(new JoinInfo(path,JoinType.InnerJoin));
        return this;
    }

    public JoinDescriptor leftJoin(EntityPath path){
        joinInfos.add(new JoinInfo(path,JoinType.LeftJoin));
        return this;
    }

    public JoinDescriptor rightJoin(EntityPath path){
        joinInfos.add(new JoinInfo(path,JoinType.RightJoin));
        return this;
    }

    public JoinDescriptor fullJoin(EntityPath path){
        joinInfos.add(new JoinInfo(path,JoinType.FullJoin));
        return this;
    }

    public JoinDescriptor innerJoin(EntityPath path,Path alias){
        joinInfos.add(new JoinInfo(path,JoinType.InnerJoin,alias));
        return this;
    }

    public JoinDescriptor leftJoin(EntityPath path,Path alias){
        joinInfos.add(new JoinInfo(path,JoinType.LeftJoin,alias));
        return this;
    }

    public JoinDescriptor rightJoin(EntityPath path,Path alias){
        joinInfos.add(new JoinInfo(path,JoinType.RightJoin,alias));
        return this;
    }

    public JoinDescriptor fullJoin(EntityPath path,Path alias){
        joinInfos.add(new JoinInfo(path,JoinType.FullJoin,alias));
        return this;
    }

    public JPAQueryBase includeJoin(JPAQueryBase query){
        for (JoinInfo joinInfo: joinInfos){
            switch (joinInfo.joinType){
                case InnerJoin:
                    if(joinInfo.alias == null){
                        query.innerJoin(joinInfo.path);
                    }else {
                        query.innerJoin(joinInfo.path,joinInfo.alias);
                    }
                    query.fetchJoin();
                    break;
                case LeftJoin:
                    if(joinInfo.alias == null){
                        query.leftJoin(joinInfo.path);
                    }else {
                        query.leftJoin(joinInfo.path,joinInfo.alias);
                    }
                    query.fetchJoin();
                    break;
                case RightJoin:
                    if(joinInfo.alias == null){
                        query.rightJoin(joinInfo.path);
                    }else {
                        query.rightJoin(joinInfo.path,joinInfo.alias);
                    }
                    query.fetchJoin();
                    break;
                case FullJoin:
                    if(joinInfo.alias == null){
                        query.join(joinInfo.path);
                    }else {
                        query.join(joinInfo.path,joinInfo.alias);
                    }
                    query.fetchJoin();
                    break;
            }
        }
        return query;
    }

    private class  JoinInfo{
        private EntityPath path;
        private JoinType joinType;
        private Path alias;

        public JoinInfo(EntityPath path, JoinType joinType) {
            this.path = path;
            this.joinType = joinType;
            this.alias = null;
        }

        public JoinInfo(EntityPath path, JoinType joinType, Path alias) {
            this.path = path;
            this.joinType = joinType;
            this.alias = alias;
        }
    }
}


