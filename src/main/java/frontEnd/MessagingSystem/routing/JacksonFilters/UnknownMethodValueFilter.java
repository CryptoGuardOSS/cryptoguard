package frontEnd.MessagingSystem.routing.JacksonFilters;

import frontEnd.MessagingSystem.routing.structure.Scarf.Method;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

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
public class UnknownMethodValueFilter {

    //region Overridden Methods
    @Override
    public boolean equals(Object input) {

        if (input == null || !input.getClass().equals(ArrayList.class) || ((ArrayList) input).isEmpty())
            return true;

        ArrayList<Method> foundMethods = (ArrayList<Method>) input;

        Iterator<Method> roundRobin = foundMethods.iterator();
        while (roundRobin.hasNext()) {
            String name = StringUtils.trimToNull(roundRobin.next().getSelf());
            if (name == null || name.equals("UNKNOWN"))
                roundRobin.remove();
        }

        return foundMethods.isEmpty();

    }
    //endregion

}
