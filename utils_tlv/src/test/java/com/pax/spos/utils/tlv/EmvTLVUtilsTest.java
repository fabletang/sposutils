package com.pax.spos.utils.tlv;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.tlv.model.EmvTLV;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EmvTLVUtilsTest {

    @Test
    public void testJustConstructed() throws Exception {
        byte tagFirt= (byte)0x21;
        boolean dest = EmvTLVUtils.justConstructed(tagFirt);
        assertEquals(true, dest);
        tagFirt= (byte)0x31;
         dest = EmvTLVUtils.justConstructed(tagFirt);
        assertEquals(true, dest);
        tagFirt= (byte)0x16;
        dest = EmvTLVUtils.justConstructed(tagFirt);
        assertEquals(false, dest);
    }


//    @Test
    public void testFindByTag() throws Exception {
        String hexStr = "28 15 C101010303010105E101020308C101020303027776";
        byte[] test = ByteStringHex.hexStr2Bytes(hexStr);
//        List<EmvTLV> res=EmvTLVUtils.bytes2TopNestedTLVs(test);
        List<EmvTLV> res = EmvTLVUtils.bytes2NestedFlatTLVs(test);
//        List<EmvTLV> res=EmvTLVUtils.bytes2FlatTLVs(test);
        assertEquals(4, res.size());
        List<EmvTLV> tlvs = EmvTLVUtils.findByTag(0xE1010203, res);
        assertEquals(8, tlvs.get(0).getLength());
         hexStr = "E10100001DC101010303010105E101020310C101020303027776C101030303020122";
         test = ByteStringHex.hexStr2Bytes(hexStr);
        res = EmvTLVUtils.bytes2NestedFlatTLVs(test);
        tlvs = EmvTLVUtils.findByTag(0xC1010303, res);
        assertEquals(1, tlvs.size());
//        System.out.println("tlv ="+tlvs.get(0));
        tlvs = EmvTLVUtils.findByTag(0xC8010103,res);
//        assertEquals(0,tlvs.size());
        assertNull(tlvs);

    }

    @Test
    public void testBytes2NestedFlatTLVs() throws Exception {

        String hexStr = "21 11 0203010105 230A 1403027776 1503020122";
        byte[] test = ByteStringHex.hexStr2Bytes(hexStr);
//        List<EmvTLV> res=EmvTLVUtils.bytes2TopNestedTLVs(test);
        List<EmvTLV> res = EmvTLVUtils.bytes2NestedFlatTLVs(test);
//        List<EmvTLV> res=EmvTLVUtils.bytes2FlatTLVs(test);
        assertEquals(5, res.size());

    }

    @Test
    public void testBytes2TopNestedTLVs() throws Exception {

//    String hexStr= "E101000015C101010303010105E101020308C101020303027776";
        //String hexStr = "E10100001DC101010303010105E101020310C101020303027776C101030303020122";
        String hexStr = "21110203010105230A14030277761503020122";
        byte[] test = ByteStringHex.hexStr2Bytes(hexStr);
        List<EmvTLV> res = EmvTLVUtils.bytes2TopNestedTLVs(test);
//        List<EmvTLV> res=EmvTLVUtils.bytes2FlatTLVs(test);
//        List<EmvTLV> res=EmvTLVUtils.bytes2FlatTLVs(test);
        System.out.println("testBytes2TopNestedTLVs res="+res);
        assertEquals(1, res.size());
        String res0 = ByteStringHex.bytes2HexStr(EmvTLVUtils.TLV2Bytes(res.get(0)));

        System.out.println("testBytes2TopNestedTLVs res0="+res0);
        assertEquals(hexStr, res0);
    }

    @Test
    public void testTLV2Bytes() throws Exception {
//        String  "E101000015C101010303010105E101020310C101020303027776";
        String hexStr = "01";
        int tag = ByteStringHex.hex8Str2int(hexStr);
        byte[] value = ByteStringHex.hexStr2Bytes("00 01 05");
        EmvTLV tlv = new EmvTLV();
        tlv.setTag(tag);
        tlv.setValue(value);
//        System.out.println("testTLV2Bytes tlv="+tlv);
        byte[] res = EmvTLVUtils.TLV2Bytes(tlv);
        assertEquals(5, res.length);
//        System.out.println("=="+Byte.parseByte("C",16));
        assertEquals((byte) (0x01), res[0]);
        assertEquals((byte) (0x01), res[3]);
//      ?????  tlv = EmvTLVUtils.processTag(tlv.getTag(), tlv);
//        System.out.println("testTLV2Bytes tlv="+tlv);
        // System.out.println("===========12 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
       //21 11 02 03010105 23 0A 14030277 76150302 0122
        hexStr = "21";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("00 01 05");
        EmvTLV tlv1 = new EmvTLV();
        tlv1.setTag(tag);

        hexStr = "02";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("01 01 05");
        EmvTLV tlv11 = new EmvTLV(tag, value);

//        hexStr="E1 01 02 03";
        hexStr = "23";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 11 06");
//        EmvTLV tlv12=new EmvTLV(tag,value);
        EmvTLV tlv12 = new EmvTLV();
        tlv12.setTag(tag);
        tlv12.setValue(value);

        hexStr = "14";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 77 76");
        EmvTLV tlv121 = new EmvTLV(tag, value);

        hexStr = "15";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 01 22");
        EmvTLV tlv122 = new EmvTLV(tag, value);

//        tlv122 = new EmvTLV("C1010303", "020122");

        EmvTLVUtils.addSubTLV(tlv11, tlv1);
        EmvTLVUtils.addSubTLV(tlv121, tlv12);
        EmvTLVUtils.addSubTLV(tlv122, tlv12);
        EmvTLVUtils.addSubTLV(tlv12, tlv1);
//        System.out.println("11 testTLV2Bytes tlv="+tlv1);

        res = EmvTLVUtils.TLV2Bytes(tlv1);
        //E10100001DC101010303010105E101020310C101020303027776C101030303020122
        System.out.println("===========13 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
        assertEquals((byte) (0x21), res[0]);
        assertEquals((byte) (0x11), res[1]);
        assertEquals((byte) (0x22), res[res.length - 1]);
        assertEquals((byte) (0x01), res[5]);
    }

//    @Test
    public void testTLVs2Bytes() throws Exception {

//        String  "E101000015C101010303010105E101020310C101020303027776";
        String hexStr = "C1 01 02 03";
        int tag = ByteStringHex.hex8Str2int(hexStr);
        byte[] value = ByteStringHex.hexStr2Bytes("00 01 05");
        EmvTLV tlv = new EmvTLV();
        tlv.setTag(tag);
        tlv.setValue(value);
//        System.out.println("testTLV2Bytes tlv="+tlv);
        byte[] res = EmvTLVUtils.TLV2Bytes(tlv);
        assertEquals(8, res.length);
//        System.out.println("=="+Byte.parseByte("C",16));
        assertEquals((byte) (0xC1), res[0]);
        assertEquals((byte) (0x05), res[7]);
//     /////////   tlv = EmvTLVUtils.processTag(tlv.getTag(), tlv);
//        System.out.println("testTLV2Bytes tlv="+tlv);
        // System.out.println("===========12 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
        hexStr = "E1 01 00 00";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("00 01 05");
        EmvTLV tlv1 = new EmvTLV();
        tlv1.setTag(tag);

        hexStr = "C1 01 01 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("01 01 05");
        EmvTLV tlv11 = new EmvTLV(tag, value);

//        hexStr="E1 01 02 03";
        hexStr = "C1 01 02 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 11 06");
//        EmvTLV tlv12=new EmvTLV(tag,value);
        EmvTLV tlv12 = new EmvTLV();
        tlv12.setTag(tag);
        tlv12.setValue(value);

        hexStr = "C1 01 02 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 77 76");
        EmvTLV tlv121 = new EmvTLV(tag, value);

        hexStr = "C1 01 03 03";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value = ByteStringHex.hexStr2Bytes("02 01 22");
        EmvTLV tlv122 = new EmvTLV(tag, value);

        tlv122 = new EmvTLV("C1010303", "020122");

        hexStr = "C1 23 45 67";
        tag = ByteStringHex.hex8Str2int(hexStr);
        value =null;
        EmvTLV tlv123 = new EmvTLV(tag, value);

        EmvTLVUtils.addSubTLV(tlv11, tlv1);
        EmvTLVUtils.addSubTLV(tlv121, tlv12);
        EmvTLVUtils.addSubTLV(tlv122, tlv12);
        EmvTLVUtils.addSubTLV(tlv12, tlv1);
        EmvTLVUtils.addSubTLV(tlv123,tlv1);
//        System.out.println("11 testTLV2Bytes tlv="+tlv1);

        res = EmvTLVUtils.TLV2Bytes(tlv1);
        //E10100001DC101010303010105E101020310C101020303027776C101030303020122
//        System.out.println("===========13 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
        List<EmvTLV> tlvs = new ArrayList<EmvTLV>();
        tlvs.add(tlv);
        tlvs.add(tlv1);
        res = EmvTLVUtils.TLVs2Bytes(tlvs);
        //C1010203 03000105 E1010000 1DC101010303010105E101020310C101020303027776C101030303020122
        System.out.println("===========14 testTLV2Bytes res="+ByteStringHex.bytes2HexStr(res));
        assertEquals((byte) (0xC1), res[0]);
        assertEquals((byte) (0x22), res[res.length - 6]);
        assertEquals((byte) (0x22), res[12]);
        assertEquals((byte) (0x15), res[25]);
    }
}