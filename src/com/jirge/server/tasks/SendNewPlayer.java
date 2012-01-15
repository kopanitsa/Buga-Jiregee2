package com.jirge.server.tasks;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import com.jirge.server.BugaJiregeeGame;
import com.jirge.server.Player;
import com.jirge.server.PushServer;
import com.jirge.shared.message.NewPlayerMessage;
import com.newatlanta.appengine.taskqueue.Deferred;

/**
* TODO: Doc me.
*
* @author Toby
*/
@SuppressWarnings("serial")
public class SendNewPlayer implements Deferred.Deferrable {
  private Player newPlayer;
  private BugaJiregeeGame game;
  public SendNewPlayer(Player newPlayer, BugaJiregeeGame game) {
    this.newPlayer = newPlayer;
    this.game = game;
  }
  public void doTask() throws ServletException, IOException {
    List<Player> players = new ArrayList<Player>(game.getPlayers());
    players.remove(newPlayer);
    PushServer.sendMessage(players, new NewPlayerMessage(newPlayer.toValue()));
  }
}
