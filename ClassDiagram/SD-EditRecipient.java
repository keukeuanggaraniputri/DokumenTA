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

20
Logika Proses
Procedure deleteRecipient(String messageId)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
begin
	databaseWriteable.rawQuery("delete from recipient where recipient_message_id='messageId'");
end

21
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