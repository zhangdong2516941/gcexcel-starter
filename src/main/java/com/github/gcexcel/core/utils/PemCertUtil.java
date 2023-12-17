package com.github.gcexcel.core.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.grapecity.documents.excel.Workbook;

import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

/**
 * PemCert 证书转换为字符串
 * @Author DongDong Zhang
 * @Date 2023/12/15 16:57
 * @Description:
 * @Version 1.0
 */
public class PemCertUtil {
    private static final String  SPLIT_STR = "####";
    /**
     * 将一行base64格式的证书转化为X509Certificate
     * @param pemString
     * @return
     * @throws CertificateException
     */
    public static X509Certificate getCertificate(String pemString) throws CertificateException {
        // 将一行的 Base64 字符串转换为多行形式
        String formattedCertificateString = formatBase64String(pemString);
        // 将 PEM 证书字符串解码为字节数组
        byte[] certificateBytes = Base64.getMimeDecoder().decode(formattedCertificateString);
        // 使用 CertificateFactory 生成 Certificate 对象
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        Certificate certificate = factory.generateCertificate(new java.io.ByteArrayInputStream(certificateBytes));
        return (X509Certificate) certificate;
    }


    /**
     * 需要混淆方法内数据
     *
     * @param encryptLicenseKey
     * @param certString
     */
    public static void decryptLicense(String encryptLicenseKey, String certString)  {
        try {
            X509Certificate x509Cert = PemCertUtil.getCertificate(certString);
            Date now = new Date();
            System.out.println("start date :" + x509Cert.getNotBefore().toString());
            System.out.println("end date :" + x509Cert.getNotAfter().toString());
            if (now.before(x509Cert.getNotBefore()) || now.after(x509Cert.getNotAfter())) {
                throw new Exception("Certificate is not valid at this time");
            }
            RSA rsa = new RSA(null, x509Cert.getPublicKey());
            // 使用公钥解密
            String decryptedText =  rsa.decryptStr(encryptLicenseKey, KeyType.PublicKey , StandardCharsets.UTF_8);
            String[] split = decryptedText.split(SPLIT_STR);
            Workbook.SetLicenseKey(SecureUtil.aes(HexUtil.decodeHex(split[0])).decryptStr(split[1], StandardCharsets.UTF_8));
            return;
        }catch (Exception e){
            System.err.println(" decryptLicense解密失败:"+e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=========================未授权=======================================");
    }

    private static String formatBase64String(String base64String) {
        StringBuilder formattedString = new StringBuilder();
        int index = 0;
        while (index < base64String.length()) {
            formattedString.append(base64String, index, Math.min(index + 64, base64String.length()));
            formattedString.append("\n");
            index += 64;
        }
        return formattedString.toString();
    }

}
