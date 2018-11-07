package com.sztouyun.advertisingsystem.service.account;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.account.QUser;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.system.Organization;
import com.sztouyun.advertisingsystem.model.system.QOrganization;
import com.sztouyun.advertisingsystem.repository.system.OrganizationRepository;
import com.sztouyun.advertisingsystem.utils.SpringUtil;
import com.sztouyun.advertisingsystem.viewmodel.account.UserViewModel;
import lombok.experimental.var;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthenticationService {
    public static UserViewModel getUser() {
        try {
            return (UserViewModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception ex){
            throw new BusinessException("请先登录！",401);
        }
    }

    public static void  setAdminLogin(){
        UserViewModel adminUser  = getAdminUser();
        SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken(adminUser, adminUser.getPassword(),adminUser.getAuthorities()));
    }

    public static UserViewModel getAdminUser(){
        return SpringUtil.getBean(UserService.class).findFirstAdminUser();
    }

    /**
     * 获取用户相关权限过滤where条件SQL
     *
     * @return
     */
    public static String getUserAuthenticationFilterSql(String userFiled) {
        var user = getUser();
        if (user.isAdmin())
            return " 1=1 ";

        switch (user.getRoleTypeEnum()) {
            case ManagerialStaff:
                String userOrganizationCode = getUserOrganizationCode();
                //没有组织就查询自己的和自己创建并且没有组织的人
                if (StringUtils.isEmpty(userOrganizationCode))
                    return String.format("(%s = '%s' or %s in (select id FROM `user` where organization_id is NULL AND `user`.creator_id = '%s' ))",userFiled,user.getId(),userFiled, user.getId());
                //查询自己组织下面的人和自己创建并且没有组织的人
                return String.format("( %s in (select u.id FROM `user` u LEFT JOIN organization o on u.organization_id = o.id where o.`code` like '%s' or (u.creator_id = '%s' AND u.organization_id IS NULL ) ))",userFiled,userOrganizationCode+"%", user.getId());
            default:
                return String.format(" %s = '%s' ",userFiled,getUser().getId());
        }
    }

    /**
     * 获取用户相关权限过滤条件
     *
     * @return
     */
    public static BooleanBuilder getUserAuthenticationFilter(StringPath userFiled) {
        var user = getUser();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (user.isAdmin())
            return booleanBuilder;

        QUser qUser = QUser.user;
        switch (user.getRoleTypeEnum()) {
            case ManagerialStaff:
                String userOrganizationCode = getUserOrganizationCode();
                BooleanBuilder myCreatedFilter = new BooleanBuilder(qUser.organizationId.isNull().and(qUser.creatorId.eq(user.getId())));
                //没有组织就查询自己的和自己创建并且没有组织的人
                if (StringUtils.isEmpty(userOrganizationCode))
                    return booleanBuilder.and(userFiled.eq(user.getId())
                            .or(userFiled.in(JPAExpressions.select(qUser.id).from(qUser).where(myCreatedFilter))));

                //查询下属(自己组织下面的人和自己创建并且没有组织的人)
                JPQLQuery<String> subordinateQuery = JPAExpressions.select(qUser.id).from(qUser).leftJoin(qUser.organization)
                        .where(getOrganizationCodeAuthentication(qUser.organization.code, userOrganizationCode)
                                .or(myCreatedFilter));

                booleanBuilder.and(userFiled.in(subordinateQuery));
                break;
            default:
                booleanBuilder.and(userFiled.eq(user.getId()));
                break;
        }
        return booleanBuilder;
    }

    /**
     * 获取广告客户用户相关权限过滤条件
     *
     * @return
     */
    public static BooleanBuilder getCustomerUserAuthenticationFilter(StringPath userFiled){
        var user = getUser();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (user.isAdmin())
            return booleanBuilder;

        QUser qUser = QUser.user;
        QCustomer qCustomer = QCustomer.customer;
        switch (user.getRoleTypeEnum()) {
            case ManagerialStaff:
                //下属的广告客户用户
                JPQLQuery<String> subordinateCustomerUserQuery = JPAExpressions.select(qUser.id).from(qUser).innerJoin(qUser.customer,qCustomer)
                        .where(getUserAuthenticationFilter(qCustomer.ownerId));

                booleanBuilder.and(userFiled.in(subordinateCustomerUserQuery));
                break;
        }
        return booleanBuilder;
    }

    /**
     * 获取组织相关权限过滤
     *
     * @return
     */
    public static BooleanBuilder getOrganizationAuthenticationFilter() {
        StringPath organizationCodeFiled = QOrganization.organization.code;
        var user = getUser();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (user.isAdmin())
            return booleanBuilder;
        String userOrganizationCode = getUserOrganizationCode();
        if (StringUtils.isEmpty(userOrganizationCode))
            return booleanBuilder.and(organizationCodeFiled.isNull());

        return getOrganizationCodeAuthentication(organizationCodeFiled, userOrganizationCode);
    }

    private static String getUserOrganizationCode() {
        var user = getUser();
        if (StringUtils.isEmpty(user.getOrganizationId()))
            return null;

        Organization organization = SpringUtil.getBean(OrganizationRepository.class).findById(user.getOrganizationId());
        if (organization == null)
            return null;
        return organization.getCode();
    }

    private static BooleanBuilder getOrganizationCodeAuthentication(StringPath organizationCodeFiled, String organizationCode) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        var user = getUser();
        switch (user.getRoleTypeEnum()) {
            case ManagerialStaff:
                booleanBuilder.and(organizationCodeFiled.startsWith(organizationCode));
                break;
            case SaleMan:
                booleanBuilder.and(organizationCodeFiled.eq(organizationCode));
                break;
        }
        return booleanBuilder;
    }
}
