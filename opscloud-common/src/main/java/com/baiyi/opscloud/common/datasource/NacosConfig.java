package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/11/11 3:23 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NacosConfig extends BaseDsConfig {

    private Nacos nacos;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Nacos {

        private String version;
        private String url;
        private Account account;
        private List<String> roles;

        public Map<String, String> getLoginParam() {
            Map<String, String> loginParam = Maps.newHashMap();
            loginParam.put("username", this.account.getUsername());
            loginParam.put("password", this.account.getPassword());
            return loginParam;
        }

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Account {

        private String prefix;
        private String username;
        private String password;

    }

}