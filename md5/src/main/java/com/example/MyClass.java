package com.example;

        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;

public class MyClass {
    public static void main(String[] arg) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("md5");
        String password="123456";
        byte[] bytes=digest.digest(password.getBytes());
        StringBuffer buffer=new StringBuffer();
        for (byte b:bytes){
            //每一个byte和8个2进制位做与运算
            int number=b & 0xfff;
            String numberStr=Integer.toHexString(number);
//            System.out.print(numberStr);
            if(numberStr.length()==1){
                buffer.append("0");
            }
            buffer.append(numberStr);
        }
        System.out.print(buffer.toString());
    }
}
