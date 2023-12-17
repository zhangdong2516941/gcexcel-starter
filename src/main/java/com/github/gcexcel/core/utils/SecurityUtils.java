package com.github.gcexcel.core.utils;

import com.github.gcexcel.core.config.NotProguard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author DongDong Zhang
 * @Date 2023/12/13 16:35
 * @Description:
 * @Version 1.0
 */
@NotProguard
@Component
public class SecurityUtils {

    /**
     * 加密之后的license
     */
    @Value("${encryptLicenseKey:}")
    private String encryptLicenseKey;
    /**
     * base64格式证书
     */
    @Value("${pemCert:}")
    private String pemCert;
    @Value("${defaultLicenseKey:}")
    private String defaultLicenseKey;


    public void setEncryptLicenseKey(String encryptLicenseKey) {
        this.encryptLicenseKey = encryptLicenseKey;
    }

    public void setPemCert(String pemCert) {
        this.pemCert = pemCert;
    }
    public void setDefaultLicenseKey(String defaultLicenseKey) {
        this.defaultLicenseKey = defaultLicenseKey;
    }

    /**
     * 每24小时执行一次
     * @throws Exception
     */
//    @Scheduled(fixedRate = 24 * 60 * 60_000)
    public  void setLicenseKey()  {
        System.out.println("================定时任务扫描================");
        PemCertUtil.decryptLicense(encryptLicenseKey, pemCert);
    }




}
