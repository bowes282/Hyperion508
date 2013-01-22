package org.hyperion.rs2.packet.impl;

import org.hyperion.rs2.packet.PacketListener;
import org.hyperion.script.util.Called;

@Called("button_click")
public class ButtonClickPacket implements PacketListener {

    public int interfaceId;
    public int button;
    public int child;

    public ButtonClickPacket(int interfaceId, int buttonId, int child) {
        this.interfaceId = interfaceId;
        this.button = buttonId;
        this.child = child;
    }
}