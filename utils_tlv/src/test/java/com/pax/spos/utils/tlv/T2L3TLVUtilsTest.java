package com.pax.spos.utils.tlv;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.tlv.model.T2L3TLV;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class T2L3TLVUtilsTest {



    @Test
    public void testTLVs2Bytes2() throws Exception {
        T2L3TLV oldMerchantId = new T2L3TLV(0XFF28, "102980058140002".getBytes());
        T2L3TLV payNewPosSer = new T2L3TLV(0X9F5B, "012222233333".getBytes());
        System.out.println("testTLVs2Bytes oldMerchantId=" + oldMerchantId);
        List<T2L3TLV> lists = new ArrayList<T2L3TLV>();
        lists.add(oldMerchantId);
        lists.add(payNewPosSer);
        //byte[] res = T2L3TLVUtils.TLV2Bytes(tlv);
        //tlv对象转 bytes 也是 List<T2L3TLV>
        byte[] res = T2L3TLVUtils.TLVs2Bytes(lists);

        assert (res != null);
        assertEquals((byte) (0x0F), res[2]);
        System.out.println("===========01 testTLVs2Bytes2 res=" + ByteStringHex.bytes2HexStr(res));
        // 查找用使用 bytes2TLVS
        List<T2L3TLV> tlvs = T2L3TLVUtils.bytes2TLVs(res);
        System.out.println("===========02 tlvs.size()=" + tlvs.size());
        System.out.println("===========03 tlvs=" + tlvs);
        //findBytag 返回的是list
        //bytes2TLVs
        List<T2L3TLV> oldMerchantIds = T2L3TLVUtils.findByTag(0XFF28, tlvs);
        System.out.println("testTLVs2Bytes2 oldMerchantId=" + oldMerchantIds.get(0));
        assertEquals((byte) (0x0F), oldMerchantIds.get(0).getLength());
        List<T2L3TLV> payNewPosSers = T2L3TLVUtils.findByTag(0X9F5B, tlvs);
        System.out.println("testTLVs2Bytes2 payNewPosSer=" + payNewPosSers.get(0));
        assertEquals((byte) (0x0C), payNewPosSers.get(0).getLength());
    }
}
