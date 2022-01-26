package observer;

import enums.NotificationCode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author StefanP
 * Ova klasa omogucava da vise stvari posaljemo preko Observer sablona.
 * Ovo nam je bio Postman u Gerudoku.
 * */
@Data
@AllArgsConstructor		//omogucava konstruktor sa svim argumentima
public class Notification {
	private NotificationCode code;
	private Object object;
}
