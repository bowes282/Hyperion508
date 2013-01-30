require 'bootstrap'

# packet = ActionSender
on :login do |c|
  c.packet.send_message "Welcome to ScapeRune"
end