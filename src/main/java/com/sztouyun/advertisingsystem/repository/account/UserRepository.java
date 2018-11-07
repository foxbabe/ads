package com.sztouyun.advertisingsystem.repository.account;

import com.querydsl.core.BooleanBuilder;
import com.sztouyun.advertisingsystem.model.account.QUser;
import com.sztouyun.advertisingsystem.model.account.User;
import com.sztouyun.advertisingsystem.repository.BaseRepository;
import com.sztouyun.advertisingsystem.service.account.AuthenticationService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@CacheConfig(cacheNames = "users")
public interface UserRepository extends BaseRepository<User> {
    User findById(String id);

    User findByUsername(String username);

    @Caching(evict = {@CacheEvict(key = "#p0.getId()"), @CacheEvict(key = "#p0.getUsername()")})
    @Override
    User save(User user);

    boolean existsByIdNotAndUsername(String id, String username);

    boolean existsByIdNotAndNickname(String id, String nickname);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    @Override
    default BooleanBuilder getAuthorizationFilter() {
        return AuthenticationService.getUserAuthenticationFilter(QUser.user.id).or(AuthenticationService.getCustomerUserAuthenticationFilter(QUser.user.id));
    }

    @Override
    default BooleanBuilder getStatusFilter() {
        return new BooleanBuilder().and(QUser.user.enableFlag.eq(true));
    }

    @CacheEvict(allEntries = true)
    @Modifying
    @Query(value = "UPDATE User set organizationId = :organizationId WHERE organizationId = :oldOrganizationId")
    void updateUserOrganization(@Param("organizationId") String organizationId, @Param("oldOrganizationId") String oldOrganizationId);

    @CacheEvict(allEntries = true)
    @Modifying
    @Query(value = "UPDATE User set organizationId =null WHERE organizationId = :organizationId and id in  (:list) ")
    void updateByOrganizationIdAndIdIn(@Param("organizationId") String organizationId, @Param("list") List<String> list);

    @Query("SELECT user FROM User user WHERE user.organizationId = :organizationId and user.id in (:list)")
    List<User> findByOrganizationIdAndIdIn(@Param("organizationId") String organizationId, @Param("list") List<String> list);

    User findFirstByIsAdmin(Boolean isAdmin);

    void deleteByCustomerId(String customerId);
}
