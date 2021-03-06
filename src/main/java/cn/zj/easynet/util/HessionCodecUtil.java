package cn.zj.easynet.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class HessionCodecUtil {

    private static final Logger logger = Logger.getLogger(HessionCodecUtil.class);

    public byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = null;
        HessianOutput output = null;
        try {
            baos = new ByteArrayOutputStream(1024);
            output = new HessianOutput(baos);
            output.startCall();
            output.writeObject(obj);
            output.completeCall();
        } catch (final IOException ex) {
            throw ex;
        } finally {
            if (output != null) {
                try {
                    baos.close();
                } catch (final IOException ex) {
                    logger.error("Failed to close stream.", ex);
                }
            }
        }
        return baos != null ? baos.toByteArray() : null;
    }

    public Object deSerialize(byte[] in) throws IOException {
        Object obj = null;
        ByteArrayInputStream bais = null;
        HessianInput input = null;
        try {
            bais = new ByteArrayInputStream(in);
            input = new HessianInput(bais);
            input.startReply();
            obj = input.readObject();
            input.completeReply();
        } catch (final IOException ex) {
            throw ex;
        } catch (final Throwable e) {
            logger.error("Failed to decode object.", e);
        } finally {
            if (input != null) {
                try {
                    bais.close();
                } catch (final IOException ex) {
                    logger.error("Failed to close stream.", ex);
                }
            }
        }
        return obj;
    }

    public static void main(String[] args) throws Exception {
        Dto1 dto1 = new Dto1("guoj", 23, true);
        byte[] bytes = new HessionCodecUtil().serialize(dto1);
        System.out.println(new String(bytes, "utf-8"));
        System.out.println(new HessionCodecUtil().deSerialize(bytes));
        
        System.out.println((cn.zj.easynet.util.Dto1)new HessionCodecUtil().
        		deSerialize("c Mt cn.zj.easynet.util.Dto1S nameS guojS ageI   S sexTzz".getBytes("utf-8")));

    }

}
