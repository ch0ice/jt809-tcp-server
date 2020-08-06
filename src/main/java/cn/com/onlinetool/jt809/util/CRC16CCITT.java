package cn.com.onlinetool.jt809.util;

public class CRC16CCITT {
    public static int crc16(byte[] bytes){
        int crc = 0xFFFF;
        for (int j = 0; j < bytes.length ; j++) {
            crc = ((crc  >>> 8) | (crc  << 8) )& 0xffff;
            crc ^= (bytes[j] & 0xff);//byte to int, trunc sign
            crc ^= ((crc & 0xff) >> 4);
            crc ^= (crc << 12) & 0xffff;
            crc ^= ((crc & 0xFF) << 5) & 0xffff;
        }
        crc &= 0xffff;
        return crc;
    }
    public static byte[]  crcBytes(byte[] bytes){
        byte[] int2bytes = ByteArrayUtil.int2bytes(crc16(bytes));
        byte[] int2bytes2 =  {int2bytes[2],int2bytes[3]};
        return int2bytes2;
    }

}
