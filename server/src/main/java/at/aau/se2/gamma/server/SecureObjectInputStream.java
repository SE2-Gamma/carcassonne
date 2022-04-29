package at.aau.se2.gamma.server;

import java.io.*;

public class SecureObjectInputStream extends ObjectInputStream {

    public SecureObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
        // Only deserialize instances of AllowedClass
        /*if (!osc.getName().equals(AllowedClass.class.getName())) {
            throw new InvalidClassException("Unauthorized deserialization", osc.getName());
        }*/
        return super.resolveClass(osc);
    }
}
