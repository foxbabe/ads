package com.sztouyun.advertisingsystem.service.system;


import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.common.jpa.JoinDescriptor;
import com.sztouyun.advertisingsystem.exception.BusinessException;
import com.sztouyun.advertisingsystem.model.account.QUser;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.model.common.CodeTypeEnum;
import com.sztouyun.advertisingsystem.model.common.RoleTypeEnum;
import com.sztouyun.advertisingsystem.model.contract.QContract;
import com.sztouyun.advertisingsystem.model.customer.QCustomer;
import com.sztouyun.advertisingsystem.model.system.Organization;
import com.sztouyun.advertisingsystem.model.system.OrganizationArchive;
import com.sztouyun.advertisingsystem.model.system.QOrganization;
import com.sztouyun.advertisingsystem.repository.account.UserRepository;
import com.sztouyun.advertisingsystem.repository.contract.ContractRepository;
import com.sztouyun.advertisingsystem.repository.customer.CustomerRepository;
import com.sztouyun.advertisingsystem.repository.system.OrganizationArchiveRepository;
import com.sztouyun.advertisingsystem.repository.system.OrganizationRepository;
import com.sztouyun.advertisingsystem.service.BaseService;
import com.sztouyun.advertisingsystem.service.common.CodeGenerationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class OrganizationService extends BaseService {
    private static final String ROOT_ORGANIZATION_ID = "0";//表示顶级组织结构

    private final QOrganization qOrganization = QOrganization.organization;

    private final QContract qContract = QContract.contract;


    @Autowired
    private CodeGenerationService codeGenerationService;

    private final QUser qUser = QUser.user;

    private final QCustomer qCustomer = QCustomer.customer;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationArchiveRepository organizationArchiveRepository;

    /**
     * 查询所有数据, 返回list列表, 可以用作树结构返回和列表返回
     *
     * @return
     */
    public Iterable<Organization> getAllOrganization() {
        return organizationRepository.findAllAuthorized(new JoinDescriptor().leftJoin(qOrganization.parent));
    }

    /**
     * 查询组织机构详细信息
     *
     * @param id
     * @return
     */
    public Organization getOrganization(String id) {
        if (StringUtils.isEmpty(id))
            throw new BusinessException("组织id不能为空！");

        Organization organization = organizationRepository.findOneAuthorized(qOrganization.id.eq(id));
        if (organization != null)
            return organization;
        throw new BusinessException("组织不存在或权限不足！");
    }

    /**
     * 新增组织结构
     *
     * @param organization
     * @return
     */
    @Transactional
    public void createOrganization(Organization organization) {
        if (organization == null)
            throw new BusinessException("缺失组织结构信息");
        if (organizationRepository.findByName(organization.getName()) != null)
            throw new BusinessException("该组织结构已经存在", -2);

        if (ROOT_ORGANIZATION_ID.equals(organization.getParentId()) && !getUser().isAdmin())
            throw new BusinessException("您没有该操作权限");

        String parentCode = CodeTypeEnum.ORG.toString();
        if (!ROOT_ORGANIZATION_ID.equals(organization.getParentId())) {
            Organization parentOrganization = organizationRepository.findById(organization.getParentId());
            if (parentOrganization == null)
                throw new BusinessException("父亲组织不存在");
            parentCode = parentOrganization.getCode();
        }
        User user = userRepository.findById(organization.getOwnerId());
        if (null == user)
            throw new BusinessException("指定的负责人不存在");

        user.setOrganizationId(organization.getId());
        userRepository.save(user);

        organization.setCode(codeGenerationService.generateCode(parentCode + ".", 4));
        organizationRepository.save(organization);
    }

    /**
     * 修改组织结构
     *
     * @param organization
     * @return
     */
    @Transactional
    public void updateOrganization(Organization organization) {
        Organization dbOrganization = organizationRepository.findById(organization.getId());
        if (dbOrganization == null)
            throw new BusinessException("查询不到组织机构信息");

        boolean hasRepeat = organizationRepository.exists(qOrganization.name.eq(organization.getName()).and(qOrganization.id.ne(organization.getId())));
        if (hasRepeat)
            throw new BusinessException("该组织结构已经存在", -2);

        if (!ROOT_ORGANIZATION_ID.equals(organization.getParentId()) && !organizationRepository.exists(organization.getParentId()))
            throw new BusinessException("父亲节点不存在");

        User user = userRepository.findById(organization.getOwnerId());
        if (null == user)
            throw new BusinessException("指定的负责人不存在");

        //判断负责人是否被其他组织机构占用
        if (!StringUtils.isEmpty(user.getOrganizationId()) && !user.getOrganizationId().equals(organization.getId()))
            throw new BusinessException("该负责人已经被分配");

        if (!(user.getRoleTypeEnum().equals(RoleTypeEnum.ManagerialStaff) || user.isAdmin()))
            throw new BusinessException("您选择的人员没有相关管理权限");

        boolean exists = organizationRepository.exists(qOrganization.ownerId.eq(organization.getOwnerId()).and(qOrganization.id.eq(organization.getId())));
        if (!exists) {
            if(!StringUtils.isEmpty(dbOrganization.getOwnerId())){
                //获取老的负责人, 设置为游离状态
                User oldUser = userRepository.findById(dbOrganization.getOwnerId());
                oldUser.setOrganizationId(null);
                userRepository.save(oldUser);
            }
            //设置新的负责人组织结构id
            user.setOrganizationId(organization.getId());
            userRepository.save(user);
        }

        if (!dbOrganization.getParentId().equals(organization.getParentId())) {// 将组织机构A移动到B组织结构门下, A组织机构以及A组织机构下面的组织机构的所有code都需要变换
            String newCodeFromParent = codeGenerationService.generateCode(organizationRepository.findOne(q -> q.select(qOrganization.code).from(qOrganization).where(qOrganization.id.eq(organization.getParentId()))) + ".", 4);
            organization.setCode(newCodeFromParent);
            organizationRepository.updateChildrenCode(dbOrganization.getCode(),newCodeFromParent);
        }
        organizationRepository.save(organization);
    }

    /**
     * 删除组织结构, 将删除的数据复制到归档表
     *
     * @param id
     * @return
     */
    @Transactional
    public void deleteOrganization(String id) {

        if (StringUtils.isEmpty(id))
            throw new BusinessException("缺失组织结构信息");

        if (organizationRepository.exists(qOrganization.parentId.eq(id)))
            throw new BusinessException("该组织结构含有下级, 不能删除");

        Organization organization = organizationRepository.findById(id);
        if (organization == null)
            throw new BusinessException("没有找到组织结构信息");

        //-- 组织机构下的人员有没有负责的合同
        boolean existsContracts = contractRepository.exists(qContract.owner.organizationId.eq(id));

        if (existsContracts)
            throw new BusinessException("该组织机构有相关业务关联, 无法直接删除");

        //-- 组织机构下的人员有没有负责对应的客户数据
        boolean existsCustomers = customerRepository.exists(qCustomer.owner.organizationId.eq(id));
        if (existsCustomers)
            throw new BusinessException("该组织机构有相关业务关联, 无法直接删除");

        OrganizationArchive organizationArchive = new OrganizationArchive();
        BeanUtils.copyProperties(organization, organizationArchive);

        //将负责人, 组织机构关联的人员 都设置为游离状态
        userRepository.updateUserOrganization(null, organization.getId());

        organizationArchiveRepository.save(organizationArchive);
        organizationRepository.delete(organization.getId());
    }


    /**
     * 分页查询所有组织机构, 包括根据组织机构名称模糊搜索
     *
     * @param name
     * @param pageable
     * @param currentId
     * @return
     */
    public Page<Organization> getAllOrganizations(String name, String currentId, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (!StringUtils.isEmpty(name)) {
            booleanBuilder.and(qOrganization.name.contains(name));
        }
        if(!StringUtils.isEmpty(currentId)) {
            booleanBuilder.and(qOrganization.id.ne(currentId));
        }
        return organizationRepository.findAllAuthorized(booleanBuilder, pageable, new JoinDescriptor().leftJoin(qOrganization.parent));
    }
}
