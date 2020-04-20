/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package tools.dynamia.cms.blog

import tools.dynamia.cms.core.api.URIable

import java.time.Month
import java.time.format.TextStyle

class BlogArchive implements URIable {


    private String monthName
    private int year
    private int month
    private long count

    BlogArchive(int year, int month, long count) {
        this.year = year
        this.month = month
        this.count = count
        this.monthName = Month.of(month).getDisplayName(TextStyle.FULL, Locale.getDefault())?.capitalize()
    }

    String getMonthName() {
        return monthName
    }

    int getMonth() {
        return month
    }

    int getYear() {
        return year
    }

    long getCount() {
        return count
    }

    @Override
    String toString() {
        return "$monthName $year"
    }

    @Override
    String toURI() {
        return "$year/$month"
    }
}
