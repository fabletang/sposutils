package com.pax.spos.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ByteStringHexTest {

    @Test
    public void testHexStr2Bytes() throws Exception {

        String test = "0102 03";
        byte[] bytes = ByteStringHex.hexStr2Bytes(test);
        int i = (int) (bytes[2]);
        assertEquals(3, i);

    }

    @Test
    public void testBytes2HexStr() throws Exception {
        //0104 0000 0a
        byte[] test = {(byte) 0x01, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x0a};
        String str = ByteStringHex.bytes2HexStr(test);
        assertEquals("010400000A", str);

    }

    @Test
    public void testBcd2Str() throws Exception {
        byte[] test = {(byte) 0x02, (byte) 0x1a};
        String str = ByteStringHex.bcd2Str(test);
        assertEquals("2110", str);
         byte[] test2 = {(byte) 0x03, (byte) 0x21};
        str = ByteStringHex.bcd2Str(test2);
        assertEquals("321", str);
    }

    @Test
    public void testStr2Bcd() throws Exception {
        String test = "0113";
        byte[] bytes = ByteStringHex.str2Bcd(test);
        assertEquals((byte) (0x13), bytes[1]);
        assertEquals((byte) (0x01), bytes[0]);
    }

    @Test
    public void testBytes2Int() throws Exception {
        String hexStr = "0F00 0001";
        byte[] bytes = ByteStringHex.hexStr2Bytes(hexStr);
        assertEquals((byte) (0x0F), bytes[0]);
        assertEquals((byte) (0x01), bytes[3]);
        int i = ByteStringHex.bytes2Int(bytes);
        assertEquals(251658241, i);
        hexStr = "0F 0001";
        bytes = ByteStringHex.hexStr2Bytes(hexStr);
        assertEquals(3, bytes.length);
        assertEquals((byte) (0x0F), bytes[0]);
        assertEquals((byte) (0x01), bytes[2]);
        i = ByteStringHex.bytes2Int(bytes);
        assertEquals(983041, i);
    }

    @Test
    public void testInt2Bytes() throws Exception {
        int i = 251658241;
        byte[] bytes = ByteStringHex.int2Bytes(i);
        assertEquals(4, bytes.length);
        assertEquals((byte) (0x0F), bytes[0]);
        assertEquals((byte) (0x01), bytes[3]);
        i = 983041;
        bytes = ByteStringHex.int2Bytes(i);
        assertEquals(4, bytes.length);
        assertEquals((byte) (0x0F), bytes[1]);
        assertEquals((byte) (0x01), bytes[3]);
    }

    @Test
    public void testGBKUTF8() throws Exception {
        String str = "a中文字";
        String str2 = ByteStringHex.GBKStr2UTF8Str(ByteStringHex.UTF8Str2GBKStr(str));
        assertEquals(str, str2);
    }

    @Test
    public void testCalculateCRC() throws Exception {

    }

    @Test
    public void testCalculateLRC() throws Exception {

    }

    @Test
    public void testHexStrEqualByte() throws Exception {
        String str = "3C";
        byte b = (byte) (0x3C);
        boolean result = ByteStringHex.hexStrEqualByte(str, b);
        assertEquals(true, result);
    }

    @Test
    public void testHexStrEqualHi4bit() throws Exception {
        String str = "3";
        byte b = (byte) (0x3C);
        boolean result = ByteStringHex.hexStrEqualHi4bit(str, b);
        assertEquals(true, result);
    }

    @Test
    public void testHexStrEqualLo4bit() throws Exception {
        String str = "C";
        byte b = (byte) (0x3C);
        boolean result = ByteStringHex.hexStrEqualLo4bit(str, b);
        assertEquals(true, result);
    }

    @Test
    public void testInt2BytesN() throws Exception {
        int i = 0x3FD;
        String str;
        str = ByteStringHex.bytes2HexStr(ByteStringHex.int2BytesN(i));
        assertEquals("03FD", str);
        i = 0x12FC;
        str = ByteStringHex.bytes2HexStr(ByteStringHex.int2BytesN(i));
        assertEquals("12FC", str);
        i = 0xE2FC1234;
        str = ByteStringHex.bytes2HexStr(ByteStringHex.int2BytesN(i));
        assertEquals("E2FC1234", str);

        i = 16;
        str = ByteStringHex.bytes2HexStr(ByteStringHex.int2BytesN(i));
        assertEquals("10", str);
    }
    @Test
    public void testInt2FixBcd() throws Exception{
       int i=12345678;
       byte[] bcd=ByteStringHex.int2FixBcd(i,4);
//        System.out.println("--testInt2FixBcd "+ByteStringHex.bytes2HexStr(bcd));
        assertEquals(4, bcd.length);
       assertEquals(0x12, bcd[0]);
         bcd=ByteStringHex.int2FixBcd(i,5);
//        System.out.println("--testInt2FixBcd "+ByteStringHex.bytes2HexStr(bcd));
        assertEquals(5, bcd.length);
        assertEquals(0x12, bcd[1]);

    }
    @Test
    public void testBcd2Int() throws Exception{
        String hexStr="1234";
        byte[] bcd=ByteStringHex.hexStr2Bytes(hexStr);
        int i=ByteStringHex.bcd2Int(bcd);
        assertEquals(1234,i);

        hexStr="12345";
        bcd=ByteStringHex.hexStr2Bytes(hexStr);
//        System.out.println("--testBcd2Int "+ByteStringHex.bytes2HexStr(bcd));
        i=ByteStringHex.bcd2Int(bcd);
        assertEquals(12345,i);

        hexStr="1234567";
        bcd=ByteStringHex.hexStr2Bytes(hexStr);
//        System.out.println("--testBcd2Int "+ByteStringHex.bytes2HexStr(bcd));
        i=ByteStringHex.bcd2Int(bcd);
        assertEquals(1234567,i);
        hexStr="12345678";
        bcd=ByteStringHex.hexStr2Bytes(hexStr);
//        System.out.println("--testBcd2Int "+ByteStringHex.bytes2HexStr(bcd));
        i=ByteStringHex.bcd2Int(bcd);
        assertEquals(12345678,i);
    }
    @Test
    public void testInt2Asc() throws Exception{
        int i=1234;
        byte[] asc=ByteStringHex.int2Asc(i);
        assertNotNull(asc);
        assertEquals(0x31,asc[0]);
        assertEquals(0x34,asc[3]);
        i=-1234;
         asc=ByteStringHex.int2Asc(i);
        assertNotNull(asc);
        assertEquals(0x2D,asc[0]);
        assertEquals(0x31,asc[1]);
        assertEquals(0x34,asc[4]);
    }
    @Test
    public void testAsc2Int() throws Exception{
      String hexStr="31 32 33 34";
      byte[] asc=ByteStringHex.hexStr2Bytes(hexStr);
      int i=ByteStringHex.asc2Int(asc);
       assertEquals(1234,i);

        hexStr="2D 31 32 33 34";
        asc=ByteStringHex.hexStr2Bytes(hexStr);
        i=ByteStringHex.asc2Int(asc);
        assertEquals(-1234,i);
    }
}
