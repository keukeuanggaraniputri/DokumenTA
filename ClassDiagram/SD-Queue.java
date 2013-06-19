Logika Proses
function getMessageIdFromTime(long timemillis) : ArrayList<String>
var
cursor : Cursor, //penampung data yang diambil dari database
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
messageId : ArrayList<String>, //penampung data id message yang didapat dari database
begin
	cursor <- databaseReadable.rawQuery("select time_message_id from time where time_timesent='timemillis'");
	while(cursor.movetToNext()) do 
		messageId.add(cursor.getString(cursor.getColumnIndex("time_message_id")));
	endwhile
	return messageId;
end

Logika Proses
function getMessageFromMessage(msgId) : String
var
message : String, //penampung data message atau content yang didapat dari database
cursor : Cursor, //penampung data yang diambil dari database
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
begin
	message <- null;
	cursor <- databaseReadable.rawQuery("select message_message from message where message_id='msgId'");
	if(cursor.moveToNext() = true) then
		message <- cursor.getString(cursor.getColumnIndex("message_message"));
	endif
	return message;
end

Logika Proses
function getRecipientFromRecipient(msgId) : Cursor
var
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
begin
	return databaseReadable.rawQuery("select * from recipient_number from recipient where recipient_message_id='msgId'");
end

Logika Proses
Procudure sendSMS(phoneNumber, message)
var
sms : SmsManager, //inisiasi class SmsManager untuk dapat menggunakan method dari class ini
sentPI : PendingIntent, //penampung bila sms berhasil dikirimkan
deliveredPI : PendingIntent, //penampung bila sms gagal dikirimkan
begin
	SmsManager sms <- SmsManager.getDefault();
	sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
end