
package adsstest.feed;

import adsstest.exceptions.MessageException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Chukwudi Alimele
 */
public class Message implements Serializable{

    public String type;
    public String id;
    public String[] fields = new String[0];
    public final char SEPARATOR = ',';
    
    public Message(String raw) throws MessageException {
        parseCSV(raw, SEPARATOR);       
        
        if(fields.length > 0){
            type = fields[0];
        }else{
            throw new MessageException("Invalid message - message cannot be empty");
        }
        
        if(fields.length > 1){
            id = fields[1];
        }else{
            throw new MessageException("Invalid message - could not determine message ID");
        }
        
        if(fields.length > 13){
            throw new MessageException("Invalid message - number of fields cannot be greater than 13");
        }
        
        if(fields.length < 10){
            throw new MessageException("Invalid message - number of fields cannot be less than 10");
        }
        
    }

    
    /**
     * CSV record parser. Convert a CSV record to a List of Strings representing the fields of the
     * CSV record. The CSV record is expected to be separated by the specified CSV field separator.
     * @param record The CSV record.
     * @param csvSeparator The CSV field separator to be used.
     */
    private void parseCSV(String record, char csvSeparator) {

        // Prepare.
        boolean quoted = false;
        StringBuilder fieldBuilder = new StringBuilder();
        List<String> flds = new ArrayList<>();

        // Process fields.
        for (int i = 0; i < record.length(); i++) {
            char c = record.charAt(i);
            fieldBuilder.append(c);

            if (c == '"') {
                quoted = !quoted; // Detect nested quotes.
            }

            if ((!quoted && c == csvSeparator) // The separator ..
                    || i + 1 == record.length()) // .. or, the end of record.
            {
                String field = fieldBuilder.toString() // Obtain the field, ..
                        .replaceAll(csvSeparator + "$", "") // .. trim ending separator, ..
                        .replaceAll("^\"|\"$", "") // .. trim surrounding quotes, ..
                        .replace("\"\"", "\""); // .. and un-escape quotes.
                flds.add(field.trim()); // Add field to List.
                fieldBuilder = new StringBuilder(); // Reset.
            }
        }

        fields = new String[flds.size()];
        
        flds.toArray(fields);
        
    }
    
    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String[] getFields() {
        return fields;
    }

    public Object getField(int index) {
        return fields[index];
    }

}
