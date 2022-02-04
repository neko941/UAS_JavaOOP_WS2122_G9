/**
 * @author jatenderjossan, neko941
 * Created on: Dec. 25, 2021
 */

package Models;

import java.time.LocalDateTime;

public enum Reminder {
    ONE_WEEK(10080),
    THREE_DAYS(4320),
    ONE_HOUR(60),
    TEN_MINUTES(10);

    public final Integer value;

    Reminder(Integer value)
    {
        this.value = value;
    }

    public LocalDateTime getReminderTime(LocalDateTime startDay)
    {
        return startDay.plusMinutes(value);
    }
}