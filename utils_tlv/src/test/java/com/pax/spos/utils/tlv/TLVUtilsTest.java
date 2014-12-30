package com.pax.spos.utils.tlv;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.tlv.model.TLV;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TLVUtilsTest {

    @Test
    public void testJustConstructed() throws Exception {

        String hexStr = "E1010000";
        int tag = ByteStringHex.hex8Str2int(hexStr);
        boolean dest = TLVUtils.justConstructed(tag);
        assertEquals(true, dest);
//        System.out.println("E1010000 isConstructed:"+TLVUtils.justConstructed(tag));
        hexStr = "C1 01 00 02";
        tag = ByteStringHex.hex8Str2int(hexStr);
        dest = TLVUtils.justConstructed(tag);
        assertEquals(false, dest);
    }

    @Test
    public void testJustArray() throws Exception {
        String hexStr = "E1 01 00 12";
        int tag = ByteStringHex.hex8Str2int(hexStr);
        boolean dest = TLVUtils.justArray(tag);
        assertEquals(true, dest);
        hexStr = "E1 01 00 02";
        tag = ByteStringHex.hex8Str2int(hexStr);
        dest = TLVUtils.justArray(tag);
        assertEquals(false, dest);

    }

    @Test
    public void testJustDataType() throws Exception {

    }

    @Test
    public void testProcessTag() throws Exception {

    }

    //@Test
    public void testFindByTag() throws Exception {
        String hexStr = "E101000015C101010303010105E101020308C101020303027776";
        byte[] test = ByteStringHex.hexStr2Bytes(hexStr);
//        List<TLV> res=TLVUtils.bytes2TopNestedTLVs(test);
        List<TLV> res = TLVUtils.bytes2NestedFlatTLVs(test);
//        List<TLV> res=TLVUtils.bytes2FlatTLVs(test);
        assertEquals(4, res.size());
        List<TLV> tlvs = TLVUtils.findByTag(0xE1010203, res);
        assertEquals(8, tlvs.get(0).getLength());
         hexStr = "E10100001DC101010303010105E101020310C101020303027776C101030303020122";
         test = ByteStringHex.hexStr2Bytes(hexStr);
        res = TLVUtils.bytes2NestedFlatTLVs(test);
        tlvs = TLVUtils.findByTag(0xC1010303, res);
        assertEquals(1, tlvs.size());
//        System.out.println("tlv ="+tlvs.get(0));
        tlvs = TLVUtils.findByTag(0xC8010103,res);
//        assertEquals(0,tlvs.size());
        assertNull(tlvs);

    }

    @Test
    public void testBytes2NestedFlatTLVs() throws Exception {

//        String hexStr= "E101000015C101010303010105E101020308C101020303027776";
//        String hexStr = "E10100001DC101010303010105E101020310C101020303027776C101030303020122";
        String hexStr = "E10100001DC101010303010105E101020310C101020303027776C101030303020122 C123456700";
        byte[] test = ByteStringHex.hexStr2Bytes(hexStr);
//        List<TLV> res=TLVUtils.bytes2TopNestedTLVs(test);
        List<TLV> res = TLVUtils.bytes2NestedFlatTLVs(test);
//        List<TLV> res=TLVUtils.bytes2FlatTLVs(test);
        System.out.println("res="+res);
        assertEquals(6, res.size());

        String res3 = ByteStringHex.bytes2HexStr(TLVUtils.TLV2Bytes(res.get(2)));
//        System.out.println("testBytes2NestedFlatTLVs "+res3);
        hexStr="CF 00 00 00 02 A0 01 C1 01 01 03 03 33 2E 31";
        test=ByteStringHex.hexStr2Bytes(hexStr);
        res=TLVUtils.bytes2NestedFlatTLVs(test);
        System.out.println("testBytes2NestedFlatTLVs "+res3);
        hexStr = "C1234567 00 E10100001DC101010303010105E101020310C101020303027776C101030303020122";
        test=ByteStringHex.hexStr2Bytes(hexStr);
        res=TLVUtils.bytes2NestedFlatTLVs(test);
        assertEquals(6, res.size());

        hexStr = "E101000022C101010303010105 C123456700 E101020310C101020303027776C101030303020122";
        test=ByteStringHex.hexStr2Bytes(hexStr);
        res=TLVUtils.bytes2NestedFlatTLVs(test);
        assertEquals(6, res.size());

        hexStr= "CF 00 00 00 02 A1 01 " +
                "C2 01 02 03 01 01 " +
                "E2 01 03 10 81 80 " +
                "C2 01 04 05 4B 42 36 32 32 35 37 36 38 37 30 39 37 34 38 38 30 38 5E 48 55 41 4E 47 20 57 " +
                "45 49 20 50 45 4E 47 20 20 20 20 20 20 20 20 20 20 20 5E 31 39 30 37 31 30 31 31 35 37 30 " +
                "32 20 20 20 20 39 39 39 39 30 30 31 35 38 30 30 30 30 30 30 " +
                "C2 01 05 05 20 36 32 32 35 37 36 38 37 30 39 37 34 38 38 30 38 3D 31 39 30 37 31 30 31 31 " +
                "35 37 30 32 31 35 38 " +
                "C2 01 06 05 01 02 " +
                "C2 01 07 03 00 ";
//        System.out.println(hexStr.trim().length()/2);
//        System.out.println(ByteStringHex.hexStr2Bytes(hexStr).length);
        test=ByteStringHex.hexStr2Bytes(hexStr);
        res=TLVUtils.bytes2NestedFlatTLVs(test);
        assertEquals(7, res.size());

    }

    //@Test
    public void testBytes2TopNestedTLVs() throws Exception {
//    String hexStr= "E101000015C101010303010105E101020308C101020303027776";
        String hexStr = "E10100001DC101010303010105E101020310C101020303027776C101030303020122";
        byte[] test = ByteStringHex.hexStr2Bytes(hexStr);
        List<TLV> res = TLVUtils.bytes2TopNestedTLVs(test);
//        List<TLV> res=TLVUtils.bytes2FlatTLVs(test);
//        List<TLV> res=TLVUtils.bytes2FlatTLVs(test);
        assertEquals(1, res.size());
        String res0 = ByteStringHex.bytes2HexStr(TLVUtils.TLV2Bytes(res.get(0)));
        assertEquals(hexStr, res0);
    }

    //@Test
    public void testTLV2Bytes() throws Exception {
//        String  "E101000015C101010303010105E101020310C101020303027776";
        String hexStr = "C1 01 02 03";
        int tag = ByteStringHex.hex8Str2int(hexStr);
        byte[] value = ByteStringHex.hexStr2Bytes("00 01 05");
        TLV tlv = new TLV();
        tlv.setTag(tag);
        tlv.setValue(value);
//        System.out.println("testTLV2Bytes tlv="+tlv);
        byte[] res = TLVUtils.TLV2Bytes(tlv);
        assertEquals(8, res.length);
//        System.out.println("=="+Byte.parseByte("C",16));
        assertEquals((byte) (0xC1), res[0]);
        assertEquals((byte) (0x05), res[7]);
        tlv = TLVUtils.processTag(tlv.getTag(), tlv);
//        System.out.println("testTLV2Bytes tlv="+tlv);
        // System.out.println("===========12 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
        hexStr = "E1 01 00 00";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("00 01 05");
        TLV tlv1 = new TLV();
        tlv1.setTag(tag);

        hexStr = "C1 01 01 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("01 01 05");
        TLV tlv11 = new TLV(tag, value);

//        hexStr="E1 01 02 03";
        hexStr = "C1 01 02 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 11 06");
//        TLV tlv12=new TLV(tag,value);
        TLV tlv12 = new TLV();
        tlv12.setTag(tag);
        tlv12.setValue(value);

        hexStr = "C1 01 02 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 77 76");
        TLV tlv121 = new TLV(tag, value);

        hexStr = "C1 01 03 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 01 22");
        TLV tlv122 = new TLV(tag, value);

        tlv122 = new TLV("C1010303", "020122");

        TLVUtils.addSubTLV(tlv11, tlv1);
        TLVUtils.addSubTLV(tlv121, tlv12);
        TLVUtils.addSubTLV(tlv122, tlv12);
        TLVUtils.addSubTLV(tlv12, tlv1);
//        System.out.println("11 testTLV2Bytes tlv="+tlv1);

        res = TLVUtils.TLV2Bytes(tlv1);
        //E10100001DC101010303010105E101020310C101020303027776C101030303020122
//        System.out.println("===========13 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
        assertEquals((byte) (0xE1), res[0]);
        assertEquals((byte) (0x22), res[res.length - 1]);
        assertEquals((byte) (0x1D), res[4]);
        assertEquals((byte) (0x10), res[17]);
    }

    @Test
    public void testTLVs2Bytes() throws Exception {

//        String  "E101000015C101010303010105E101020310C101020303027776";
        String hexStr = "C1 01 02 03";
        int tag = ByteStringHex.hex8Str2int(hexStr);
        byte[] value = ByteStringHex.hexStr2Bytes("00 01 05");
        TLV tlv = new TLV();
        tlv.setTag(tag);
        tlv.setValue(value);
//        System.out.println("testTLV2Bytes tlv="+tlv);
        byte[] res = TLVUtils.TLV2Bytes(tlv);
        assertEquals(8, res.length);
//        System.out.println("=="+Byte.parseByte("C",16));
        assertEquals((byte) (0xC1), res[0]);
        assertEquals((byte) (0x05), res[7]);
        tlv = TLVUtils.processTag(tlv.getTag(), tlv);
//        System.out.println("testTLV2Bytes tlv="+tlv);
        // System.out.println("===========12 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
        hexStr = "E1 01 00 00";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("00 01 05");
        TLV tlv1 = new TLV();
        tlv1.setTag(tag);

        hexStr = "C1 01 01 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("01 01 05");
        TLV tlv11 = new TLV(tag, value);

//        hexStr="E1 01 02 03";
        hexStr = "C1 01 02 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 11 06");
//        TLV tlv12=new TLV(tag,value);
        TLV tlv12 = new TLV();
        tlv12.setTag(tag);
        tlv12.setValue(value);

        hexStr = "C1 01 02 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 77 76");
        TLV tlv121 = new TLV(tag, value);

        hexStr = "C1 01 03 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 01 22");
        TLV tlv122 = new TLV(tag, value);

        tlv122 = new TLV("C1010303", "020122");

        hexStr = "C1 23 45 67";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value =null;
        TLV tlv123 = new TLV(tag, value);

        TLVUtils.addSubTLV(tlv11, tlv1);
        TLVUtils.addSubTLV(tlv121, tlv12);
        TLVUtils.addSubTLV(tlv122, tlv12);
        TLVUtils.addSubTLV(tlv12, tlv1);
        TLVUtils.addSubTLV(tlv123,tlv1);
//        System.out.println("11 testTLV2Bytes tlv="+tlv1);

        res = TLVUtils.TLV2Bytes(tlv1);
        //E10100001DC101010303010105E101020310C101020303027776C101030303020122
//        System.out.println("===========13 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
        List<TLV> tlvs = new ArrayList<TLV>();
        tlvs.add(tlv);
        tlvs.add(tlv1);
        res = TLVUtils.TLVs2Bytes(tlvs);
        //C1010203 03000105 E1010000 1DC101010303010105E101020310C101020303027776C101030303020122
        System.out.println("===========14 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
        assertEquals((byte) (0xC1), res[0]);
        assertEquals((byte) (0x22), res[res.length - 6]);
        assertEquals((byte) (0x22), res[12]);
        assertEquals((byte) (0x15), res[25]);
    }
   @Test
    public void tests() throws Exception {
       String test="CF 00 00 00 02 A5 04 C6 04 01 03 82 01 08 60 00 00 04 01 60 31 00 31 31 76 08 10 00 18 00 00 0A C0 00 14 14 52 19 12 29 31 34 35 32 31 39 36 36 38 38 30 30 30 30 30 30 30 30 30 30 32 32 31 30 32 33 31 30 30 36 30 35 31 30 30 30 32 00 11 00 00 00 54 37 00 01 91 31 9F 06 05 A0 00 00 00 65 9F 22 01 09 DF 05 08 32 30 30 39 31 32 33 31 DF 06 01 01 DF 07 01 01 DF 02 81 80 B7 2A 8F EF 5B 27 F2 B5 50 39 8F DC C2 56 F7 14 BA D4 97 FF 56 09 4B 74 08 32 8C B6 26 AA 6F 0E 6A 9D F8 38 8E B9 88 7B C9 30 17 0B CC 12 13 E9 0F C0 70 D5 2C 8D CD 0F F9 E1 0F AD 36 80 1F E9 3F C9 98 A7 21 70 50 91 F1 8B C7 C9 82 41 CA DC 15 A2 B9 DA 7F B9 63 14 2C 0A B6 40 D5 D0 13 5E 77 EB AE 95 AF 1B 4F EF AD CF 9C 01 23 66 BD DA 04 55 C1 56 4A 68 81 0D 71 27 67 6D 49 38 90 BD DF 04 01 03 DF 03 14 44 10 C6 D5 1C 2F 83 AD FD 92 52 8F A6 E3 8A 32 DF 04 8D 0A C6 04 04 02 02 02 64 ";
       test=test.replace(" ","");
        byte[] testBytes=ByteStringHex.hexStr2Bytes(test);

        System.out.println("testbytes len="+testBytes.length);
        List<TLV> tlvs = new ArrayList<TLV>();
        tlvs=TLVUtils.bytes2NestedFlatTLVs(testBytes);
        List<TLV> findTlvs=TLVUtils.findByTag(0xC6040103,tlvs);
       assertNotNull(findTlvs);
        assertNotNull(findTlvs.get(0));
        System.out.println("tlvs len=" + tlvs.size());
        System.out.println("findtlvs len=" + findTlvs.size());
        System.out.println("findtlvs len=" + findTlvs.get(0).getLength());
        System.out.println("findtlvs value len=" + findTlvs.get(0).getValue().length);
        System.out.println("0x82=" + 0x82);
        System.out.println("0x0108=" + 0x0108);
        System.out.println("0x60="+0x60);

        assertEquals(findTlvs.get(0).getValue()[0],0x60);
        assertEquals(findTlvs.get(0).getValue().length,0x0108);
//        assertEquals(findTlvs.get(0).getLength(),0x82);

    }
    @Test
    public void tests3() throws Exception {
        String test = "CF 00 00 01 02 A5 06 C6 05 01 02 02 00 10";
        test = test.replace(" ", "");
        byte[] testBytes = ByteStringHex.hexStr2Bytes(test);

        System.out.println("testbytes len=" + testBytes.length);
        List<TLV> tlvs = new ArrayList<TLV>();
        tlvs=TLVUtils.bytes2NestedFlatTLVs(testBytes);
        List<TLV> findTlvs=TLVUtils.findByTag(0xCf000001,tlvs);
        System.out.println("----"+ByteStringHex.bytes2HexStr(findTlvs.get(0).getValue()));
//        assertEquals(0xA5,findTlvs.get(0).getValue()[0]);
        assertEquals(2,tlvs.size());
         findTlvs=TLVUtils.findByTag(0xC6050102,tlvs);
        assertNotNull(findTlvs);
        assertNotNull(findTlvs.get(0));

    }
     @Test
    public void tests4() throws Exception {
        String test = "CF 00 00 01 02 A5 06 C6 05 01 02 00";
        test = test.replace(" ", "");
        byte[] testBytes = ByteStringHex.hexStr2Bytes(test);

        System.out.println("testbytes len=" + testBytes.length);
        List<TLV> tlvs = new ArrayList<TLV>();
        tlvs=TLVUtils.bytes2NestedFlatTLVs(testBytes);
        List<TLV> findTlvs=TLVUtils.findByTag(0xCf000001,tlvs);
        System.out.println("----"+ByteStringHex.bytes2HexStr(findTlvs.get(0).getValue()));
//        assertEquals(0xA5,findTlvs.get(0).getValue()[0]);
        assertEquals(2,tlvs.size());
         findTlvs=TLVUtils.findByTag(0xC6050102,tlvs);
        assertNotNull(findTlvs);
        assertNotNull(findTlvs.get(0));
         assertEquals(1, findTlvs.size());
         assertEquals(0, findTlvs.get(0).getLength());
         assertNull(findTlvs.get(0).getValue());

    }
       @Test
    public void tests5() throws Exception {
        String test = "CF 00 00 00 02 A1 01 C2 01 02 03 01 01 E2 01 03 10 81 A6 C2 01 04 05 00 C2 01 05 05 25 36 32 32 35 38 38 32 31 31 30 35 38 37 36 33 38 3D 34 39 31 32 31 32 30 30 38 33 35 37 38 33 38 39 39 38 33 35 C2 01 06 05 68 39 39 36 32 32 35 38 38 32 31 31 30 35 38 37 36 33 38 3D 31 35 36 31 35 36 30 35 30 30 30 35 30 30 30 30 30 30 31 30 31 35 38 39 39 38 33 35 32 31 34 30 30 30 30 34 39 31 32 30 3D 32 31 31 30 35 38 37 36 33 38 3D 30 30 30 30 30 30 30 30 30 3D 30 35 30 30 30 30 30 30 30 32 31 30 30 30 30 30 30 30 30 30 30 30 30 C2 01 07 03 00 C2 01 09 01 00";
        test = test.replace(" ", "");
        byte[] testBytes = ByteStringHex.hexStr2Bytes(test);

        System.out.println("testbytes len=" + testBytes.length);
        List<TLV> tlvs = new ArrayList<TLV>();
        tlvs=TLVUtils.bytes2NestedFlatTLVs(testBytes);
           System.out.println("tlvs size=" + tlvs.size());
           System.out.println("tlvs =" + tlvs.toString());
        List<TLV> findTlvs=TLVUtils.findByTag(0xCf000000,tlvs);
        System.out.println("----"+ByteStringHex.bytes2HexStr(findTlvs.get(0).getValue()));
//        assertEquals(0xA5,findTlvs.get(0).getValue()[0]);
//        assertEquals(2,tlvs.size());
         findTlvs=TLVUtils.findByTag(0xC2010203,tlvs);
        assertNotNull(findTlvs);
        assertNotNull(findTlvs.get(0));
         assert(findTlvs.size()>0);
//         assertEquals(0,findTlvs.get(0).getLength());
         assertNotNull(findTlvs.get(0).getValue());
//C2010505 C2010605
           findTlvs=TLVUtils.findByTag(0xC2010505 ,tlvs);
           assertNotNull(findTlvs);
    }
}