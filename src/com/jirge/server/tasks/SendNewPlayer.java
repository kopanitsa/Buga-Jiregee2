package com.jirge.server.tasks;


import javax.servlet.ServletException;

import com.jirge.server.Game;
import com.jirge.server.Player;
import com.jirge.server.PushServer;
import com.jirge.shared.message.NewPlayerMessage;
import com.newatlanta.appengine.taskqueue.Deferred;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* TODO: Doc me.
*
* @author Toby
*/
@SuppressWarnings("serial")
public class SendNewPlayer implements Deferred.Deferrable {
  private Player newPlayer;
  private Game game;
  public SendNewPlayer(Player newPlayer, Game game) {
    this.newPlayer = newPlayer;
    this.game = game;
  }
  public void doTask() throws ServletException, IOException {
    List<Player> players = new ArrayList<Player>(game.getPlayers());
    players.remove(newPlayer);
    PushServer.sendMessage(players, new NewPlayerMessage(newPlayer.toValue()));
  }
}
