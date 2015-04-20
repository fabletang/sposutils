package com.pax.spos.utils.tlv;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.tlv.model.T2L3TLV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fable on 14-9-18.
 * 以下是全民付 TLV格式规范，全民付所有的TLV 不会出现嵌套
 * Tag用法说明
 * 为适应增值业务不断变化的需求，本子域采用TLV（tag-length-value）的表示方式，即每个子域由tag标签(T)，子域取值的长度(L)和子域取值(V)构成。
 * 标签(T)属性为bit，由16进制表示，占2个字节长度。例如，“9F33”为一个占用两个字节的tag标签。
 * 子域长度(L)属性也为bit，占1～3个字节长度。具体编码规则如下
 * a)当L字段最左边字节的最左bit位（即bit8）为0，表示该L字段占一个字节，它的后续7个bit位（即bit7～bit1）表示子域取值的长度，采用二进制数表示子域取值长度的十进制数。例如，某个域取值占3个字节，那么其子域取值长度表示为“00000011”。所以，若子域取值的长度在1～127字节之间，那么该L字段本身仅占一个字节。
 * b)当L字段最左边字节的最左bit位（即bit8）为1，表示该L字段不止占一个字节，那么它到底占几个字节由该最左字节的后续7个bit位（即bit7～bit1）的十进制取值表示。例如，若最左字节为10000010，表示L字段除该字节外，后面还有两个字节。其后续字节的十进制取值表示子域取值的长度。例如，若L字段为“1000 0001 1111 1111”，表示该子域取值占255个字节。所以，若子域取值的长度在127～255字节之间，那么该L字段本身需占两个字节。
 * 子域取值(V)属性为ASCII，长度根据实际需要填写。
 * 子域取值(V)所支持的数据属性有：
 * ——b   二进制（二进制数或者位组合）。
 * ——cn  BCD码。右对齐，左补‘0’。如，数字12345可以保存在n12的授权金额数据对象中，形如‘00 01 23 45’。
 * ——An	每个字节包含一个字符字母数字型数据元（A-Z，a-z，0-9）。
 * ——var. up to N 	变长数据，最大长度可为N。
 */
public class T2L3TLVUtils {
    private static byte[] filterEmvBytes(byte[] bytes) {
        // 长度校验
        if (bytes == null || bytes.length < 2) {
            return null;
        }
//        // 剔除无效数据
//        int pos = 0;
//        for (int i = 0; i < bytes.length; i++) {
//            if (bytes[i] == 0x00 || (bytes[i] & 0xFF) == 0xFF) {
//                pos = i;
//                //continue;
//            } else {
//                break;
//            }
//        }
//        int bytesLen = bytes.length;
//        int len = bytesLen - pos;
//        if (len <= 2) {
//            return null;
//        }
//        byte[] dest = new byte[len];
//        System.arraycopy(bytes, pos, dest, 0, len);
//        return dest;
        return bytes;
    }

    private static byte[] findLenBytes(byte[] bytes, int start) {
        if (bytes == null || bytes.length < 3) return null;
        int pos = start;
//        if (pos+1>bytes.length) return null;
//            if ((bytes[pos] & 0x80) == 0x80) {
//            }
        for (; pos < bytes.length-1; ) {
            if ((bytes[pos] & 0x80) == 0x80) {
//            if ((bytes[pos] & 0x1F) == 0x1F) {
                pos += 1;
            }else{
                break;
            }
        }
        byte[] dest = new byte[pos + 1 - start];
//        //System.out.println(pos);
        System.arraycopy(bytes, start, dest, 0, pos + 1 - start);
        return dest;
    }

    private static List<T2L3TLV> parseBytes(byte[] bytes) {
//        private static List<T2L3TLV> parseBytes(byte[] bytes, List<T2L3TLV> flatTLVs, int fatherTag, int pos) {
        if (bytes == null || bytes.length < 2) {
            return null;
        }
        int pos=0;
        //todo bug
//        bytes=filterSposBytes(bytes);
//        int bytesLen = bytes.length;
        int tag;
        int len;
        List<T2L3TLV> dest=new ArrayList<T2L3TLV>();
        T2L3TLV tlv ;
        for(;pos<bytes.length-1;) {
            byte[] tagBytes = new byte[2];
        //System.out.println("pos="+pos);
//            int tagLen = tagBytes.length;
            int tagLen = 2;
            System.arraycopy(bytes, pos, tagBytes, 0, tagLen);
            pos += tagLen;
            //System.out.println("+pos="+pos);
//            //System.out.println("pos="+ pos);
            tag = ByteStringHex.bytes2Int(tagBytes);
//            //System.out.println("tagBytes="+ ByteStringHex.bytes2HexStr(tagBytes));
            byte[] lenbytes=findLenBytes(bytes,pos);
            if (lenbytes==null){
                //TODO
                return null;
            }
//            len = bytes[pos];
            len = ByteStringHex.bytes2Int(lenbytes);
            pos +=lenbytes.length;
            //System.out.println("++pos="+pos);
            //System.out.println("++len="+len);
            //处理`length ==0 的情况
            if (len == 0) {
                tlv = new T2L3TLV();
//            //System.out.println("len==0");
                tlv.setValue(null);
                tlv.setTag(tag);
                tlv.setLength(len);
                dest.add(tlv);
            } else {
                tlv = new T2L3TLV();
                byte[] value = new byte[len];
                System.arraycopy(bytes, pos, value, 0, len);
                tlv.setValue(value);
                tlv.setTag(tag);
                tlv.setLength(len);
                dest.add(tlv);

                pos += len;
            }
            //System.out.println("+++pos="+pos);
            if (pos>bytes.length-1) break;
        }
        // //System.out.println("===parse Bytes:"+flatTLVs);
        if(dest.size()<1) return null;
        return dest;
    }

    /**
     * tlv.length 转 bytes
     *
     * @param len tlv.length
     * @return byte[]
     */
    private static byte[] TLVLenth2Bytes(int len) {
        if (len < 0) return null;
        //len 不能超过 3byte
        if (len >= 0xFFFFFF) {
            return null;
        }

        if (len < 0xFF) {
            byte[] lenvalutebytes = ByteStringHex.int2FixBytes(len,1);
            return lenvalutebytes;
        }
        if (len > 0xFF && len<0xFFFF) {
            byte[] lenvalutebytes = ByteStringHex.int2FixBytes(len,2);
            return lenvalutebytes;
        }
        if (len > 0xFFFF && len<0xFFFFFF) {
            byte[] lenvalutebytes = ByteStringHex.int2FixBytes(len,3);
            return lenvalutebytes;
        }
        return null;
    }

//    private static byte[] noValueTLV2Bytes(T2L3TLV tlv, int sonBytesLen) {
//        if (tlv == null || tlv.getTag() < 1) {
////            if (tlv == null || !justSpos(tlv.getTag()) || !justConstructed(tlv.getTag()) || tlv.getValue() != null) {
//            return null;
//        }
//        if (tlv.getValue() == null && !tlv.isConstructed()) {
//            byte[] tagBytes = ByteStringHex.int2BytesN(tlv.getTag());
//            if (tagBytes == null) return null;
//            byte[] dest = new byte[tagBytes.length + 1];
//            System.arraycopy(tagBytes, 0, dest, 0, tagBytes.length);
//            return dest;
//        }
//
////        byte[] lenBytes = TLVLenth2Bytes(tlv.getLength()+sonBytesLen);
//        byte[] lenBytes = TLVLenth2Bytes(sonBytesLen);
//
//        byte[] tagBytes = ByteStringHex.int2BytesN(tlv.getTag());
//        int len;
//        if (lenBytes == null || lenBytes.length == 0) {
//
//            len = tagBytes.length + 1;
//        } else {
//            len = tagBytes.length + lenBytes.length;
//        }
//        byte[] dest = new byte[len];
//        System.arraycopy(tagBytes, 0, dest, 0, tagBytes.length);
//        if (lenBytes != null) {
//            System.arraycopy(lenBytes, 0, dest, tagBytes.length, lenBytes.length);
//        }
////        //System.out.println("~~~ -----noValueTLV2Bytes dest=" + ByteStringHex.bytes2HexStr(dest));
//        return dest;
//    }

    private static byte[] insertBytes2Front(byte[] src, byte[] dest) {
        if (src == null || src.length < 1) {
            return null;
        }
        if (dest == null) {
            return null;
        }
        int srcLen = src.length;
        int destLen = dest.length;
        byte[] tmp = new byte[srcLen + destLen];
        System.arraycopy(dest, 0, tmp, srcLen, destLen);
        System.arraycopy(src, 0, tmp, 0, srcLen);
        return tmp;
    }

    /**
     * tag/value不为空的TLV 转 bytes
     *
     * @param tlv
     * @return byte[]
     */
    private static byte[] TLV2Bytes(T2L3TLV tlv) {
        if (tlv == null || tlv.getTag() == 0) {
            return null;
        }
        if (tlv.getValue() == null || tlv.getValue().length < 1) { //            return null;

            byte[] dest = new byte[3];
            byte[] tagBytes = ByteStringHex.int2FixBytes(tlv.getTag(), 2);
            System.arraycopy(tagBytes, 0, dest, 0, 2);
            return dest;
        }
//        tlv.length 可以为0, 但是 如果不为零，并且不等于value的长度，视为非法
//        if (tlv.getLength() != tlv.getValue().length && tlv.getLength() != 0) {
//            return null;
//        }
        int tlv_l = tlv.getValue().length;
        byte[] lenBytes = TLVLenth2Bytes(tlv_l);
//        byte[] tagBytes = ByteStringHex.int2BytesN(tlv.getTag());
        byte[] tagBytes = ByteStringHex.int2FixBytes(tlv.getTag(), 2);
//        int len = tagBytes.length + lenBytes.length + tlv_l;
        int len = 2 + lenBytes.length + tlv_l;
        tlv.setLength(len);
        byte[] dest = new byte[len];
        System.arraycopy(tagBytes, 0, dest, 0, tagBytes.length);
        System.arraycopy(lenBytes, 0, dest, tagBytes.length, lenBytes.length);
        System.arraycopy(tlv.getValue(), 0, dest, tagBytes.length + lenBytes.length, tlv_l);
        //System.out.println("---TLV2Bytes len:" + dest.length);
        //System.out.println("---TLV2Bytes:" + ByteStringHex.bytes2HexStr(dest));
        return dest;
    }

    private static int getTLVByteLen(T2L3TLV tlv) {
        if (tlv==null||tlv.getTag()==0) return 0;
        int lenLen=1;
        if (tlv.getValue()==null) return 3;
        int valueLen=tlv.getValue().length;
        if (valueLen<0xFFFFFF){lenLen=3;}
        if (valueLen<0xFFFF){lenLen=2;}
        if (valueLen<0xFF){lenLen=1;}
        return 2+lenLen+valueLen;
    }

    public static List<T2L3TLV> bytes2TLVs(byte[] bytes) {
//        //System.out.println("bytes2TLVs bytes="+ByteStringHex.bytes2HexStr(bytes));
        if (bytes == null || bytes.length < 2) {
            return null;
        }
////        bytes = filterEmvBytes(bytes);
//        List<T2L3TLV> flatTLVs = new ArrayList<T2L3TLV>();
//        int fatherTag = 0, pos = 0;
//        flatTLVs = (parseBytes(bytes, flatTLVs, fatherTag, pos));
////        //System.out.println("bytes2TLVs flatTLVS="+flatTLVs.get(0));
        return parseBytes(bytes);
    }


    public static byte[] TLVs2Bytes(List<T2L3TLV> tlvs) {
        if (tlvs == null) {
            return null;
        }
        int size=tlvs.size();
        if (size < 1) {
            return null;
        }
        int totalLen=0;
        //统计所有 tlv 的长度，累加。
        for (int i=0;i<size;i++){
            totalLen+=getTLVByteLen(tlvs.get(i));
        }
        byte[] dest=new byte[totalLen];
        //System.out.println("---totalLen:"+totalLen);
        int pos=0;
        for (int i=0;i<size;i++){
           int tlvlen=getTLVByteLen(tlvs.get(i));
            byte[] tlvb=TLV2Bytes(tlvs.get(i));
//           int tlvlen=tlvb.length;
            //System.out.println("---i:"+i);
            //System.out.println("---pos:"+pos);
            //System.out.println("---tlvlen:"+tlvlen);
            if (tlvb!=null&&tlvlen>1){
                System.arraycopy(tlvb, 0, dest, pos,tlvlen);
                pos +=tlvlen;
            }
            if(pos>totalLen-2) break;
        }
        return dest;
       // return ByteStringHex.ArrayBytes2Bytes(dest);
    }


    /**
     * 查找函数 根据tag int 查找, 无匹配返回null
     *
     * @param tag
     * @param tlvs
     * @return TLVs
     */
    public static List<T2L3TLV> findByTag(int tag, List<T2L3TLV> tlvs) {
        if (tlvs == null || tlvs.size() < 1) {
            return null;
        }
        List<T2L3TLV> items = new ArrayList<T2L3TLV>();
        for (T2L3TLV tlv : tlvs) {
            if (tag == tlv.getTag()) {
                items.add(tlv);
            }
        }
        if (items.size() == 0) {
            return null;
        }
        return items;
    }

    /**
     * 删除函数 根据tag int 查找,
     *
     * @param tag
     * @param flatTLVs 由 byte2FlatTLVs 得到
     * @return TLVs
     */
    private static List<T2L3TLV> removeByTag(int tag, List<T2L3TLV> flatTLVs) {
        if (flatTLVs == null || flatTLVs.size() < 1) {
            return null;
        }
        List<T2L3TLV> items = new ArrayList<T2L3TLV>();
        for (T2L3TLV tlv : flatTLVs) {
            if (tag != tlv.getTag()) {
                items.add(tlv);
            }
        }
        if (items.size() == 0) {
            return null;
        }
        return items;
    }

//
//    /**
//     * 从byte[] 剔除 指定Tag的TLV
//     *
//     * @param tag      int
//     * @param tlvBytes tlv字节流
//     * @return byte[] tlv 字节流
//     */
//    public static byte[] removeTag(int tag, byte[] tlvBytes) {
//        List<T2L3TLV> flatTLVs = bytes2TLVs(tlvBytes);
//        if (flatTLVs == null || flatTLVs.size() < 1) return null;
//        List<T2L3TLV> tmp = removeByTag(tag, flatTLVs);
//        return flatTLVs2Bytes(tmp);
//    }
//
//    public static byte[] removeTag(T2L3TLV emvTLV, byte[] tlvBytes) {
//        if (emvTLV == null) return tlvBytes;
//        return removeTag(emvTLV.getTag(), tlvBytes);
//    }


//    /**
//     * 根据emvTLV的tag查找，修改 bytes的值,如果有重复的tag,所有的value 都会修改。
//     *
//     * @param emvTLV
//     * @param tlvBytes
//     * @return byte[]
//     */
//    public static byte[] modifyByTag(T2L3TLV emvTLV, byte[] tlvBytes) {
//        if (emvTLV == null || emvTLV.getValue() == null) return tlvBytes;
//        if (tlvBytes == null || tlvBytes.length < 2) return null;
//
//        List<T2L3TLV> flatTLVs = bytes2TLVs(tlvBytes);
//        if (flatTLVs == null || flatTLVs.size() < 1) return null;
//        for (T2L3TLV tlv : flatTLVs) {
//            if (emvTLV.getTag() == tlv.getTag()) {
//                tlv.setValue(emvTLV.getValue());
//            }
//        }
//        return flatTLVs2Bytes(flatTLVs);
//    }

}
