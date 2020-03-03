/*
 * @(#)DataRecordFilter.java 1.00 20/03/03
 *
 * Copyright (C) 2020 Jürgen Reuter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.soundpaint.ml.four_in_a_row;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataRecordFilter implements FileFilter
{
  private static final String RECORD_FILE_NAME_PATTERN = "record_[0-9]*.json";

  private final Pattern namePattern;

  private static final DataRecordFilter defaultInstance =
    new DataRecordFilter();

  public static DataRecordFilter getDefaultInstance()
  {
    return defaultInstance;
  }

  public DataRecordFilter()
  {
    namePattern = Pattern.compile(RECORD_FILE_NAME_PATTERN);
  }

  public boolean accept(final File pathname)
  {
    final String fileName = pathname.getName();
    Matcher matcher = namePattern.matcher(fileName);
    return matcher.matches();
  }
}

/*
 * Local Variables:
 *   coding:utf-8
 *   mode:Java
 * End:
 */
