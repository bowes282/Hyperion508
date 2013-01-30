require 'bootstrap'
java_import 'org.hyperion.rs2.model.Item'
java_import 'org.hyperion.rs2.model.Location'
java_import 'org.hyperion.rs2.model.container.Bank'

on :command do |c, game| 
  command = c.packet.command
  args = c.packet.args
  player = c.player
  
  case command
  when "item"
    if (1..3).include? args.length
      id = args[1].to_i
      amount = 1
      
      if args.length == 3
        amount = args[2].to_i
      end
      
      player.get_inventory.add Item.new(id, amount)
    else
      player.get_action_sender.send_message "Syntax is ::item [id] [amount]"
    end
  when "tele"
    if (1..3).include? args.length
      x = args[1].to_i
      y = args[2].to_i
      z = player.get_location.get_z
      
      if args.length == 4
        z = args[3].to_i;
      end 
      
      player.set_teleport_target Location.create(x, y ,z)
    else
      player.getActionSender().sendMessage("Syntax is ::tele [x] [y] [z].");
    end
  when "pos"
    player.get_action_sender.send_message "You are at #{player.get_location}."
  when "bank"
    Bank.open player
  when "debug"
    debug = player.is_debugging ? false : true
    
    player.is_debugging debug
    player.get_action_sender.send_message "Debug Mode : #{debug}"
    # ---
  end
end