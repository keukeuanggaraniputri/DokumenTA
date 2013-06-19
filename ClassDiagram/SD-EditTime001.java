//all process
Logika Proses
var
begin
	
end

//per modul
14
Logika Proses
Procedure updateTime(String messageId, long timesent, long timemilis)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
begin
	databaseWriteable.rawQuery("update time set time_timemillis='timemillis' where time_message_id='messageId'");
	databaseWriteable.rawQuery("update time set time_timesent='timesent' where time_message_id='messageId'");
end

16
Logika Proses
function getTime(String messageId) : Cursor
var
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
cursor : Cursor, //sebagai penampung dari data yang berhasil diambil dari database
begin
	cursor <- databaseReadable.rawQuery("select * from table time where time_message_id='messageId'");
	return cursor;
end

18
Logika Proses
function count(Cursor cursor) : int
var
count : int, //sebagai penampung berapa banyak row record yang ada pada cursor
begin
	count <- cursor.getCount();
	return count;
end

20
Logika Proses
Procedure deleteTime(String messageId)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
begin
	databaseWriteable.rawQuery("delete from time where time_message_id='messageId'");
end

21
Logika Proses
function getMessageFrequency(String messageId) : String
var
frequency : String, //sebagai penampung data frequency hasil fetch dari database
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
cursor : Cursor, //sebagai penampung dari data yang berhasil diambil dari database
begin
	cursor <- databaseReadable.rawQuery("select message_frequency from table message where message_id='messageId'");
	if(cursor.moveToNext() = true) then
		frequency <- cursor.getString(cursor.getColumnIndex("message_frequency"));
	endif
	return frequency;
end

23
Logika Proses
Procedure saveScheduleToTime(long timemillis, long timesent, String messageId)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
contentValues : ContentValues, //sebagai inisiasi terhadap class ContentValues
begin
	contentValues.put("time_timemillis", timemillis);
	contentValues.put("time_message_id", messageId);
	contentValues.put("time_timesent", timesent);
	databaseWriteable.insert("time", null, contentValues);
	contentValues.clear();
end

28
Logika Proses
Procedure repetition(PendingIntent pending, AlarmManager alarm, TimeListDatabaseHelper databaseHelper, String[] data, long[] time)
var
timemillis : long, //sebagai penampung data timemillis
timesent : long, //sebagai penampung data timesent
frequency : String, //sebagai penampung data frequency
messageId : String, //sebagai penampung data messageId
interval : long, //sebagai penampung data interval
begin
	timemillis <- time[0];
	timesent <- time[1];
	
	frequency <- data[0];
	messageId <- data[1];
	
	interval <- interval(frequency);
	
	alarm.setRepeating(AlarmManager.RTC_WAKEUP, timesent, interval, pending);
	for(int remaining <- Integer.parseInt(data[2]) to remaining > 1) do
		timemillis <- databaseHelper.addTimemillisFromTime(timemillis);
		databaseHelper.saveScheduleToTime(timemillis, timesent, messageId);
	endfor
end