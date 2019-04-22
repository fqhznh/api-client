package com.talkweb.unicom.api.utils;


import com.talkweb.unicom.api.exception.ApiException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * 3DES加解密
 * @author fqh
 */
public class TripleDES {

    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish
    private static final String DES_PADDING = "DESede/ECB/PKCS5Padding";
    private static final String CHARSET = "UTF-8";
    //使用CBC需要使用初始化向量
    //private static final byte[] KEY_IV = new byte[]{0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};

    /**
     * 获取key 密钥必须为24， 否则进行截取或者用0x0补充
     * @param key
     * @return
     * @throws Exception
     */
    private static SecretKey getKey(String key) throws Exception {
        byte[] bytes = key.getBytes(CHARSET);
        if(bytes.length != 24) {
            byte[] bs = new byte[24];
            System.arraycopy(bytes, 0, bs, 0, Math.min(24, bytes.length));
            bytes = bs;
        }
        return new SecretKeySpec(bytes, Algorithm);
    }

    /**
     * 加密数据
     * @param data
     * @param key
     * @return
     */
    public static byte[] encrypt(byte[] data, String key){
        try {
            Cipher cipher = Cipher.getInstance(DES_PADDING);
            //IvParameterSpec ips = new IvParameterSpec(KEY_IV);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new ApiException("8000", "DESede加密失败", e);
        }
    }

    /**
     * 解密数据
     * @param data
     * @param key
     * @return
     */
    public static byte[] decrypt(byte[] data, String key){
        try {
            Cipher cipher = Cipher.getInstance(DES_PADDING);
            //IvParameterSpec ips = new IvParameterSpec(KEY_IV);
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new ApiException("8000", "DESede解密失败", e);
        }
    }

    /**
     * 解密数据
     * <p>
     *     使用Base64解码
     * </p>
     * @param data
     * @param key
     * @return
     */
    public static String decrypt(String data, String key) {
        try {
            byte[] bytes = Base64.decode(data);
            byte[] retBytes = decrypt(bytes, key);
            return new String(retBytes, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密数据
     * <p>
     *     使用Base64编码
     * </p>
     * @param data
     * @param key
     * @return
     */
    public static String encrypt(String data, String key) {
        try {
            byte[] bytes = data.getBytes(CHARSET);
            byte[] retBytes = encrypt(bytes, key);
            return Base64.encode(retBytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String data = "{\"nonceStr\":\"123123\"}";
        String key = "262553f144f0a8c56f60c45f";

        String encryptData = encrypt(data, key);
        String decryptData = decrypt(encryptData, key);

        System.out.println("encryptData====>" + encryptData);
        System.out.println("decryptData====>" + decryptData);
    }

}
