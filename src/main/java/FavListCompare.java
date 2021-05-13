import java.util.*;
import org.bson.Document;
import com.google.common.collect.Maps;
import com.google.common.collect.MapDifference;

public class FavListCompare {
    public static void main(String[] args) {
        // Creating some test data to do some comparisons.  This kind of look like fav lists.
        // Notice they are type "Document".  So exactly what you get back from mongo.

        // Original Object
        Document originalFav = new Document();
        originalFav.put("name", "list 1");
        originalFav.put("description", "this is a awesome list 1");
        originalFav.put("members", Arrays.asList("item1", "item2"));

        Document favItem1 = new Document();
        favItem1.put("sid", "bclevay");
        favItem1.put("dn", "cn=bclevay");

        Document favItem2 = new Document();
        favItem2.put("sid", "jlwilso");
        favItem2.put("dn", "cn=jlwilso");

        originalFav.put("favorites", Arrays.asList(new Document(favItem1), new Document(favItem2)));

        // Udated Object
        Document updatedFav = new Document();
        updatedFav.put("name", "list 1");
        updatedFav.put("description", "this is an ok list 2");
        updatedFav.put("members", Arrays.asList("item2", "item3"));

        Document favItem3 = new Document();
        favItem3.put("sid", "bclevay1");
        favItem3.put("dn", "cn=bclevay1");
        updatedFav.put("favorites", Arrays.asList(new Document(favItem1), new Document(favItem3)));

        // Use guava to find the differences.  A entry in 'diffEntries' means it found a difference.
        MapDifference difference = Maps.difference(originalFav, updatedFav);
        Map diffEntries = difference.entriesDiffering();

        // Print the differences
        for (Object key : diffEntries.keySet()) {
            // Get the values and look at one of their types
            // If it is a List do some additional processing
            // If it isn't a List just print it
            Object o = diffEntries.get(key);
            Object originalValue = ((MapDifference.ValueDifference) o).leftValue();
            Object updatedValue = ((MapDifference.ValueDifference) o).rightValue();

            if (originalValue instanceof List) {
                System.out.println(key + " was changed ");
                List<String> added = new ArrayList((List) updatedValue);
                added.removeAll(new HashSet<>((List) originalValue));
                System.out.println("  Items added: " + added);

                List<String> removed = new ArrayList((List) originalValue);
                removed.removeAll(new HashSet<>((List) updatedValue));
                System.out.println("  Items removed: " + removed);
            } else {
                System.out.println(key + " was changed from '" + originalValue + "' to '" + updatedValue + "'");
            }
        }
    }
}
