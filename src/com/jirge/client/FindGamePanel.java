package com.jirge.client;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.jirge.shared.LoginResults;

public class FindGamePanel extends VerticalPanel {

  private final Logger mLogger = Logger.getLogger(this.getClass().getName());
  TextBox mNameTextBox;
  Label mStatusText;
  GameServiceAsync mGameService;
  Main mMain;
  boolean mFindingGame;

  FindGamePanel(Main main) {
    mMain = main;
    mGameService = GameService.App.getInstance();

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
    secondRow.add(mNameTextBox = new TextBox());
    Button loginButton = new Button("Join a Game");
    mNameTextBox.addKeyPressHandler(new KeyPressHandler() {
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
    thirdRow.add(mStatusText = new Label());
  }

  private void findGame() {
    findGame(null, 0);
  }

  private void findGame(Long gameId, final int numAttempts) {
    mLogger.warning("findGame gameId:"+gameId);
    if (mFindingGame) {
      return;
    }
    mFindingGame = true;
    mStatusText.setText("Looking for a game...");
    final String name = mNameTextBox.getText();

    mGameService.login(name, gameId, new AsyncCallback<LoginResults>() {
      public void onFailure(Throwable caught) {
        mFindingGame = false;
        Window.alert("Failure: " + caught.getMessage());
      }

      public void onSuccess(LoginResults results) {
        mLogger.info("FindGamePanel#onSuccess(login)");
        mFindingGame = false;
        mStatusText.setText("");
        Long gameId = results.getGameId();
        if (gameId != null) {
          if (numAttempts < 20) {
            findGame(gameId, numAttempts + 1);
          } else {
            mStatusText.setText("Too many attempts to find a game");
          }
        } else {
          mMain.loginComplete(name, results);
        }
      }
    });
  }
}
