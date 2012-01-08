package com.jirge.client;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Searches for a new Game.
 *
 * @author Toby Reyelts
 */
public class FindGamePanel extends VerticalPanel {

  TextBox nameTextBox;
  Label statusText;
//okada  GameServiceAsync gameService;
  Main main;
  boolean findingGame;

  FindGamePanel(Main main) {
    this.main = main;
  //okada    gameService = GameService.App.getInstance();

    HorizontalPanel firstRow = new HorizontalPanel();
    add(firstRow);
    Label title = new Label("Buga Jiregee!");
    title.setStyleName("title");
    firstRow.add(title);

    HorizontalPanel secondRow = new HorizontalPanel();
    add(secondRow);
    Label name = new Label("Your Name");
    name.addStyleDependentName("nameLabel");
    secondRow.add(name);
    secondRow.add(nameTextBox = new TextBox());
    Button loginButton = new Button("Join a Game");
    nameTextBox.addKeyPressHandler(new KeyPressHandler() {
      public void onKeyPress(KeyPressEvent event) {
        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
          findGame();
        }
      }
    });
    loginButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        findGame();
      }
    });
    secondRow.add(loginButton);

    HorizontalPanel thirdRow = new HorizontalPanel();
    add(thirdRow);
    thirdRow.add(statusText = new Label());
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        nameTextBox.setFocus(true);          
      }
    });
  }

  private void findGame() {
    findGame(null, 0);
  }

  private void findGame(Long gameId, final int numAttempts) {
    if (findingGame) {
      return;
    }
    findingGame = true;
    statusText.setText("Looking for a game...");
    final String name = nameTextBox.getText();
//okada
//    gameService.login(name, gameId, Main.getNumRounds(), Main.getWaitTime(),
//      new AsyncCallback<LoginResults>() {
//      public void onFailure(Throwable caught) {
//        findingGame = false;
//        Window.alert("Failure: " + caught.getMessage());
//      }
//
//      public void onSuccess(LoginResults results) {
//        findingGame = false;
//        statusText.setText("");
//        Long gameId = results.getGameId();
//        if (gameId != null) {
//          if (numAttempts < 20) {
//            findGame(gameId, numAttempts + 1);
//          } else {
//            statusText.setText("Too many attempts to find a game");
//          }
//        } else {
//          main.loginComplete(name, results);
//        }
//      }
//    });
  }
}
