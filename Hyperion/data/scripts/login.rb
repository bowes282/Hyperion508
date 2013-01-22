require 'bootstrap'

on :login do |context|
  context.player.get_action_sender.send_message "Welcome to ScapeRune"
end