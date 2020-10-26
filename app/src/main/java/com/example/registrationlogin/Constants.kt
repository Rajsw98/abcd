package com.example.registrationlogin

object Constants {
    const val DB_NAME="MY_RECORDS_DB"
    const val DB_VERSION=1
    const val TABLE_NAME="MY_RECORDS_TABLE"
    const val C_ID="ID"
    const val C_NAME="NAME"
    const val C_IMAGE="IMAGE"
    const val C_EMAIL="EMAIL"
    const val C_PASSWORD="PASSWORD"
    const val C_ADDED_TIMESTAMP="ADDED_TIME_STAMP"
    const val C_UPDATED_TIMESTAMP="UPDATED_TIME_STAMP"

    const val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("+ C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_NAME + " TEXT,"
            + C_EMAIL + " TEXT,"
            + C_IMAGE + " TEXT,"
            + C_PASSWORD + " TEXT"
            + " TEXT"+ ")"
            )
}