package com.jirge.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;
import com.jirge.shared.LoginResults;
import com.jirge.shared.message.Message;

@RemoteServiceRelativePath("game_service")
public interface GameService extends RemoteService {

	/**
	 * Utility/Convenience class. Use GameService.App.getInstance() to access
	 * static instance of GameServiceAsync
	 */
	public static class App {

		private static final GameServiceAsync ourInstance = (GameServiceAsync) GWT
				.create(GameService.class);

		public static GameServiceAsync getInstance() {
			return ourInstance;
		}
	}

	/**
	 * Logs a user in with the given name. Returns a handle used to receive push
	 * notifications from the server.
	 * 
	 * @param name
	 *            The handle by which the user will be known.
	 * @return data structure containing information required by the client.
	 */
	LoginResults login(String name, Long gameId);

	/**
	 * Confirms a successfully opened channel for the player in this session.
	 * 
	 * @return a list of {@code Messages} that should catch the player up to the
	 *         current game state.
	 */
	List<Message> confirmLogin();

	/**
	 * Get the list of accessible point of selected piece.
	 * 
	 * @return a list of indexes of board that the selected piece can move to.
	 */
	int[] getAccessiblePoints(int index);

	/**
	 * Move the selected piece to the target position.
	 * 
	 * @return success or fail
	 */
	boolean movePiece(int fromIndex, int toIndex);

}
