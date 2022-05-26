package org.feup.apm.lunchlist4

import android.app.ActionBar

fun addBackButton(supportActionBar: androidx.appcompat.app.ActionBar?) {
    supportActionBar?.setDisplayHomeAsUpEnabled(true);
    supportActionBar?.setDisplayShowHomeEnabled(true);
}