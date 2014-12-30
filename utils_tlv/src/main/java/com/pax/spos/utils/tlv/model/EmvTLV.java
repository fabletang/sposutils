package com.pax.spos.utils.tlv.model;

import com.pax.spos.utils.ByteStringHex;

import java.util.List;

/**
 * Created by fable on 14-9-18.
 */
public class EmvTLV {
    private boolean isConstructed;//是否复合结构
    private int tag;
    private int length;
    private byte[] value;
    private String dataType;
    private int fatherTag;
    private List<EmvTLV> subTLVs;

    public EmvTLV() {
    }

    public EmvTLV(int tag, byte[] value) {
        this.tag = tag;
        this.value = value;
    }

    public EmvTLV(int tag, byte value) {
        this.tag = tag;
        byte[] bs = new byte[1];
        bs[0] = value;
        this.value = bs;
    }

    public EmvTLV(int tag, int length, byte[] value) {
        this.tag = tag;
        this.length = length;
        this.value = value;
    }

    public EmvTLV(String tagHexStr, String valueHexStr) {
        if (tagHexStr != null && valueHexStr != null && tagHexStr.length() != 8 && valueHexStr.length() < 6) {
        } else {
            this.tag = ByteStringHex.hex8Str2int(tagHexStr);
            this.value = ByteStringHex.hexStr2Bytes(valueHexStr);
        }
    }

    @Override
    public String toString() {
        return "EmvTLV{" +
                "isConstructed=" + isConstructed +
                ", tag=" + tag +
                ", tag_Hex=" + Integer.toHexString(tag).toUpperCase() + '\'' +
                ", length=" + length +
                ", value=" + ByteStringHex.bytes2HexStr(value) +
                ", dataType='" + dataType + '\'' +
                ", fatherTag_Hex=" + Integer.toHexString(fatherTag).toUpperCase() + '\'' +
                ", fatherTag=" + fatherTag +
                ", subTLVs=" + subTLVs +
                '}' + '\'';
    }

    public int getFatherTag() {
        return fatherTag;
    }

    public void setFatherTag(int fatherTag) {
        this.fatherTag = fatherTag;
    }

    public List<EmvTLV> getSubTLVs() {
        return subTLVs;
    }

    public void setSubTLVs(List<EmvTLV> subTLVs) {
        this.subTLVs = subTLVs;
    }

    public boolean isConstructed() {
        return isConstructed;
    }

    public void setConstructed(boolean isConstructed) {
        this.isConstructed = isConstructed;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
