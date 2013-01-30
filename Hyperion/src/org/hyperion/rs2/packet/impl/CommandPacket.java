package org.hyperion.rs2.packet.impl;

import org.hyperion.rs2.packet.PacketListener;
import org.hyperion.script.util.Called;

@Called("command")
public class CommandPacket implements PacketListener {

    public String command;
    public String[] args;

    public CommandPacket(String command, String[] args) {
        this.command = command;
        this.args = args;
    }
}
