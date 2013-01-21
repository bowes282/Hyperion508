package org.hyperion.data;

import org.apache.mina.core.buffer.IoBuffer;

public interface Persistable {

    /**
     * Serializes the class into the specified buffer.
     *
     * @param buf The buffer.
     */
    public void serialize(IoBuffer buf);

    /**
     * Deserializes the class from the specified buffer.
     *
     * @param buf The buffer.
     */
    public void deserialize(IoBuffer buf);
}
