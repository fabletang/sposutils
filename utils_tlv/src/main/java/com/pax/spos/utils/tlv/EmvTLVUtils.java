package com.pax.spos.utils.tlv;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.tlv.model.EmvTLV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fable on 14-9-18.
 */
public class EmvTLVUtils {
    private static byte[] filterEmvBytes(byte[] bytes) {
        // 长度校验
        if (bytes == null || bytes.length < 2) {
            return null;
        }
        // 剔除无效数据
        int pos = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 0x00 || (bytes[i] & 0xFF) == 0xFF) {
                pos = i;
                //continue;
            } else {
                break;
            }
        }
        int bytesLen = bytes.length;
        int len = bytesLen - pos;
        if (len <= 2) {
            return null;
        }
        byte[] dest = new byte[len];
        System.arraycopy(bytes, pos, dest, 0, len);
        return dest;
    }

    public static byte[] findTagBytes (byte[] bytes,int start){
        if (bytes==null||bytes.length<2)return null;
        int pos=start;
        for (;pos<bytes.length;){
           if ((bytes[pos]&0x1F)==0x1F){
               pos+=1;
           }{
                break;
            }
        }
        byte[] dest=new byte[pos+1-start];
//        System.out.println(pos);
        System.arraycopy(bytes,start,dest,0,pos+1-start);
        return dest;
    }
    public static boolean justConstructed(byte bits8) {
        return ((bits8&0x20)==0x20);
    }
    private static List<EmvTLV> parseBytes(byte[] bytes, List<EmvTLV> flatTLVs, int fatherTag, int pos) {
        if (bytes == null || bytes.length < 2) {
            return null;
        }
        if (bytes.length - pos < 2) {
            return flatTLVs;
        }
        //todo bug
//        bytes=filterSposBytes(bytes);
        int bytesLen = bytes.length;
        int tag;
        int len;
//        int lenBytes = 1;// length 对应的bytes2Int
        int lenBytes=1;// length 对应的bytes2Int
        //List<TLV> items = new ArrayList<TLV>();
        // parse tag
        EmvTLV tlv = new EmvTLV();
//        byte[] tagBytes = new byte[4];
        byte[] tagBytes = findTagBytes(bytes,pos);
//        System.out.println("tagBytes="+ByteStringHex.bytes2HexStr(tagBytes));
        if (tagBytes==null||tagBytes.length<1) return flatTLVs;
        int tagLen=tagBytes.length;
        System.arraycopy(bytes, pos, tagBytes, 0, tagLen);
        pos += tagLen;
//            System.out.println("pos="+ pos);
        tag = ByteStringHex.bytes2Int(tagBytes);
//            System.out.println("tagBytes="+ ByteStringHex.bytes2HexStr(tagBytes));

        if ((bytes[pos] & 0x80) == 0x80) {
//            lenBytes = (bytes[pos] & 0x7F);
            lenBytes = (bytes[pos] & 0x0F);
//            System.out.println("lenBytes"+ lenBytes);
            //假定length最多占用5个byte
            if (lenBytes > 4) {
                return flatTLVs;
            }
            pos +=1;
            byte[] lenValue = new byte[lenBytes];
            System.arraycopy(bytes,pos,lenValue,0,lenBytes);

//            System.out.println("lenBytes"+ ByteStringHex.bytes2HexStr(lenValue));
            len = ByteStringHex.bytes2Int(lenValue);
//            System.out.println("len"+ len);
        } else {
            len = bytes[pos];
        }
        //处理`length ==0 的情况
        if (len==0){
            pos +=1;
//            System.out.println("len==0");
            tlv.setValue(null);
            tlv.setTag(tag);
            tlv.setLength(len);
            boolean isConstructed=justConstructed(tagBytes[0]);
            tlv.setConstructed(isConstructed);
            tlv.setFatherTag(fatherTag);
            if (tlv.isConstructed()) {
                fatherTag = tlv.getTag();
            }
            flatTLVs.add(tlv);
            if (bytesLen - pos >= 2) {
                flatTLVs = (parseBytes(bytes, flatTLVs, fatherTag, pos));
            }
        }else {
            byte[] value = new byte[len];
//            System.arraycopy(bytes, pos + 1, value, 0, len);
//            System.out.println("===pos="+pos);
//            System.out.println("===len="+len);
            if (len+pos+1>bytes.length){
             //   System.arraycopy(bytes, pos + 1, value, 0, bytes.length-pos-1);
            }else {
                System.arraycopy(bytes, pos + 1, value, 0, len);
            }
            pos += lenBytes;
//            pos = i;
            tlv.setValue(value);
            tlv.setTag(tag);
            tlv.setLength(len);
            boolean isConstructed=justConstructed(tagBytes[0]);
            tlv.setConstructed(isConstructed);
            tlv.setFatherTag(fatherTag);
//        tlv = processTag(tlv);
//        System.out.println("---tlv="+tlv);
//        System.out.println("---tlv.tag="+ByteStringHex.int2HexStr(tag));
//        System.out.println("---tlv.isConstructed="+tlv.isConstructed());
//        System.out.println("---pos="+pos);
            //todo
            if (tlv.isConstructed()) {
                flatTLVs.add(tlv);
                fatherTag = tlv.getTag();
//            System.out.println("======parse tlv.tag:" + tag);
            } else {
                flatTLVs.add(tlv);
                pos += tlv.getValue().length;
            }
            if (bytesLen - pos >= 2) {
//            System.out.println("---parse Bytes:" + flatTLVs);
//            System.out.println("---parse fatherTag:" + ByteStringHex.int2HexStr(fatherTag));

//            System.out.println("----------------------byteLen=" + bytesLen);
//            System.out.println("----------------------    pos=" + pos);
                flatTLVs = (parseBytes(bytes, flatTLVs, fatherTag, pos));
            }
        }
        // System.out.println("===parse Bytes:"+flatTLVs);
        return flatTLVs;
    }

    private static List<EmvTLV> TLV2FlatTLVs(EmvTLV tlv) {

        List<EmvTLV> flatTLVs = new ArrayList<EmvTLV>();
        if (tlv == null || tlv.getTag() == 0) {
//            return flatTLVs;
            return null;
        }
        flatTLVs.add(tlv);
        List<EmvTLV> subTLVs = tlv.getSubTLVs();
        if (subTLVs == null) return flatTLVs;
        for (EmvTLV tlv1 : subTLVs) {
            if (tlv1!=null&&tlv.getTag()>0) {
                flatTLVs.addAll(TLV2FlatTLVs(tlv1));
            }
        }
        return flatTLVs;
    }

    private static List<EmvTLV> TLVs2FlatTLVs(List<EmvTLV> TLVs) {
        if (TLVs == null || TLVs.size() < 1) {
            return null;
        }
        List<EmvTLV> dest = new ArrayList<EmvTLV>();
        for (EmvTLV tlv : TLVs) {
//            TLV2FlatTLVs2(tlv, dest);
            if (tlv!=null&&tlv.getTag()>0) {
                dest.addAll(TLV2FlatTLVs(tlv));
            }
        }
        return dest;
    }
    private static EmvTLV getFatherTLV(int fatherTag, List<EmvTLV> TLVs) {
        if (fatherTag<1) {
            return null;
        }
        if (TLVs == null || TLVs.size() < 1) {
            return null;
        }
        List<EmvTLV> items = new ArrayList<EmvTLV>();

        for (EmvTLV tlv : TLVs) {
            if (fatherTag == tlv.getTag()) {
                items.add(tlv);
            }
        }
        if (items.size() < 1) return null;
        return items.get(items.size() - 1);
    }

    /**
     * tlv.length 转 bytes
     *
     * @param len tlv.length
     * @return byte[]
     */
    private static byte[] TLVLenth2Bytes(int len) {
        if (len < 0) return null;
        //len 不能超过 0x7FFFFFFF, 即2048M, 也不能为0
        if (len >= 0x7FFFFFFF || len == 0) {
            return null;
        }
        if (len > 0x7F) {
            byte[] lenValue = ByteStringHex.int2BytesN(len);
            byte[] dest = new byte[1 + lenValue.length];
            byte[] lenvalutebytes = ByteStringHex.int2BytesN(lenValue.length);

            dest[0] = (byte) (lenvalutebytes[0] | 0x80);
            System.arraycopy(lenValue, 0, dest, 1, lenValue.length);
            return dest;
        } else {
            byte[] dest = new byte[1];
            dest[0] = (byte) (len & 0x7F);
            return dest;
//            return ByteStringHex.int2BytesN(len);
        }
    }
    private static byte[] noValueTLV2Bytes(EmvTLV tlv, int sonBytesLen) {
        if (tlv == null|| tlv.getTag()<1 ) {
//            if (tlv == null || !justSpos(tlv.getTag()) || !justConstructed(tlv.getTag()) || tlv.getValue() != null) {
            return null;
        }
        if (tlv.getValue()==null&&!tlv.isConstructed()){
            byte[] tagBytes=ByteStringHex.int2BytesN(tlv.getTag());
            if (tagBytes==null)return null;
            byte[] dest=new byte[tagBytes.length+1];
            System.arraycopy(tagBytes,0,dest,0,tagBytes.length);
            return dest;
        }

//        byte[] lenBytes = TLVLenth2Bytes(tlv.getLength()+sonBytesLen);
        byte[] lenBytes = TLVLenth2Bytes(sonBytesLen);

        byte[] tagBytes = ByteStringHex.int2BytesN(tlv.getTag());
        int len;
        if (lenBytes == null || lenBytes.length == 0) {

            len = tagBytes.length + 1;
        } else {
            len = tagBytes.length + lenBytes.length;
        }
        byte[] dest = new byte[len];
        System.arraycopy(tagBytes, 0, dest, 0, tagBytes.length);
        if (lenBytes != null) {
            System.arraycopy(lenBytes, 0, dest, tagBytes.length, lenBytes.length);
        }
//        System.out.println("~~~ -----noValueTLV2Bytes dest=" + ByteStringHex.bytes2HexStr(dest));
        return dest;
    }

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
    private static byte[] hasValueTLV2Bytes(EmvTLV tlv) {
        if (tlv == null || tlv.getTag()<0) {
            return null;
        }
        if (tlv.getValue() == null || tlv.getValue().length < 1) { //            return null;

            byte[] dest = new byte[5];
            byte[] tagBytes=ByteStringHex.int2FixBytes(tlv.getTag(),4);
            System.arraycopy(tagBytes,0,dest,0,4);
            return dest;
        }
//        tlv.length 可以为0, 但是 如果不为零，并且不等于value的长度，视为非法
//        if (tlv.getLength() != tlv.getValue().length && tlv.getLength() != 0) {
//            return null;
//        }
        int tlv_l = tlv.getValue().length;
        byte[] lenBytes = TLVLenth2Bytes(tlv_l);
        byte[] tagBytes = ByteStringHex.int2BytesN(tlv.getTag());
        int len = tagBytes.length + lenBytes.length + tlv_l;
        tlv.setLength(len);
        byte[] dest = new byte[len];
        System.arraycopy(tagBytes, 0, dest, 0, tagBytes.length);
        System.arraycopy(lenBytes, 0, dest, tagBytes.length, lenBytes.length);
        System.arraycopy(tlv.getValue(), 0, dest, tagBytes.length + lenBytes.length, tlv_l);
        return dest;
    }
    private static ArrayList<Byte> insertBytes2ArrayFront(byte[] src, ArrayList<Byte> dest) {
        if (src == null || src.length < 1) {
            return dest;
        }
        if (dest == null) {
//            return null;
            dest = new ArrayList<Byte>();
        }
        ArrayList<Byte> srcArray = ByteStringHex.Bytes2ArrayBytes(src);
        ArrayList<Byte> tmp = new ArrayList<Byte>();
        for (Byte b : srcArray) {
            tmp.add(b);
        }
        for (Byte b : dest) {
            tmp.add(b);
        }
//        tmp.addAll(srcArray);
//        tmp.addAll(dest);
//        System.out.println("insertBystes2Array front. tmp=" + ByteStringHex.bytes2HexStr(ByteStringHex.ArrayBytes2Bytes(tmp)));
        return tmp;
    }
    //Collections.sort(items,new TLVComparator());
    private static ArrayList<Byte> parseTLVs(List<EmvTLV> flatTLVs, ArrayList<Byte> bytes, int sonsLen) {
        if (flatTLVs == null || flatTLVs.size() < 1) {
            return bytes;
        }
        int len = flatTLVs.size();
        EmvTLV tlv = flatTLVs.get(len - 1);
        if (tlv==null||tlv.getTag()==0){
            flatTLVs.remove(tlv);
            bytes = parseTLVs(flatTLVs, bytes, sonsLen);
        }
//        System.out.println("-----parseTLVs. tlv =" + ByteStringHex.int2HexStr(tlv.getTag()));
//        if (tlv.getFatherTag()!=0){
        // if (tlv.getSubTLVs()!=null){tlv.setValue(null);}
        if (tlv.isConstructed()) {
            tlv.setValue(null);
        }

        if (tlv.getValue() == null || tlv.isConstructed()) {
//            System.out.println ("===============================");
            if (tlv.isConstructed()) {
            }
            if (tlv.getValue() == null) {

                sonsLen += 0;

            } else {
                sonsLen += tlv.getValue().length;
            }
            EmvTLV fatherTLV = getFatherTLV(tlv.getTag(), flatTLVs);
            int len2 = fatherTLV.getLength() + sonsLen;
//            int len2=fatherTLV.getLength()+ TLVLenth2Bytes(fatherTLV.getLength()).length+4;
//            int len2=TLVLenth2Bytes(fatherTLV.getLength()).length+4;
            fatherTLV.setLength(len2);
            int pos = flatTLVs.indexOf(fatherTLV);

            flatTLVs.set(pos, fatherTLV);

            byte[] addBytes = noValueTLV2Bytes(tlv, sonsLen);
            flatTLVs.remove(len - 1);
            if (addBytes != null && addBytes.length > 1) {
                bytes = insertBytes2ArrayFront(addBytes, bytes);
                sonsLen += addBytes.length;
//                System.out.println("======addbytes="+ByteStringHex.bytes2HexStr(addBytes));
            }
            if (flatTLVs.size() == 0) {
                return bytes;
            }
//            System.out.println("parseTLVs.10 bytes =" + ByteStringHex.bytes2HexStr(ByteStringHex.ArrayBytes2Bytes(bytes)));
        }
        if (tlv.getValue() != null) {
            if (tlv.isConstructed()) {
                //  sonsLen+=tlv.getValue().length;
            }
            byte[] addBytes = hasValueTLV2Bytes(tlv);
            if (addBytes != null && addBytes.length > 1) {
                bytes = insertBytes2ArrayFront(addBytes, bytes);
            }
            flatTLVs.remove(len - 1);
            sonsLen += addBytes.length;
            if (tlv.isConstructed()) {
//                    sonsLen+=tlv.getValue().length;
                EmvTLV fatherTLV = getFatherTLV(tlv.getTag(), flatTLVs);
//                int len2=fatherTLV.getLength()+sonsLen;
                int len2 = fatherTLV.getLength() + addBytes.length;
                fatherTLV.setLength(len2);
//                System.out.println("-------len2="+len2);
                int pos = flatTLVs.indexOf(fatherTLV);

                flatTLVs.set(pos, fatherTLV);

            }
        }
        if (flatTLVs.size() == 0) {
            return bytes;
        }
        if (flatTLVs.size() > 0) {
            bytes = parseTLVs(flatTLVs, bytes, sonsLen);
        }
//        System.out.println("parseTLVs.11 bytes =" + ByteStringHex.bytes2HexStr(ByteStringHex.ArrayBytes2Bytes(bytes)));
        return bytes;
//        System.out.println("parseTLVs. tlv.getvalue="+ tlv);
    }

    public static List<EmvTLV> bytes2FlatTLVs(byte[] bytes) {
//        System.out.println("bytes2FlatTLVs bytes="+ByteStringHex.bytes2HexStr(bytes));
        if (bytes == null || bytes.length < 2) {
            return null;
        }
//        bytes = filterEmvBytes(bytes);
        List<EmvTLV> flatTLVs = new ArrayList<EmvTLV>();
        int fatherTag = 0, pos = 0;
        flatTLVs = (parseBytes(bytes, flatTLVs, fatherTag, pos));
//        System.out.println("bytes2FlatTLVs flatTLVS="+flatTLVs.get(0));
        return flatTLVs;
    }

    /**
     * TLV嵌套工具， 用于添加 tlv.subTLVs, 可以智能处理 tlv.tag(变0xC为0xE)
     * @param srcTLV   son TLV
     * @param destTLV father TLV
     */
    public static void addSubTLV(EmvTLV srcTLV, EmvTLV destTLV) {
        if (srcTLV == null || destTLV == null) return;

        List<EmvTLV> fatherSubTLVs = destTLV.getSubTLVs();
        srcTLV.setFatherTag(destTLV.getTag());
        if (fatherSubTLVs == null) {
            fatherSubTLVs = new ArrayList<EmvTLV>();
            fatherSubTLVs.add(srcTLV);
            destTLV.setSubTLVs(fatherSubTLVs);
        } else {
//            if (fatherSubTLVs.contains(srcTLV)) return;
            fatherSubTLVs.add(srcTLV);
            destTLV.setSubTLVs(fatherSubTLVs);
        }
//        destTLV.setValue(null);
        destTLV.setConstructed(true);
        //todo 时间不够 。。。
//        if (destTLV.getTag() != 0) {
//            byte[] tagBytes = ByteStringHex.int2Bytes(destTLV.getTag());
//            byte hi4bit = (byte) (0xF0 & tagBytes[0]);
//            if (hi4bit == (byte) 0xC0) {
//                tagBytes[0] = (byte) (0x0F & tagBytes[0]);
//                tagBytes[0] = (byte) (0xE0 | tagBytes[0]);
//                destTLV.setTag(ByteStringHex.bytes2Int(tagBytes));
//            }
//        }
    }
    private static List<EmvTLV> addSubTLVs(EmvTLV srcTLV, List<EmvTLV> destTLVs) {
        if (srcTLV == null || destTLVs == null) return null;
        int tag, fatherTag;
        tag = srcTLV.getTag();
        fatherTag = srcTLV.getFatherTag();
        if (fatherTag == 0) {
            return destTLVs;
        }
        EmvTLV fatherTLV = getFatherTLV(fatherTag, destTLVs);
        if (fatherTLV == null) {
            return destTLVs;
        }

        List<EmvTLV> fatherSubTLVs = fatherTLV.getSubTLVs();
        if (fatherSubTLVs == null) {
            fatherSubTLVs = new ArrayList<EmvTLV>();
            fatherSubTLVs.add(0, srcTLV);
        } else {
            if (fatherSubTLVs.contains(srcTLV)) return destTLVs;
            fatherSubTLVs.add(0, srcTLV);
        }
        fatherTLV.setSubTLVs(fatherSubTLVs);
        int index = destTLVs.indexOf(fatherTLV);
        destTLVs.set(index, fatherTLV);
        return destTLVs;
    }

    private static List<EmvTLV> makeflatTLVsNested(List<EmvTLV> nestedTLVs, List<EmvTLV> flatTLVs) {
        if (flatTLVs == null || flatTLVs.size() < 1) {
            return null;
        }
        int pos = flatTLVs.size();
        pos -= 1;
        EmvTLV tlv = flatTLVs.get(pos);
        if (tlv.getFatherTag() != 0) {
            addSubTLVs(tlv, nestedTLVs);
            flatTLVs.remove(pos);
            makeflatTLVsNested(nestedTLVs, flatTLVs);
        } else {
            //todo
            return nestedTLVs;
        }
        return nestedTLVs;
    }

    private static List<EmvTLV> flatTLVs2NestedTLVs(List<EmvTLV> flatTLVs) {
        if (flatTLVs == null) return null;
        List<EmvTLV> dest = new ArrayList<EmvTLV>();
        for (EmvTLV tlv : flatTLVs) {
            dest.add(tlv);
        }
        makeflatTLVsNested(dest, flatTLVs);
        return dest;
    }
    /**
     * bytes 转为嵌套的tlv, 包含多叉树结构的的所有节点TLV对象
     *
     * @param bytes
     * @return 多叉树结构的的所有节点TLV对象
     */
    public static List<EmvTLV> bytes2NestedFlatTLVs(byte[] bytes) {
        List<EmvTLV> flatTLVs = bytes2FlatTLVs(bytes);
        if (flatTLVs == null || flatTLVs.size() == 1) return flatTLVs;
        return flatTLVs2NestedTLVs(flatTLVs);
    }
    private static List<EmvTLV> getTLVsNoFather(List<EmvTLV> flatTLVs) {
        if (flatTLVs == null || flatTLVs.size() < 1) {
            return null;
        }
        List<EmvTLV> dest = new ArrayList<EmvTLV>();
        for (EmvTLV tlv : flatTLVs) {
            if (tlv.getFatherTag() == 0 && tlv.getTag()>0) {
                dest.add(tlv);
            }
        }
        if (dest.size() < 1) {
            return null;
        }
        return dest;
    }
    /**
     * bytes 转为嵌套的tlv, 不包含多叉树结构的的子节点TLV对象
     *
     * @param bytes
     * @return 不包含多叉树结构的的子节点TLV对象
     */
    public static List<EmvTLV> bytes2TopNestedTLVs(byte[] bytes) {
        List<EmvTLV> TLVs = bytes2NestedFlatTLVs(bytes);
        if (TLVs == null || TLVs.size() == 1) return TLVs;
        return getTLVsNoFather(TLVs);
    }


//    private static List<EmvTLV> TLVs2FlatTLVs(List<EmvTLV> TLVs) {
//        if (TLVs == null || TLVs.size() < 1) {
//            return null;
//        }
//        List<EmvTLV> dest = new ArrayList<EmvTLV>();
//        for (EmvTLV tlv : TLVs) {
//            if (tlv!=null&&tlv.getTag()>0) {
//                dest.addAll(TLV2FlatTLVs(tlv));
//            }
//        }
//        return dest;
//    }

    public static byte[] TLVs2Bytes(List<EmvTLV> nestedTLVs) {
        if (nestedTLVs == null || nestedTLVs.size() < 1) {
            return null;
        }
        List<EmvTLV> flatTLVs = new ArrayList<EmvTLV>();
        flatTLVs = TLVs2FlatTLVs(nestedTLVs);
//        System.out.println("---flatTLVs size"+flatTLVs.size());
        ArrayList<Byte> dest = new ArrayList<Byte>();
        int sonsLen = 0;
        dest = parseTLVs(flatTLVs, dest, sonsLen);
        return ByteStringHex.ArrayBytes2Bytes(dest);
    }

    public static byte[] TLV2Bytes(EmvTLV nestedTLV) {
        List<EmvTLV> flatTLVs = new ArrayList<EmvTLV>();
        flatTLVs = TLV2FlatTLVs(nestedTLV);
//        System.out.println("--TLV2Bytes flatTLVs size=" + flatTLVs.size());
        if (flatTLVs == null || flatTLVs.size() < 1) {
            return null;
        }
        //byte[] dest = new byte[0];
        ArrayList<Byte> dest = new ArrayList<Byte>();
//        byte[] dest = new byte[0];
        int sonsLen = 0;
        dest = parseTLVs(flatTLVs, dest, sonsLen);
//        parseTLVs(flatTLVs, dest, sonsLen);
        return ByteStringHex.ArrayBytes2Bytes(dest);
    }

    /**
     * 查找函数 根据tag int 查找, 无匹配返回null
     *
     * @param tag
     * @param flatTLVs
     * @return TLVs
     */
    public static List<EmvTLV> findByTag(int tag, List<EmvTLV> flatTLVs) {
        if (flatTLVs == null || flatTLVs.size() < 1) {
            return null;
        }
        List<EmvTLV> items = new ArrayList<EmvTLV>();
        for (EmvTLV tlv : flatTLVs) {
            if (tag == tlv.getTag()) {
                items.add(tlv);
            }
        }
        if (items.size()==0){return null;}
        return items;
    }

    /**
     * 删除函数 根据tag int 查找,
     *
     * @param tag
     * @param flatTLVs 由 byte2FlatTLVs 得到
     * @return TLVs
     */
    private static List<EmvTLV> removeByTag(int tag, List<EmvTLV> flatTLVs) {
        if (flatTLVs == null || flatTLVs.size() < 1) {
            return null;
        }
        List<EmvTLV> items = new ArrayList<EmvTLV>();
        for (EmvTLV tlv : flatTLVs) {
            if (tag != tlv.getTag()) {
                items.add(tlv);
            }
        }
        if (items.size()==0){return null;}
        return items;
    }
    private static byte[] flatTLVs2Bytes(List<EmvTLV> flatTLVs) {
//        System.out.println("--TLV2Bytes flatTLVs size=" + flatTLVs.size());
        if (flatTLVs == null || flatTLVs.size() < 1) {
            return null;
        }
        //byte[] dest = new byte[0];
        ArrayList<Byte> dest = new ArrayList<Byte>();
//        byte[] dest = new byte[0];
        int sonsLen = 0;
        dest = parseTLVs(flatTLVs, dest, sonsLen);
//        parseTLVs(flatTLVs, dest, sonsLen);
        return ByteStringHex.ArrayBytes2Bytes(dest);
    }

    /**
     * 从byte[] 剔除 指定Tag的TLV
     * @param tag int
     * @param tlvBytes tlv字节流
     * @return byte[] tlv 字节流
     */
    public static byte[] removeTag(int tag,byte[] tlvBytes) {
        List<EmvTLV> flatTLVs=bytes2FlatTLVs(tlvBytes);
        if (flatTLVs==null||flatTLVs.size()<1)return null;
        List<EmvTLV> tmp= removeByTag(tag,flatTLVs);
        return flatTLVs2Bytes(tmp);
    }
}
