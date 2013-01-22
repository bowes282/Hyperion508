require 'java'

modules = {
  'Packets' => 'org.hyperion.rs2.packet',
  'Packet' => 'org.hyperion.rs2.net',
  'Model' => 'org.hyperion.rs2.model',
}

modules.each do |k, v|
  eval <<-RUBY
    module #{k}
      include_package '#{v}'
    end
  RUBY
end

class Context
  attr_reader :player, :packet

  def initialize(player, packet)
    @player, @packet = player, packet
  end
end

@handlers = {}

def on(event_name, &block)
  (@handlers[event_name] ||= []) << block
end

def execute_event(event_name, params)
  context = Context.new *(params.values)
  (@handlers[event_name.to_sym] || []).each { |h| h.call(context) }
end

def load_handlers
  scripts_dir = File.expand_path(File.dirname(__FILE__))
  Dir.foreach(scripts_dir) do |file|
    if file =~ /\.rb$/ && file != File.basename(__FILE__)
      require file
    end
  end
end

load_handlers

