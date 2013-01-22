require 'bootstrap'

on :login do |context|
  context.player.get_action_sender.send_message "Welcome to ScapeRune"
end

on :npc_attack do |context|
  npc = context.packet.npc
  context.player.get_action_sender.send_message "Npc Attack [npc=#{npc.get_definition.get_id}]"
end