Hyperion 508
===========
>Hyperion is a Java gameserver suite server which aims to provide an excellent, stable, quality base with advanced features such as update server (to stream the cache to the client) and login server (for multi-world support with cross world messaging).
>The server has been developed completely from scratch, even the ISAAC cipher code is scratch (as the one used by most is copyrighted Jagex - from a deob).
>There are lots of unique ideas: we are spending time making the server good and high quality instead of cramming lots of features in a small amount of time.
>Also, for those working on their own servers, there is documentation on the login, update and in-game protocol, and information about the updating and walking procedures.

About
---
*(pardon my english)*
This is just a side project i'm working on; for anyone who has an interest in the #508 protocol, please check this out.
Currently contains the essential outgoing packets and all incoming packets, e.g.: Commands, Chat, Movement, Walking, PlayerUpdating, etc.
I'm currently designing *(an OO design)* the source to be flexible, organized and custom systems to make it easier adding content like quest systems and minigames.

Scripting API
---
Hyperion #508 has a JRuby Scripting System as seen in [Parabolika's OSGI Server](https://github.com/blackflag7/osgi-server#scripting), just really modified.
You can easily implement different scripting languages if the default one doesn't suite you (JRuby). With the system, simply drop the script files into the directory and it will automatically load up. 

Example: 
~~~
    on :login do |context|
      context.player.get_action_sender.send_message "Welcome to Hyperion #508, #{context.player.getname}!"
      puts "#{context.player.getname} has just logged in"
    end
~~~
