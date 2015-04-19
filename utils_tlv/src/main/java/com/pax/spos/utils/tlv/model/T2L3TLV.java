package com.pax.spos.utils.tlv.model;

import com.pax.spos.utils.ByteStringHex;

import java.util.List;

/**
 * Created by fable on 14-9-18.
 */
public class T2L3TLV {
    private int tag;
    private int length;
    private byte[] value;


    public T2L3TLV() {

    }
    public T2L3TLV(int tag, byte[] value) {
        this.tag = tag;
        this.value = value;
    }

    public T2L3TLV(int tag, byte value) {
        this.tag = tag;
        byte[] bs = new byte[1];
        bs[0] = value;
        this.value = bs;
    }

    public T2L3TLV(int tag, int length, byte[] value) {
        this.tag = tag;
        this.length = length;
        this.value = value;
    }

    public T2L3TLV(String tagHexStr, String valueHexStr) {
        if (tagHexStr != null && valueHexStr != null && tagHexStr.length() != 8 && valueHexStr.length() < 6) {
        } else {
            this.tag = ByteStringHex.hex8Str2int(tagHexStr);
            this.value = ByteStringHex.hexStr2Bytes(valueHexStr);
        }
    }

    @Override
    public String toString() {
        return "T2L3TLV{" +
                ", tag=" + tag +
                ", tag_Hex=" + Integer.toHexString(tag).toUpperCase() + '\'' +
                ", length=" + length +
                ", value=" + ByteStringHex.bytes2HexStr(value) +
                '}' + '\'';
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
}
