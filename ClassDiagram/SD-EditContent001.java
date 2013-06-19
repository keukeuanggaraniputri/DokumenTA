15
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

23
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

24
Logika Proses
Prosedur updateContent(String messageId, String data)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
begin
	databaseWriteable.rawQuery("update message set message_message='data' where message_id='messageId'");
end
