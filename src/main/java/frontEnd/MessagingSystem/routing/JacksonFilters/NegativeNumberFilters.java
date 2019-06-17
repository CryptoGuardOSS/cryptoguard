package frontEnd.MessagingSystem.routing.JacksonFilters;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * @author franceme
 * Created on 2019-06-15.
 * @since 03.06.01
 *
 * <p>NegativeNumberFilters</p>
 * <p>This filter will check if the input number is greater than 0 or not.</p>
 * If the condition matches, it won't set the value.
 */
@Log4j2
@Data
public class NegativeNumberFilters {

    //region Overridden Methods
    @Override
    public boolean equals(Object input) {

        return input == null
                || !input.getClass().equals(Integer.class)
                || (Integer) input <= 0;
    }
    //endregion
}