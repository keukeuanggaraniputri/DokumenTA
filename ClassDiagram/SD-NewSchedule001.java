//all
databaseHelper : TimeListDatabaseHelper
timemillis, timesent : Long
recipient, datetime, content, freqTime : EditText Widget
btn_save, btn_cancel : Button Widget
frequency : Spinner Widget
freq = { "Once", "hourly", "daily", "weekly", "monthly", "yearly", "2 hourly", "4 hourly", "6 hourly", 
		 "8 hourly", "12 hourly", "2 weekly", "3 weekly", "2 monthly", "4 monthly", "6 monthly"} : Array of String
date_schedule : DatePicker 
time_schedule : TimePicker
data : Array of String
check : boolean 
freqtimes : Frequency
schedule : Schedule
intent : Intent
pending : PendingIntent
alarm : AlarmManager
dataRepetition : Array of String
time : Array of long

begin
	recipient <- new EditText();
	datetime <- new EditText();
	content <- new EditText();
	freqTime <- new EditText();
	frequency <- new Spinner(freq);
	data[0] <- datetime.getText();
	data[1] <- recipient.getText();
	data[2] <- content.getText();
	data[3] <- freqtime.getText();
	data[4] <- frequency.getText();
	
	if (btn_save.clicked) then
			timesent <- timemillis;
			timemillis <- databaseHelper.addTimemillis(timemillis);
			schedule <- new Schedule(timemillis, timesent, data);
			schedule.setMessageType(schedule.contentMessages);		//check the type of message, whether dynamic or static message -> contain %%y, etc
			data[7] = schedule.getType();		
			databaseHelper.saveScheduletoMessage(schedule.getTimemillis(), schedule.getTimesent(), data);
			databaseHelper.saveScheduleToType(data[7], data[2], schedule.getScheduleId);
			databaseHelper.saveScheduleToTime(timemillis, timesent, schedule.getScheduleId);
			databaseHelper.saveScheduleToContact(schedule.getRecipientNumbers());
			databaseHelper.saveScheduleToRecipient(data[1], schedule.getScheduleId);
			intent = new Intent();
			intent.putExtra("timemillis", timesent);
			pending = PendingIntent.getActivity();
			alarm = getSystemService(ALARM_SERVICE);
			if (data[4] = "Once") then
				dataRepetition[0] <- data[3];
				dataRepetition[1] <- schedule.getScheduleId();
				dataRepetition[2] <- data[4];
				time[0] <- timemillis;
				time[1] <- timesent;
				freqtimes.repetition(pending, alarm, databaseHelper, dataRepetition, time);
				else 
				alarm.set(AlarmManager.RTC_WAKEUP, timesent, pending);
			endif	
	endif
end

14
Logika Proses
Prosedur addTimemillis(timemillis)
var
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
cursor : Cursor, //sebagai penampung dari data yang berhasil diambil dari database
begin
	cursor <- databaseReadable.rawQuery("select time_timemillis from time");
	
	while(cursor.moveToNext() == true) do
		if (cursor.getLong(cursor.getColumnIndex("time_timemillis")) == timemillis) then
			timemillis <- timemillis + 1;
		endif
	endwhile
end

17
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

18
Logika Proses
function getType() : String
var
begin
	return type;
end

20
Procedure saveScheduletoMessage(long timemillis, String[] data)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
contentValues : ContentValues, //sebagai inisiasi terhadap class ContentValues
begin
	contentValues.put("message_timedate", data[0]);
	contentValues.put("message_message", data[2]);
	contentValues.put("message_status", data[5]);
	contentValues.put("message_timemillis", timemillis);
	contentValues.put("message_type", data[7]);
	contentValues.put("message_frequency", data[3]);
	databaseWriteable.insert("message", null, contentValues);
	contentValues.clear();
end

21
Procedure saveScheduleToType(String type, String content, String messageId)
var
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
contentValues : ContentValues, //sebagai inisiasi terhadap class ContentValues
begin
	if(type = "normal") then
		contentValues.put("nm_message", content);
		contentValues.put("nm_message_id", messageId);
		databaseWriteable.insert("normal_message", null, contentValues);
		else 
			contentValues.put("tm_message", content);
			contentValues.put("tm_message_id", messageId);
			databaseWriteable.insert("typical_message", null, contentValues);
	endif
	contentValues.clear();
end

22
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

23
Logika Proses
Procedure saveScheduleToContact(String phoneNumbers)
var
databaseReadable : openhelper.getReadableDatabase(), //sebagai inisiasi method untuk melakukan query read (baca) dari database
databaseWriteable : openHelper.getWriteableDatabase(), //sebagai inisiasi method untuk melakukan query write (tulis) ke database
contentValues : ContentValues, //sebagai inisiasi terhadap class ContentValues
st : StringTokenizer, //sebagai inisiasi terhadap class StringTokenizer
tempPhoneNumber : String. //sebagai penampung nomor recipient
begin
	st <- new StringTokenizer(phoneNumbers, ";");
	while(st.hasMoreElements()) do
		tempPhoneNumber <- (String) st.nextElement();
		cursor <- databaseReadable.rawQuery("select * from contact where contact_number='temptPhoneNumber'");
		if(cursor.moveToNext() = false) then
			contentValues.put("contact_number", tempPhoneNumber);
			databaseWriteable.insert("contact", null, contentValues);
		endif
	endwhile
	contentValues.clear();
end

24
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

31
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