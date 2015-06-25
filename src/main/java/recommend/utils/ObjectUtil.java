package recommend.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectUtil {
	private final static Log log = LogFactory.getLog(ObjectUtil.class);

	public static Object byteToObject(byte[] bytes) {

		Object obj = null;
		try {
			// byte array to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();

			bi.close();
			oi.close();
		} catch (Exception e) {
			log.error(e);
		}
		return obj;
	}

	public static byte[] objectToByte(Object obj) {
		byte[] bytes = null;
		try {
			// object to byte array
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			log.error(e);
		}
		return bytes;
	}

}
