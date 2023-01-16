package Game;

import java.io.Serializable;
import java.util.Objects;

public record Tuple<X,Y>(X x, Y y) implements Serializable {
    @Override
    public int hashCode(){
        return Objects.hash(x,y);
    }
}
