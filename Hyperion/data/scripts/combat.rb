require 'bootstrap'

java_import 'org.hyperion.rs2.action.Action'
java_import 'org.hyperion.rs2.model.EntityCooldowns'
java_import 'org.hyperion.rs2.model.Animation'
java_import 'org.hyperion.rs2.model.Combat'
java_import 'org.hyperion.rs2.model.World'
java_import 'org.hyperion.rs2.model.NPC'
java_import 'org.hyperion.rs2.model.Player'

ATTACK_ANIMATION = Animation.create(422, 1)

class AttackAction < Action

  attr_reader :player, :victim

  def initialize(player, victim)
    super player, 300 
    @victim = victim
    @player = get_player
  end
  
  def getQueuePolicy()
    Action::QueuePolicy::NEVER
  end
  
  def getWalkablePolicy()
    Action::WalkablePolicy::FOLLOW
  end

  def execute
    if Combat.can_attack @player, @victim 
      if !@player.get_entity_cooldowns.get EntityCooldowns::CooldownFlags::MELEE_SWING
        @player.face @victim.get_location
        @player.set_in_combat true
        @player.set_aggressor_state true
        Combat.do_attack @player, @victim, Combat::AttackType::MELEE
        @player.get_entity_cooldowns.flag EntityCooldowns::CooldownFlags::MELEE_SWING, Combat.get_attack_speed(@player), @player 
      end
    else
      stop
    end
  end

end

on :npc_attack do |context|
  npc = context.packet.npc
  player = context.player
  
  if npc != nil and player.get_location.is_within_interaction_distance npc.get_location
    player.get_action_queue.add_action AttackAction.new(player, npc)
  end
  
end

on :player_attack do |context|
  victim = context.packet.target
  player = context.player
  
  if victim != nil and player.get_location.is_within_interaction_distance victim.get_location
    player.get_action_queue.add_action AttackAction.new(player, victim)
  end
  
end