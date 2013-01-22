package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.model.ChatMessage;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.task.Task;

import java.util.Queue;

public class PlayerTickTask implements Task {

    private final Player player;

    public PlayerTickTask(Player player) {
        this.player = player;
    }

    @Override
    public void execute(GameEngine context) {
        final Queue<ChatMessage> messages = player.getChatMessageQueue();
        if (messages.size() > 0) {
            player.getUpdateFlags().flag(UpdateFlag.CHAT);
            final ChatMessage message = player.getChatMessageQueue().poll();
            player.setCurrentChatMessage(message);
        } else {
            player.setCurrentChatMessage(null);
        }
        player.getWalkingQueue().processNextMovement();
    }
}
