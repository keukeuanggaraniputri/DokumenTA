15
Logika Proses
function getRecipients(String messageId) : String
var
recipients : String, //penampung data recipients yang diambil dari database
contact : ArrayList<String>, //penampung data contact 
sb : StringBuilder, //sebagai inisiasi dari class StringBuilder
begin
	while(recipient.moveToNext()) do
		contact.add(recipient.getString(recipient.getColumnIndex("recipient_number")));
	endwhile
	
	sb.append(contact.get(0));
	for(int i <- 1 to i < contact.size()) do
		sb.append(";");
		sb.append(contact.get(i));
	endfor
	
	recipients = sb.toString();
	return recipients;
end

18
Logika Proses
function getMessageDatetime(String messageId) : String
var
datetime : String, //sebagai penampung data datetime hasil fetch dari database
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
cursor : Cursor, //sebagai penampung dari data yang berhasil diambil dari database
begin
	cursor <- databaseReadable.rawQuery("select message_timedate from table message where message_id='messageId'");
	if(cursor.moveToNext() = true) then
		datetime <- cursor.getString(cursor.getColumnIndex("message_timedate"));
	endif
	return datetime;
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

25
Logika Proses
function getTime(String messageId) : Cursor
var
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
cursor : Cursor, //sebagai penampung dari data yang berhasil diambil dari database
begin
	cursor <- databaseReadable.rawQuery("select * from table time where time_message_id='messageId'");
	return cursor;
end

28
Logika Proses
function count(Cursor cursor) : int
var
count : int, //sebagai penampung berapa banyak row record yang ada pada cursor
begin
	count <- cursor.getCount();
	return count;
end

31
Logika Proses
function getMessage(String messageId) : String
var
message : String, //sebagai penampung data message hasil fetch dari database
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
cursor : Cursor, //sebagai penampung dari data yang berhasil diambil dari database
begin
	cursor <- databaseReadable.rawQuery("select message_message from table message where message_id='messageId'");
	if(cursor.moveToNext() = true) then
		message <- cursor.getString(cursor.getColumnIndex("message_message"));
	endif
	return message;
end

48
Logika Proses
Procedure deleteRecipient(String messageId)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
begin
	databaseWriteable.rawQuery("delete from recipient where recipient_message_id='messageId'");
end

49
Logika Proses
Procedure saveScheduleToRecipient(String recipients, String messageId)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
st : StringTokenizer, //sebagai inisiasi terhadap class StringTokenizer
tempPhoneNumber : String, //sebagai penampung data nomor recipent
contentValues : ContentValues, //sebagai inisiasi terhadap class ContentValues
begin
	StringTokenizer st <- new StringTokenizer(recipients, ";");
	while (st.hasMoreelements()) do
		tempPhoneNumber <- (String) st.nextElement();
		contentValues.put("recipient_number", tempPhoneNumber);
		contentValues.put("recipient_message_id", messageId);
		databaseWriteable.insert("recipient", null, contentValues);
		contentValues.clear();
	endWhile
	contentValues.clear();
end

51
Logika Proses
function getType() : String
var
begin
	return type;
end
/*
Logika Proses
Prosedur setMessageType(String message)
var
type : String, //sebagai penampung type dari message
begin
	if(message.contain("%%")) then
		this.type <- "typical";
		else this.type <- "normal";
	endif
end
*/

53
Logika Proses
Procedure updateType(String type, String content, String messageId)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
begin
	if(type = "normal") then
		databaseWriteable.rawQuery("update normal_message set nm_message='content' where nm_message_id='messageId'");
		else databaseWriteable.rawQuery("update typical_message set tm_message='content' where tm_message_id='messageId'");
	endif
end
/*
Logika Proses
Procedure deleteType(String type, String messageId)
var
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
data
begin
	if(type = "normal") then
		databaseReadable.rawQuery("delete normal_message where nm_message_id='messageId'");
		else databaseReadable.rawQuery("delete typical_message where tm_message_id='messageId'");
	endif
end
*/

54
Logika Proses
Prosedur updateContent(String messageId, String data)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
begin
	databaseWriteable.rawQuery("update message set message_message='data' where message_id='messageId'");
end

55
Logika Proses
Procedure deleteTime(String messageId)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
begin
	databaseWriteable.rawQuery("delete from time where time_message_id='messageId'");
end

56
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

61
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


































Logika Proses
function getScheduleList() : Cursor
var
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
begin
	return databaseReadable.rawQuery("select * from message");
end

Logika Proses
Procedure updateContent(String messageId, String data)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
begin
	databaseWriteable.rawQuery("update message set message_content='data' where message_id='message_id'");
end

Logika Proses
Procedure updateTime(String messageId, String[] data)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
dataRepetition : String[3], //sebagai penampung data frequency, messageId, dan remaining
time : long[2], //sebagai penampung data timesent(waktu pengiriman) dan timemillis (sebagai primary key untuk table time)
begin
	databaseWriteable.rawQuery("delete from time where time_message_id='messageId'");
	if(data[4] = 'Once') then
		dataRepetition[0] <- data[4];
		dataRepetition[1] <- messageId;
		dataRepetition[2] <- data[3];
		
		time[0] = addTimemillisFromTime(data[0]);
		time[1] = data[0];
end

Logika Proses
Procedure updateRecipient(String messageId, String data)
var
databaseWriteable : openHelper. getWriteableDatabase(), //sebagai inisiasi method untuk mekalukan query write (tulis) ke database
begin
	
end