package edu.ucla.cens.budburst.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

import edu.ucla.cens.budburst.Budburst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public abstract class SyncableRow extends Row {
	public Boolean synced = false;

	
}