package com.confluex.zuul.web

import org.junit.Test

import com.confluex.zuul.web.test.ZuulWebIntegrationTest
import org.springframework.beans.factory.annotation.Autowired
import com.confluex.zuul.data.dao.EnvironmentDao
import com.confluex.zuul.data.dao.SettingsGroupDao


class EnvironmentControllerIntegrationTest extends ZuulWebIntegrationTest {
    @Autowired
    EnvironmentDao environmentDao

    @Autowired
    SettingsGroupDao settingsGroupDao

    @Autowired
    EnvironmentController controller

    @Test
    void shouldCascadeDeleteEnvironmentSettings() {
        loginAsUser(LOGIN_ROLE_SYSTEM_ADMIN)
        def env = environmentDao.findById("prod").get()
        def groups = env.groups
        assert groups
        controller.delete("prod")
        assert !environmentDao.findById("prod").get()
        groups.each {
            assert !settingsGroupDao.findById(it.id).get()
        }
    }
}
