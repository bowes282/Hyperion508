require 'bootstrap'

on :login do |c|
    c.player.get_action_sender.send_message "BLACK FLAG"
end