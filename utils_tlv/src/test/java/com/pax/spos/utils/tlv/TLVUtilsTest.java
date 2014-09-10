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

    @Test
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
        assertEquals(5, res.size());
        String res3 = ByteStringHex.bytes2HexStr(TLVUtils.TLV2Bytes(res.get(2)));
//        System.out.println("testBytes2NestedFlatTLVs "+res3);
        hexStr="CF 00 00 00 02 A0 01 C1 01 01 03 03 33 2E 31";
        test=ByteStringHex.hexStr2Bytes(hexStr);
        res=TLVUtils.bytes2NestedFlatTLVs(test);
        System.out.println("testBytes2NestedFlatTLVs "+res3);
        hexStr = "C1234567 00 E10100001DC101010303010105E101020310C101020303027776C101030303020122";
        test=ByteStringHex.hexStr2Bytes(hexStr);
        res=TLVUtils.bytes2NestedFlatTLVs(test);
        assertEquals(5, res.size());

        hexStr = "E101000022C101010303010105 C123456700 E101020310C101020303027776C101030303020122";
        test=ByteStringHex.hexStr2Bytes(hexStr);
        res=TLVUtils.bytes2NestedFlatTLVs(test);
        assertEquals(5, res.size());

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
        assertEquals(6, res.size());

    }

    @Test
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

    @Test
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

        TLVUtils.addSubTLV(tlv11, tlv1);
        TLVUtils.addSubTLV(tlv121, tlv12);
        TLVUtils.addSubTLV(tlv122, tlv12);
        TLVUtils.addSubTLV(tlv12, tlv1);
//        System.out.println("11 testTLV2Bytes tlv="+tlv1);

        res = TLVUtils.TLV2Bytes(tlv1);
        //E10100001DC101010303010105E101020310C101020303027776C101030303020122
//        System.out.println("===========13 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
        List<TLV> tlvs = new ArrayList<TLV>();
        tlvs.add(tlv);
        tlvs.add(tlv1);
        res = TLVUtils.TLVs2Bytes(tlvs);
        //C101020303000105 E10100001DC101010303010105E101020310C101020303027776C101030303020122
//        System.out.println("===========14 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
        assertEquals((byte) (0xC1), res[0]);
        assertEquals((byte) (0x22), res[res.length - 1]);
        assertEquals((byte) (0x1D), res[12]);
        assertEquals((byte) (0x10), res[25]);
    }
}