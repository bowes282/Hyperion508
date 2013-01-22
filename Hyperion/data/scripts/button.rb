require 'bootstrap'

on :button_click do |c|
  case c.packet.interfaceId

    # close welcome screen button
  when 378
    case c.packet.button
    when 140
      c.player.get_action_sender.send_game_pane 548
    end

    # logout button
  when 182
    c.player.get_action_sender.logout

    # Equipment tab
  when 387
    case c.packet.button
    when 55
      c.player.get_action_sender.send_interface 667
      c.player.get_bonuses.item_changed
    end


  end
end