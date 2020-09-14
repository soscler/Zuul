package com.confluex.zuul.service.security

import com.confluex.zuul.data.config.ZuulDataConstants
import org.apache.commons.lang.NotImplementedException
import com.confluex.zuul.data.dao.EnvironmentDao
import com.confluex.zuul.data.model.Environment
import org.junit.Before
import org.junit.Test
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

class EnvironmentPermissionsEvaluatorTest {

    EnvironmentPermissionsEvaluator evaluator
    Environment restrictedEnv
    Environment nonRestrictedEnv

    @Before
    void createMocks() {
        def roleHierarchy = new RoleHierarchyImpl(hierarchy: "${ZuulDataConstants.ROLE_SYSTEM_ADMIN} > ${ZuulDataConstants.ROLE_ADMIN} > ${ZuulDataConstants.ROLE_USER} > ${ZuulDataConstants.ROLE_GUEST}")
        evaluator = new EnvironmentPermissionsEvaluator(roleHierarchy: roleHierarchy, environmentDao: mock(EnvironmentDao))
        restrictedEnv = new Environment(restricted: true)
        nonRestrictedEnv = new Environment(restricted: false)
    }

    @Test(expected = NotImplementedException)
    void shouldErrorOnUnrecognizedPermission() {
        def authentication = createAuthentication([ZuulDataConstants.ROLE_SYSTEM_ADMIN])
        evaluator.hasPermission(authentication, nonRestrictedEnv, 'notapermission')
    }

    @Test(expected = NotImplementedException)
    void shouldErrorWhenCheckingDomainIdentifiersForInvalidClasses() {
        def authentication = createAuthentication([ZuulDataConstants.ROLE_SYSTEM_ADMIN])
        evaluator.hasPermission(authentication, 123, 'DomainEntity', ZuulDataConstants.PERMISSION_ADMIN)
    }

    @Test
    void shouldQueryForEnvironmentWhenIdentifierIsSupplied() {
        def authentication = createAuthentication([ZuulDataConstants.ROLE_SYSTEM_ADMIN])
        assert evaluator.hasPermission(authentication, "dev", Environment.class.name, ZuulDataConstants.PERMISSION_ADMIN)
        verify(evaluator.environmentDao).findOne("dev")
    }

    // --------- PERMISSION_ADMIN
    @Test
    void shouldAllowAdminPermIfRestrictedAndUserIsSysAdmin() {
        def authentication = createAuthentication([ZuulDataConstants.ROLE_SYSTEM_ADMIN])
        assert evaluator.hasPermission(authentication, restrictedEnv, ZuulDataConstants.PERMISSION_ADMIN)
    }

    @Test
    void shouldAllowAdminPermIfNotRestrictedAndUserIsSysAdmin() {
        def authentication = createAuthentication([ZuulDataConstants.ROLE_SYSTEM_ADMIN])
        assert evaluator.hasPermission(authentication, nonRestrictedEnv, ZuulDataConstants.PERMISSION_ADMIN)
    }

    @Test
    void shouldNotAllowAdminPermIfRestrictedAndUserIsAdmin() {
        def authentication = createAuthentication([ZuulDataConstants.ROLE_ADMIN])
        assert !evaluator.hasPermission(authentication, restrictedEnv, ZuulDataConstants.PERMISSION_ADMIN)
    }

    @Test
    void shouldAllowAdminPermIfNotRestrictedAndUserIsAdmin() {
        def authentication = createAuthentication([ZuulDataConstants.ROLE_ADMIN])
        assert evaluator.hasPermission(authentication, nonRestrictedEnv, ZuulDataConstants.PERMISSION_ADMIN)
    }

    @Test
    void shouldNotAllowAdminPermIfUserIsNotAnAdmin() {
        def authentication = createAuthentication([ZuulDataConstants.ROLE_USER])
        assert !evaluator.hasPermission(authentication, nonRestrictedEnv, ZuulDataConstants.PERMISSION_ADMIN)
        assert !evaluator.hasPermission(authentication, restrictedEnv, ZuulDataConstants.PERMISSION_ADMIN)
    }


    protected Authentication createAuthentication(List<String> roles) {
        return new TestingAuthenticationToken("test user", "********", roles as String[])
    }
}
