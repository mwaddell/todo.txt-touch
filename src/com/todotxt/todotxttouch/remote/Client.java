/**
 *
 * Todo.txt Touch/src/com/todotxt/todotxttouch/remote/Client.java
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

/**
 * Holder for Supported Remote Clients
 *
 * @author Tim Barlotta
 * @author Michael J. Waddell <michael[at]waddellnet[dot]com>
 */
public enum Client 
{
    DROPBOX,
    EVERNOTE,
    LOCAL;

    @Override
    public final String toString() 
    {
        return name();
    }

    /** Returns the client with the given name (null if not found). */
    public static final Client fromString(String str)
    {
        Client retVal = null;

        if (str != null) {
            try {
                retVal = Enum.valueOf(Client.class, str.trim());
            } catch (IllegalArgumentException e) {
                /* ignore */
            }
        } 

        return retVal;
    }
}

