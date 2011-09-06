/**
 *
 * Todo.txt Touch/src/com/todotxt/todotxttouch/remote/DropboxSyncClient.java
 *
 * Copyright (c) 2009-2011 mathias, Gina Trapani, Tormod Haugen, Tim Barlotta
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
 * @author Gina Trapani <ginatrapani[at]gmail[dot]com>
 * @author Tormod Haugen <tormodh[at]gmail[dot]com>
 * @author mathias <mathias[at]x2[dot](none)>
 * @author Tim Barlotta <tim[at]barlotta[dot]net>
 * @license http://www.gnu.org/licenses/gpl.html
 * @copyright 2009-2011 mathias, Gina Trapani, Tormod Haugen, Tim Barlotta
 */
package com.todotxt.todotxttouch.remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

import com.todotxt.todotxttouch.R;
import com.todotxt.todotxttouch.TodoApplication;
import com.todotxt.todotxttouch.util.Util;

class LocalClient implements RemoteClient 
{
    private static final String TODO_TXT_LOCAL_FILE_NAME = "todo.txt";

    private final TodoApplication todoApplication;
    private final SharedPreferences sharedPreferences;

    public LocalClient(TodoApplication todoApplication, SharedPreferences sharedPreferences) 
    {
        this.todoApplication = todoApplication;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Client getClient() { return Client.LOCAL; }

    @Override 
    public boolean authenticate() { return true; }

    @Override 
    public void deauthenticate() { /* NOOP */ }

    @Override 
    public boolean isAuthenticated() { return true; }

    @Override 
    public boolean isLoggedIn() { return true; }

    @Override 
    public boolean isAvailable() 
    { 
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @Override 
    public RemoteLoginTask getLoginTask() 
    {
        return new RemoteLoginTask() {
            @Override public void showLoginDialog(Activity a) {
                todoApplication.sendBroadcast(
                    new Intent("com.todotxt.todotxttouch.ACTION_LOGIN"));
            }
        };
    }

    @Override
    public File pullTodo() 
    {
        try {
            return this.getLocalFile();
        } catch (IOException e) {
            throw new RemoteException("Failed to open file", e);
        }
    }

    @Override
    public void pushTodo(File file) 
    {
        try {
            if (!file.exists()) {
                Util.createParentDirectory(file);
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RemoteException("Failed to ensure that file exists", e);
        }

        try {
            Util.writeFile(new FileInputStream(file), getLocalFile());
        } catch (IOException e) {
            throw new RemoteException("Error writing to file", e);
        }
    }

    private String getLocalPath() 
    {
        return sharedPreferences.getString("todotxtpath", todoApplication
                .getResources().getString(R.string.TODOTXTPATH_defaultPath));
    }

    private File getLocalFile() throws IOException
    {
        File file = new File(Environment.getExternalStorageDirectory(), 
                getLocalPath() + "/" + TODO_TXT_LOCAL_FILE_NAME);

        if (!file.exists()) {
            Util.createParentDirectory(file);
            file.createNewFile();
        }

        return file;
    }
}

