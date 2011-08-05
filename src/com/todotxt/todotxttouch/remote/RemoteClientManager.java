/**
 *
 * Todo.txt Touch/src/com/todotxt/todotxttouch/remote/RemoteClientManager.java
 *
 * Copyright (c) 2011 Tim Barlotta
 *
 * LICENSE:
 *
 * This file is part of Todo.txt Touch, an Android app for managing your todo.txt file (http://todotxt.com).
 *
 * Todo.txt Touch is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any
 * later version.
 *
 * Todo.txt Touch is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with Todo.txt Touch.  If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * @author Tim Barlotta <tim[at]barlotta[dot]net>
 * @license http://www.gnu.org/licenses/gpl.html
 * @copyright 2011 Tim Barlotta
 */

package com.todotxt.todotxttouch.remote;

import android.util.Log;
import android.content.SharedPreferences;

import com.todotxt.todotxttouch.TodoApplication;

/**
 * Manager for obtaining, switching, etc. remote clients
 * 
 * @author Tim Barlotta
 * @author Michael J. Waddell <michael[at]waddellnet[dot]com>
 */
public class RemoteClientManager implements
		SharedPreferences.OnSharedPreferenceChangeListener 
{
	private final static String TAG = RemoteClientManager.class.getSimpleName();

	private Client currentClientToken;
	private RemoteClient currentClient;
	private TodoApplication todoApplication;
	private SharedPreferences sharedPreferences;

	public static final RemoteClient DUMMY_CLIENT = new RemoteClient() {
		@Override public Client getClient() { return null; }
		@Override public boolean authenticate() { return false; }
		@Override public void deauthenticate() { /* NOOP */ }
		@Override public boolean isAuthenticated() { return false; }
		@Override public boolean isLoggedIn() { return false; }
		@Override public RemoteLoginTask getLoginTask() { return null; }
		@Override public File pullTodo() { return null; }
		@Override public void pushTodo(File f){ /* NOOP */ }
		@Override public boolean isAvailable() { return false; }
	};

	public RemoteClientManager(TodoApplication todoApplication,
			SharedPreferences sharedPreferences) {
		this.todoApplication = todoApplication;
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		this.sharedPreferences = sharedPreferences;
		calculateRemoteClient(sharedPreferences);
		currentClient.authenticate();
	}

	public RemoteClient getRemoteClient() {
		return currentClient;
	}

	/**
	 * Returns the client associated with the passed in token does not switch
	 * the client
	 * 
	 * @param clientToken
	 * @return
	 */
	private RemoteClient getRemoteClient(Client clientToken) 
	{
		if (clientToken == Client.DROPBOX) {
			Log.v(TAG, "Creating new DROPBOX client");
			return new DropboxRemoteClient(todoApplication, sharedPreferences);
		}

		if (clientToken == Client.EVERNOTE) {
			Log.v(TAG, "Creating new EVERNOTE client");
			throw new UnsupportedOperationException();
			// return new EvernoteRemoteClient(todoApplication, sharedPreferences);
		}

		if (clientToken == Client.LOCAL) {
			Log.v(TAG, "Creating new LOCAL client");
			throw new UnsupportedOperationException();
			// return new LocalClient(todoApplication, sharedPreferences);
		}
		
		return DUMMY_CLIENT;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) 
	{
		if (Constants.PREF_CLIENT_TOKEN.equals(key)) {
			Log.i(TAG, "New Client Token. Syncing!");

			this.sharedPreferences = sharedPreferences;
			calculateRemoteClient(sharedPreferences);
		} // else ignore
	}

	private void calculateRemoteClient(SharedPreferences sharedPreferences) {
		currentClientToken = Client.fromString(sharedPreferences.getString(Constants.PREF_CLIENT_TOKEN, null));
		currentClient = getRemoteClient(currentClientToken);
	}
}
