package com.github.gcexcel.core.config;

import com.github.gcexcel.core.utils.SecurityUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName
 * @author jiayutao
 * @create 2022-02-22
 * @desc
 **/


@NotProguard
@Component
public class LicenseConfig implements InitializingBean {
    @Autowired
    private SecurityUtils securityUtils;

    /**
     *  初始化GCExcel的证书
     */

    @Override
    public void afterPropertiesSet() {
        System.out.println("=============================开始加载GC证书===================================");
        securityUtils.setLicenseKey();
        System.out.println("=============================加载GC证书结束===================================");
    }
}
