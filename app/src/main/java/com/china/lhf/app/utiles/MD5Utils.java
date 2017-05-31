package com.china.lhf.app.utiles;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by C罗 on 2016/10/13.
 */
public class MD5Utils {
    public static String ecoder(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            // String password = "123456";
            // String password="e10adc3949ba59abbe56e057f20f883e";
            //String password="14e1b600b1fd579f47433b88e8d85291";
            byte[] bytes = digest.digest(password.getBytes());
            StringBuffer buffer=new StringBuffer();

            for (byte b : bytes) {
                int number = b & 0xfff;
                String numberStr= Integer.toHexString(number);
                //     System.out.println(numberStr);
                if (numberStr.length()==1){
                    buffer.append("0");

                }
                buffer.append(numberStr);

            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
