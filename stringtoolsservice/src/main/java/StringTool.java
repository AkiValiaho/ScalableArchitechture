import com.akivaliaho.AppendStringsEventResult;
import com.akivaliaho.config.annotations.Interest;
import org.springframework.stereotype.Component;

/**
 * Created by akivv on 9.5.2017.
 */
@Component
public class StringTool {


    @Interest(value = "com.akivaliaho.service.events.AppendStrings")
    public AppendStringsEventResult stringAppend(String a, String b) {
        return new AppendStringsEventResult(a + b);
    }


}
