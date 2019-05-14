package com.fzm.fzm_port;

public class HexUtils {

    /**
     * 发送命令
     * @param s
     * @return
     */
    public static byte[] Stringtobytes(String s) {
        byte[] present = {};
        if (Integer.parseInt(s) >= 16) {
            present = hexString2Bytes(Integer.toHexString(Integer.parseInt(s)));
        }else if(Integer.parseInt(s) == 0){
            present = new byte[]{0x00};
        }else if(Integer.parseInt(s) == 1){
            present = new byte[]{0x01};
        }else if(Integer.parseInt(s) == 2){
            present = new byte[]{0x02};
        }else if(Integer.parseInt(s) == 3){
            present = new byte[]{0x03};
        }else if(Integer.parseInt(s) == 4){
            present = new byte[]{0x04};
        }else if(Integer.parseInt(s) == 5){
            present = new byte[]{0x05};
        }else if(Integer.parseInt(s) == 6){
            present = new byte[]{0x06};
        }else if(Integer.parseInt(s) == 7){
            present = new byte[]{0x07};
        }else if(Integer.parseInt(s) == 8){
            present = new byte[]{0x08};
        }else if(Integer.parseInt(s) == 9){
            present = new byte[]{0x09};
        }else if(Integer.parseInt(s) == 10){
            present = new byte[]{0x0a};
        }else if(Integer.parseInt(s) == 11){
            present = new byte[]{0x0b};
        }else if(Integer.parseInt(s) == 12){
            present = new byte[]{0x0c};
        }else if(Integer.parseInt(s) == 13){
            present = new byte[]{0x0d};
        }else if(Integer.parseInt(s) == 14){
            present = new byte[]{0x0e};
        }else if(Integer.parseInt(s) == 15){
            present = new byte[]{0x0f};
        }

        return present;
    }

    /**
     * 16进制byte数组转String
      * @param b
     * @return
     */

    public static String bytes2HexString(byte[] b ,int length) {
        String r = "";

        for (int i = 0; i <length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }

        return r;
    }

    /*
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))){
            return null;
        }
        else if (hex.length()%2 != 0){
            return null;
        }
        else{
            hex = hex.toUpperCase();
            int len = hex.length()/2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i=0; i<len; i++){
                int p=2*i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p+1]));
            }
            return b;
        }

    }
    /*
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
