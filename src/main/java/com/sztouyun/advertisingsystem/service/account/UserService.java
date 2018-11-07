package com.sztouyun.advertisingsystem.service.account;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.common.InvokeResult;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.account.*;
import com.sztouyun.advertisingsystem.model.common.QVerificationCodeSendLog;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.ContractStatusEnum;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.customerStore.QCustomerStorePlan;
import com.sztouyun.advertisingsystem.model.partner.QCooperationPartner;
import com.sztouyun.advertisingsystem.model.system.Organization;
import com.sztouyun.advertisingsystem.model.system.QOrganization;
import com.sztouyun.advertisingsystem.repository.account.RoleRepository;
import com.sztouyun.advertisingsystem.repository.account.UserArchiveRepository;
import com.sztouyun.advertisingsystem.repository.account.UserRepository;
import com.sztouyun.advertisingsystem.repository.advertisement.AdvertisementRepository;
import com.sztouyun.advertisingsystem.repository.common.VerificationCodeSendLogRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerVisitRepository;
import com.sztouyun.advertisingsystem.repository.customerStore.CustomerStorePlanRepository;
import com.sztouyun.advertisingsystem.repository.partner.CooperationPartnerRepository;
import com.sztouyun.advertisingsystem.repository.system.OrganizationRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.common.VerificationCodeSendService;
import com.sztouyun.advertisingsystem.service.common.VerificationCodeTypeEnum;
import com.sztouyun.advertisingsystem.utils.ApiBeanUtils;
import com.sztouyun.advertisingsystem.utils.SpringUtil;
import com.sztouyun.advertisingsystem.viewmodel.account.RoleViewModel;
import com.sztouyun.advertisingsystem.viewmodel.account.UserInfoCountViewModel;
import com.sztouyun.advertisingsystem.viewmodel.account.UserViewModel;
import lombok.experimental.var;
import org.apache.calcite.linq4j.Linq4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
@CacheConfig(cacheNames = "users")
public class UserService extends BaseService {
    public static String DEFAULT_PASSWORD = "992fed45712235bee217768818704d3f";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserArchiveRepository userArchiveRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private VerificationCodeSendLogRepository verificationCodeSendLogRepository;
    @Autowired
    private VerificationCodeSendService verificationCodeSendService;
    @Autowired
    private CooperationPartnerRepository cooperationPartnerRepository;
    @Autowired
    private CustomerStorePlanRepository customerStorePlanRepository;
    @Autowired
    private CustomerVisitRepository customerVisitRepository;
    @Autowired
    private AdvertisementRepository advertisementRepository;

    private final QUser qUser = QUser.user;

    private final QOrganization qOrganization = QOrganization.organization;

    private final QUserArchive qUserArchive = QUserArchive.userArchive;

    private final QCustomer qCustomer = QCustomer.customer;

    private final QContract qContract = QContract.contract;

    private final QVerificationCodeSendLog qVerificationCodeSendLog=QVerificationCodeSendLog.verificationCodeSendLog;

    private final QCooperationPartner qCooperationPartner = QCooperationPartner.cooperationPartner;
    private final QCustomerStorePlan qCustomerStorePlan = QCustomerStorePlan.customerStorePlan;

    @Transactional
    public void modifyPassword(String password, String newPassword) {
        User user = userRepository.findById(getUser().getId()) ;
        if (!user.getPassword().equals(password))
            throw new BusinessException("密码错误");
        user.setPassword(newPassword);
        userRepository.save(user);

    }
    @Transactional
    public void sendResetPasswordVerificationCode(String mobile){
        User user = userRepository.findByUsername(mobile);
        if (null == user)
            throw new BusinessException("该手机号尚未注册");
        verificationCodeSendService.sendVerificationCode(mobile,VerificationCodeTypeEnum.ResetPassword, Constant.CODELENGTH,Constant.INTERVALSECOND);
    }

    @Transactional
    public void resetPassword(String mobile,String code,String newPassword) {
        User user = userRepository.findByUsername(mobile);
        if (null == user)
            throw new BusinessException("该手机号尚未注册");
        Date sendTime = verificationCodeSendLogRepository.findOne(f->f.select(qVerificationCodeSendLog.createdTime).from(qVerificationCodeSendLog)
                .where(qVerificationCodeSendLog.mobile.eq(mobile).and(qVerificationCodeSendLog.code.eq(code)).and(qVerificationCodeSendLog.type.eq(VerificationCodeTypeEnum.ResetPassword.getValue()))));
        if(sendTime == null)
            throw new BusinessException("手机号与验证码不匹配，请重新输入");
        if(new Date().getTime() - sendTime.getTime() > Constant.CODEVALIDITY * 1000)
            throw new BusinessException("验证码已过期，请重新获取验证码");

        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Transactional
    public void createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new BusinessException("该电话号码已注册");
        if (userRepository.existsByNickname(user.getNickname()))
            throw new BusinessException("该用户名已注册");
        String organizationId = user.getOrganizationId();
        if (!StringUtils.isEmpty(organizationId) && !organizationRepository.exists(organizationId))
            throw new BusinessException("该组织编号不存在");
        user.setPassword(DEFAULT_PASSWORD);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId);
        if (null == user)
            throw new BusinessException("用户不存在");
        if (getUser().getId().equals(user.getId()))
            throw new BusinessException("当前人员是当前操作用户，不能直接删除");
        if (organizationRepository.existsByOwnerId(userId)) {
            throw new BusinessException("当前人员是组织机构负责人，不能直接删除");
        }
        if (customerRepository.existsByOwnerId(userId) || customerRepository.existsByCreatorId(userId)) {
            throw new BusinessException("当前人员存在相关业务关联，不能直接删除");
        }
        if (customerVisitRepository.existsByCreatorId(userId)) {
            throw new BusinessException("当前人员存在相关业务关联，不能直接删除");
        }
        if (contractRepository.existsByOwnerId(userId) || contractRepository.existsByCreatorId(userId)) {
            throw new BusinessException("当前人员存在相关业务关联，不能直接删除");
        }
        if (advertisementRepository.existsByCreatorId(userId)) {
            throw new BusinessException("当前人员存在相关业务关联，不能直接删除");
        }
        if (user.getRoleTypeEnum().equals(RoleTypeEnum.AdvertisementCustomer)&&customerStorePlanRepository.exists(qCustomerStorePlan.creatorId.eq(userId))) {
            throw new BusinessException("当前人员存在相关业务关联，不能直接删除");
        }
        if (cooperationPartnerRepository.existsByOwnerId(userId) || cooperationPartnerRepository.existsByCreatorId(userId)) {
            throw new BusinessException("当前人员存在相关业务关联，不能直接删除");
        }
        deleteUser(user);
    }

    @Caching(evict = {@CacheEvict(key = "#p0.getId()"), @CacheEvict(key = "#p0.getUsername()")})
    public void deleteUser(User user) {
        UserArchive userArchive = ApiBeanUtils.copyProperties(user, UserArchive.class);
        userArchiveRepository.save(userArchive);
        userRepository.delete(user.getId());
    }

    @Transactional
    public void updateUser(User user, String roleId) {
        String id = user.getId();
        User oldUser = userRepository.findById(id);
        if (null == oldUser)
            throw new BusinessException("该用户不存在");
        String newUserName = user.getUsername();
        if (userRepository.existsByIdNotAndUsername(id, newUserName))
            throw new BusinessException("该电话号码已存在");
        String newNickName = user.getNickname();
        if (userRepository.existsByIdNotAndNickname(id, newNickName))
            throw new BusinessException("该姓名已存在");
        if (!StringUtils.isEmpty(user.getOrganizationId()) && !organizationRepository.exists(user.getOrganizationId()))
            throw new BusinessException("该组织编号不存在");
        Role newRole = roleRepository.findOne(roleId);
        if (newRole == null)
            throw new BusinessException("角色ID不存在");
        if (oldUser.getRoleTypeEnum().equals(RoleTypeEnum.ManagerialStaff)
              && (!roleId.equals(oldUser.getRoleId()) || user.getOrganizationId()==null || !user.getOrganizationId().equals(oldUser.getOrganizationId()))
              && organizationRepository.existsByOwnerId(id))
            throw new BusinessException("该区域管理人员负责一个组织机构，请重新指定该组织机构负责人，才能修改角色");
        oldUser.setRoleId(roleId);
        oldUser.setUpdatedTime(new Date());

        oldUser.setOrganizationId(user.getOrganizationId());

        oldUser.setUsername(newUserName);
        oldUser.setNickname(newNickName);
        userRepository.save(oldUser);
    }

    public UserViewModel getUserAndRoleFromCache(String username) {
        if (StringUtils.isEmpty(username))
            return null;
        UserViewModel user = SpringUtil.getBean(this.getClass()).getUserFromCache(username);
        if (user == null)
            return null;
        if (!StringUtils.isEmpty(user.getRoleId())) {
            RoleViewModel role = roleService.findRoleByIdFromCache(user.getRoleId());
            if (role != null) {
                user.setRole(role);
            }
        }
        return user;
    }

    @Cacheable(key = "'FirstAdminUser'")
    public UserViewModel findFirstAdminUser() {
        var user = userRepository.findFirstByIsAdmin(true);
        return getUserAndRoleFromCache(user.getUsername());
    }

    @Cacheable(key = "#p0",condition="#p0!=null")
    public UserViewModel getUserFromCache(String username){
        var user = userRepository.findByUsername(username);
        return ApiBeanUtils.copyProperties(user, UserViewModel.class);
    }

    public Page<User> queryUserList(String username, Pageable pageable) {
        Page<User> pages = userRepository.findAllAuthorized(qUser.nickname.contains(username), pageable,
                new JoinDescriptor().leftJoin(qUser.role).leftJoin(qUser.organization)
                , false);
        return pages;
    }

    public Page<User> findAllByOrganizationIdAndUsername(String organizationId, String username, Pageable pageable) {
        Organization organization = organizationRepository.findById(organizationId);
        if (null == organization)
            throw new BusinessException("组织编号不存在");
        BooleanBuilder predicate = GetPredicateByOrganizationIdOrUsername(organizationId, username);
        Page<User> pages = userRepository.findAllAuthorized(predicate, pageable, new JoinDescriptor().leftJoin(qUser.role).leftJoin(qUser.organization), false);
        return pages;
    }

    public User getUser(String id) {
        return getUser(id, null);
    }

    public User getUserAndRole(String id) {
        return getUser(id, new JoinDescriptor().innerJoin(qUser.role));
    }

    private User getUser(String id, JoinDescriptor joinDescriptor) {
        if (StringUtils.isEmpty(id))
            throw new BusinessException("用户ID不能为空！");
        User user = userRepository.findOne(qUser.id.eq(id), joinDescriptor);
        if (null == user)
            throw new BusinessException("该用户不存在");
        return user;
    }

    /**
     * 根据组织结构或者用户名查询用户并统计用户维护客户数量
     */
    public Page<User> getUserInfoByOrganizationIdOrUsername(String organizationId, String username, Pageable pageable) {
        BooleanBuilder predicate = GetPredicateByOrganizationIdOrUsername(organizationId, username);
        predicate.and(qUser.isAdmin.eq(false));
        Page<User> pages = userRepository.findAllAuthorized(predicate, pageable, true);
        if (pages.getContent().size() <= 0)
            return pages;

        List<User> userList = pages.getContent();
        List<String> userIds = Linq4j.asEnumerable(userList).select(user -> user.getId()).toList();
        //获取维护客户数量
        List<UserInfoCountViewModel> customerAmountList =
                customerRepository.findAll(q -> q
                        .select(Projections.bean(UserInfoCountViewModel.class, qCustomer.ownerId.as("ownerId"), qCustomer.count().as("customerCount")))
                        .from(qCustomer)
                        .where(qCustomer.ownerId.in(userIds))
                        .groupBy(qCustomer.ownerId));

        //获取维护合作方数量
        List<UserInfoCountViewModel> cooperationPartnerList =
                cooperationPartnerRepository.findAll(q -> q
                        .select(Projections.bean(UserInfoCountViewModel.class, qCooperationPartner.ownerId.as("ownerId"), qCooperationPartner.count().as("cooperationPartnerCount")))
                        .from(qCooperationPartner)
                        .where(qCooperationPartner.ownerId.in(userIds))
                        .groupBy(qCooperationPartner.ownerId));

        //获取合同签约的数量
        List<Integer> contractStatus = Arrays.asList(
                ContractStatusEnum.PendingExecution.getValue(),
                ContractStatusEnum.AbruptlyTerminated.getValue(),
                ContractStatusEnum.Finished.getValue(),
                ContractStatusEnum.Executing.getValue()
        );
        List<UserInfoCountViewModel> signedContractCountList = contractRepository.findAll(q -> q
                .select(Projections.bean(UserInfoCountViewModel.class, qContract.signerId.as("signerId"), qContract.count().as("signedContractCount")))
                .from(qContract)
                .where(qContract.signerId.in(userIds).and(qContract.contractStatus.in(contractStatus)))
                .groupBy(qContract.signerId));

        userList.forEach(user -> {
            UserInfoCountViewModel userInfoCount = Linq4j.asEnumerable(customerAmountList).firstOrDefault(userInfoCountViewModel -> (userInfoCountViewModel.getOwnerId()).equals(user.getId()));
            if (userInfoCount != null) {
                user.setCustomerCount(userInfoCount.getCustomerCount());
            }
            UserInfoCountViewModel signedContractCount = Linq4j.asEnumerable(signedContractCountList).firstOrDefault(userInfoCountViewModel -> (userInfoCountViewModel.getSignerId()).equals(user.getId()));
            if (signedContractCount != null) {
                user.setSignedContractCount(signedContractCount.getSignedContractCount());
            }
            UserInfoCountViewModel cooperationPartnerCount = Linq4j.asEnumerable(cooperationPartnerList).firstOrDefault(userInfoCountViewModel -> (userInfoCountViewModel.getOwnerId()).equals(user.getId()));
            if (cooperationPartnerCount != null) {
                user.setCooperationPartnerCount(cooperationPartnerCount.getCooperationPartnerCount());
            }
        });
        return pages;
    }

    private BooleanBuilder GetPredicateByOrganizationIdOrUsername(String organizationId, String username) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (!StringUtils.isEmpty(organizationId)) {
            predicate.and(qUser.organizationId.eq(organizationId));
        }
        if (!StringUtils.isEmpty(username)) {
            predicate.and(qUser.nickname.contains(username));
        }
        return predicate;
    }

    public Page<User> getOrganizationOwners(String username, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder(qUser.organizationId.isNull());
        if (!StringUtils.isEmpty(username)) {
            booleanBuilder.and(qUser.nickname.contains(username));
        }
        BooleanBuilder roleFilter = new BooleanBuilder(qUser.role.roleType.eq(RoleTypeEnum.ManagerialStaff.getValue()));
        if (getUser().isAdmin()) {
            roleFilter.or(qUser.isAdmin.eq(true));
        }
        booleanBuilder.and(roleFilter);
        //查询负责人特殊处理，不过滤组织权限
        Page<User> pages = userRepository.findAll(booleanBuilder.and(userRepository.getStatusFilter()), pageable,
                new JoinDescriptor().leftJoin(qUser.role).leftJoin(qUser.organization));
        return pages;
    }

    @Transactional
    public void toggledEnable(String id) {
        User user = userRepository.findById(id);
        if (user.getId().equals(getUser().getId()) && user.isEnabled()) {
            throw new BusinessException("无法禁用当前操作用户");
        }
        if (null == user)
            throw new BusinessException("用户不存在");
        boolean currentStatus = user.isEnable();
        user.setEnableFlag(!currentStatus);
        userRepository.save(user);
    }

    @Transactional
    public InvokeResult removeUserFromOrganization(String organizationId, String userId) {
        boolean organizationExists = organizationRepository.exists(organizationId);
        if (!organizationExists)
            throw new BusinessException("组织ID不存在");
        User user = userRepository.findOne(userId);
        if (user == null)
            throw new BusinessException("用户ID不存在");
        user.setOrganizationId(null);
        userRepository.save(user);
        return InvokeResult.SuccessResult();
    }

    @Transactional
    public InvokeResult batchRemoveUserFromOrganization(String organizationId, List<String> list) {
        boolean organizationExists = organizationRepository.exists(organizationId);
        if (!organizationExists)
            throw new BusinessException("组织ID不存在");
        if (list.isEmpty())
            throw new BusinessException("用户ID列表不能为空");
        List<User> existsList = userRepository.findByOrganizationIdAndIdIn(organizationId, list);
        if (existsList.isEmpty() || existsList.size() != list.size())
            throw new BusinessException("组织ID和用户ID列表数据不准确");
        userRepository.updateByOrganizationIdAndIdIn(organizationId, list);
        return InvokeResult.SuccessResult();
    }

    @Cacheable(key = "#p0",condition="#p0!=null")
    public String getNicknameFromCache(String userId) {
        if (StringUtils.isEmpty(userId))
            return "";

        String nickname = userRepository.findOne(q -> q.select(qUser.nickname).from(qUser).where(qUser.id.eq(userId)));
        if (nickname == null) {
            nickname = userArchiveRepository.findOne(q -> q.select(qUserArchive.nickname).from(qUserArchive).where(qUserArchive.id.eq(userId)));
        }
        if (nickname == null)
            return "";

        return nickname;
    }

    public List<String> getAccessedUserIds() {
        return userRepository.findAllAuthorized(q -> q.select(qUser.id).from(qUser), false);
    }

    public List<String> getUserAllLeaderIds(List<String> userIds){
        List<String> organizationCodes = userRepository.findAll(q->q.select(qUser.organization.code).from(qUser).where(qUser.id.in(userIds)));
        List<String> leaderOrganizationCodes = Linq4j.asEnumerable(organizationCodes).selectMany(organizationCode->{
            List<String> codes = new ArrayList<>();
            if(StringUtils.isEmpty(organizationCode))
                return Linq4j.asEnumerable(codes);

            while (organizationCode.contains(".")){
                codes.add(organizationCode);
                organizationCode = organizationCode.substring(0,organizationCode.lastIndexOf("."));
            }
            return Linq4j.asEnumerable(codes);
        }).distinct().toList();
        return organizationRepository.findAll(q->q.select(qOrganization.ownerId).from(qOrganization).where(qOrganization.code.in(leaderOrganizationCodes)));
    }

    public List<String> getAllAdminUserIds(){
        return userRepository.findAll(q->q.select(qUser.id).from(qUser).where(qUser.isAdmin.eq(true)));
    }

    public List<User> getCustomerUser(List<String> customerIds) {
        return userRepository.findAll(q ->q
                .selectFrom(qUser)
                .where(qUser.customerId.in(customerIds)));
    }
}
