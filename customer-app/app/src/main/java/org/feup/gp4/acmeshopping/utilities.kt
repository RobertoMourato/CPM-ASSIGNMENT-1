package org.feup.gp4.acmeshopping

fun addBackButton(supportActionBar: androidx.appcompat.app.ActionBar?) {
    supportActionBar?.setDisplayHomeAsUpEnabled(true);
    supportActionBar?.setDisplayShowHomeEnabled(true);
}